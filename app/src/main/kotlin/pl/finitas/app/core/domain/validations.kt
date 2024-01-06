package pl.finitas.app.core.domain

import pl.finitas.app.core.domain.exceptions.InputValidationException
import java.util.LinkedList

class ValidationBuilder {
    val errors = LinkedList<String>()
    fun validate(value: Boolean, lazyMessage: () -> Any) {
        if(!value) {
            errors += lazyMessage().toString()
        }
    }
}

fun validateBuilder(validateBody: ValidationBuilder.() -> Unit) {
    val builder = ValidationBuilder()
    builder.validateBody()
    builder.errors.let {
        if (it.isNotEmpty()) {
            throw InputValidationException(it)
        }
    }
}
