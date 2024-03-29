package pl.finitas.app.core.data.data_source

import androidx.room.TypeConverter
import pl.finitas.app.core.data.model.Authority
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): Long {
        return localDateTime.toInstant(currentZoneOffset).toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(timestamp: Long): LocalDateTime {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    private val currentZoneOffset
        get() = ZoneId.systemDefault().rules.getOffset(Instant.now())
}

class BigDecimalConverter {
    @TypeConverter
    fun fromBigDecimal(bigDecimal: BigDecimal) = bigDecimal.toString()

    @TypeConverter
    fun toBigDecimal(str: String) = str.toBigDecimal()
}

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): UUID? {
        return string?.let { UUID.fromString(it) }
    }
}

class AuthoritiesConverter {
    @TypeConverter
    fun fromEnumSet(enumSet: Set<Authority>): String {
        return enumSet.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromString(string: String): Set<Authority> {
        if (string.isEmpty()) return setOf()
        return string.split(",").map { Authority.valueOf(it) }.toSet()
    }
}