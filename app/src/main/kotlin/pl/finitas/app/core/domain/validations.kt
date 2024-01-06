package pl.finitas.app.core.domain

import pl.finitas.app.core.domain.exceptions.InputValidationException
import java.util.LinkedList

class ValidationBuilder {
    val errors = mutableMapOf<String?, LinkedList<String>>()
    var singleError: String? = null
        private set
    fun validate(value: Boolean, key: String? = null, lazyMessage: () -> Any) {
        if(!value) {
            errors.getOrPut(key) { LinkedList() } += lazyMessage().toString()
        }
    }

    fun validateForSingleOutput(value: Boolean, lazyMessage: () -> Any) {
        if (!value) {
            singleError = lazyMessage().toString()
        }
    }
}

fun validateBuilder(soft: Boolean = false, validateBody: ValidationBuilder.() -> Unit): Map<String?, List<String>> {
    val builder = ValidationBuilder()
    builder.validateBody()
    if (!soft) {
        if (builder.singleError != null) {
            throw InputValidationException(builder.singleError!!)
        }
        builder.errors.let {
            if (it.isNotEmpty()) {
                throw InputValidationException(it)
            }
        }
    }
    return builder.errors
}
