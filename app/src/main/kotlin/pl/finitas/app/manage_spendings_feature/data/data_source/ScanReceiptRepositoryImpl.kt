package pl.finitas.app.manage_spendings_feature.data.data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import pl.finitas.app.manage_spendings_feature.domain.model.ParsedEntry
import pl.finitas.app.manage_spendings_feature.domain.model.ReceiptParseResult
import pl.finitas.app.manage_spendings_feature.domain.repository.ScanReceiptRepository
import java.util.Base64

@Serializable
data class Base64Receipt(
    val value: String
)

class ScanReceiptRepositoryImpl(private val httpClient: HttpClient) : ScanReceiptRepository {
    override suspend fun scanReceipt(file: ByteArray): ReceiptParseResult {
        return httpClient
            .post("http://192.168.1.32:8080/api/receipts/parse") {
                setBody(Base64Receipt(value = Base64.getEncoder().encodeToString(file)))
                contentType(ContentType.Application.Json)
            }
            .body()
    }
}