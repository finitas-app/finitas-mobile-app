package pl.finitas.app.core.domain.exceptions

class UserNotAuthorized: Exception("User not authorized.")
class InputValidationException(val errors: Map<String?, List<String>>): Exception() {
    constructor(vararg errors: String): this(mapOf(null to errors.toList()))
    constructor(errors: List<String>): this(mapOf(null to errors.toList()))
}