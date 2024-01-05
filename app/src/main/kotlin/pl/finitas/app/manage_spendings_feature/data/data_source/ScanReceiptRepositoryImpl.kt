package pl.finitas.app.manage_spendings_feature.data.data_source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import pl.finitas.app.manage_spendings_feature.domain.model.ReceiptParseResult
import pl.finitas.app.manage_spendings_feature.domain.repository.ScanReceiptRepository

class ScanReceiptRepositoryImpl(private val httpClient: HttpClient) : ScanReceiptRepository {
    override suspend fun scanReceipt(file: ByteArray): ReceiptParseResult {
        println("&*******************************")
        println("&*******************************")
        println("&*******************************")
        println("&*******************************")
        println(file.size)
        return httpClient.post("http://192.168.1.32:8080/api/receipts/parse") {
            formData {
                append("file", file, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=receipt.jpg")
//                    append(HttpHeaders.ContentType, "multipart/form-data")
                })
            }
            headers {
                append("Content-Type", "multipart/form-data; boundary=----abc")
            }
//            contentType(ContentType.MultiPart.FormData)
        }.body()
    }
}