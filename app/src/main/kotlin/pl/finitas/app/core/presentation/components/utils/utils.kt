package pl.finitas.app.core.presentation.components.utils

fun String.trimOnOverflow(length: Int = 20) =
    if (this.length > length)
        "${substring(0, length)}..."
    else this