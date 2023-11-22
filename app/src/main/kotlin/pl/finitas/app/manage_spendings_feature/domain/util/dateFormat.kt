package pl.finitas.app.manage_spendings_feature.domain.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.convertToView(): String = format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))