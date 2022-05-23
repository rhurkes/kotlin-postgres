@file:Suppress("unused")

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.ZonedDateTime
import javax.sql.DataSource
import kotlinx.serialization.Serializable

class DataAccess (config: PostgresConfig) {
    private val dataSource = getPostgresDataSource(config)

    fun <T> findOne(sql: String, mapper: (rs: ResultSet) -> T, vararg args: Any?): T? {
        return callInternal(sql, { statement: PreparedStatement ->
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                mapper.invoke(resultSet)
            } else {
                null
            }
        }, *args)
    }

    /**
     * Creates and cleans up a prepared statement with a user provided action on the statement.
     */
    private fun <T> callInternal(sql: String, action: (statement: PreparedStatement) -> T, vararg args: Any?): T {
        var conn: Connection? = null
        var statement: PreparedStatement? = null
        try {
            conn = dataSource.connection
            statement = conn.prepareStatement(sql)
            statement.setParameters(*args)
            return action(statement)
        } finally {
            conn?.close()
            statement?.close()
        }
    }

    private fun PreparedStatement.setParameters(vararg args: Any?) {
        for (i in args.indices) {
            val arg = args[i]
            val statementIndex = i + 1
            when (arg) {
                is String -> this.setString(statementIndex, arg)
                is Long -> this.setLong(statementIndex, arg)
                is Int -> this.setInt(statementIndex, arg)
                is Double -> this.setDouble(statementIndex, arg)
                is ZonedDateTime -> this.setObject(statementIndex, java.sql.Timestamp.valueOf(arg.toLocalDateTime()))
                else -> this.setObject(statementIndex, arg)
            }
        }
    }

    private fun getPostgresDataSource(config: PostgresConfig): DataSource {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = "jdbc:postgresql://${config.hosts}:${config.port}/${config.database}"
        dataSource.connectionTimeout = config.connectionTimeoutMillis
        dataSource.idleTimeout = config.idleTimeoutMillis
        dataSource.minimumIdle = config.minIdleConnections
        dataSource.maximumPoolSize = config.maxConnectionPoolSize

        if (config.ssl) {
            dataSource.addDataSourceProperty("ssl", true)
            dataSource.addDataSourceProperty("sslmode", "require")
        }

        if (!config.targetServerType.isNullOrEmpty()) {
            dataSource.addDataSourceProperty("targetServerType", config.targetServerType)
        }

        dataSource.username = config.username
        dataSource.password = config.password

        return dataSource
    }
}

@Serializable
data class PostgresConfig(
    val connectionTimeoutMillis: Long = 10_000,
    val database: String,
    val hosts: String,
    val idleTimeoutMillis: Long = 300_000,
    val minIdleConnections: Int = 1,
    val maxConnectionPoolSize: Int = 1,
    val password: String,
    val port: Long,
    val ssl: Boolean,
    val targetServerType: String? = null,
    val username: String,
)
