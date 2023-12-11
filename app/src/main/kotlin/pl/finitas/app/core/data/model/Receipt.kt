package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Receipt(
    val photo: ByteArray,
    @PrimaryKey val idReceipt: UUID,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Receipt

        if (!photo.contentEquals(other.photo)) return false
        if (idReceipt != other.idReceipt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = photo.contentHashCode()
        result = 31 * result + idReceipt.hashCode()
        return result
    }
}
