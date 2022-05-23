import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())

@ExperimentalSerializationApi
fun main() {
    val config = PostgresConfig(
        database = "test",
        hosts = "localhost",
        password = "test",
        port = 5432,
        ssl = false,
        username = "test",
    )

    val dataAccess = DataAccess(config)
    val dao = Dao(dataAccess)
    val iterations = 100_000

    val noJsonElapsed = measureTimeMillis {
        for (i in 1..iterations) {
            dao.findById(1)
        }
    }

    val jsonElapsed = measureTimeMillis {
        for (i in 1..iterations) {
            dao.findByIdWithJson(1)
        }
    }

    println("No JSON: ${noJsonElapsed / iterations}ms, ${noJsonElapsed}ms total")
    println("JSON: ${jsonElapsed / iterations}ms, ${jsonElapsed}ms total")
}

@Serializable
data class Thing(
    val id: Int,
    val text1: String,
    val text2: String,
    val text3: String,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val create_ts: ZonedDateTime,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val last_ts: ZonedDateTime,
)
