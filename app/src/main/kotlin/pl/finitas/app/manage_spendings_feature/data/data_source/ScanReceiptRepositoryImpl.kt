package pl.finitas.app.manage_spendings_feature.data.data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import pl.finitas.app.core.http.HttpUrls
import pl.finitas.app.manage_spendings_feature.domain.model.Base64Receipt
import pl.finitas.app.manage_spendings_feature.domain.model.ReceiptParseResult
import pl.finitas.app.manage_spendings_feature.domain.repository.ScanReceiptRepository
import java.util.Base64

class ScanReceiptRepositoryImpl(private val httpClient: HttpClient) : ScanReceiptRepository {
    override suspend fun scanReceipt(file: ByteArray): ReceiptParseResult {
        return httpClient
            .post(HttpUrls.parseReceipt) {
                setBody(Base64Receipt(value = Base64.getEncoder().encodeToString(file)))
                contentType(ContentType.Application.Json)
            }
            .body()
    }
}