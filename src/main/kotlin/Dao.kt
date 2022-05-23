import java.time.ZonedDateTime
import kotlinx.serialization.ExperimentalSerializationApi

class Dao(private val dataAccess: DataAccess) {
    fun findById(id: Int): Thing? {
        val sql = """
            select id, text1, text2, text3, create_ts, last_ts
            from data
            where id = ?;
        """.trimIndent()

        return dataAccess.findOne(sql, { resultSet ->
            Thing(
                id = resultSet.getInt("id"),
                text1 = resultSet.getString("text1"),
                text2 = resultSet.getString("text2"),
                text3 = resultSet.getString("text3"),
                create_ts = ZonedDateTime.parse(resultSet.getString("create_ts"), formatter),
                last_ts = ZonedDateTime.parse(resultSet.getString("last_ts"), formatter),
            )
        }, id)
    }

    @ExperimentalSerializationApi
    fun findByIdWithJson(id: Int): Thing? {
        val sql = """
            select jsonb_column
            from data
            where id = ?;
        """.trimIndent()

        return dataAccess.findOne(sql, { resultSet ->
            Serde.decode(resultSet.getString("jsonb_column"))
        }, id)
    }
}
