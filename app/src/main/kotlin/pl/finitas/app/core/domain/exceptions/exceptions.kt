package pl.finitas.app.core.domain.exceptions

class UserNotAuthorized: Exception("User not authorized.")
class InputValidationException(message: String): Exception(message)