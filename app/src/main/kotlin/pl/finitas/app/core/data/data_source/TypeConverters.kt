package pl.finitas.app.core.data.data_source

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): Long {
        return localDateTime.toEpochSecond(currentZoneOffset)
    }

    @TypeConverter
    fun toLocalDateTime(timestamp: Long): LocalDateTime {
        return Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
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