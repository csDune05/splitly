package com.example.split.auth

data class LoginValidationResult(
    val usernameError: String? = null,
    val passwordError: String? = null,
) {
    val isValid: Boolean = usernameError == null && passwordError == null
}

data class SignupValidationResult(
    val usernameError: String? = null,
    val passwordError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
) {
    val isValid: Boolean =
        usernameError == null &&
            passwordError == null &&
            emailError == null &&
            phoneError == null
}

object AuthValidation {
    private val emailRegex = Regex("""^[\w.-]+@([\w-]+\.)+[\w]{2,4}$""")
    private val phoneRegex = Regex("""^\d{10}$""")

    fun validateLogin(username: String, password: String): LoginValidationResult =
        LoginValidationResult(
            usernameError = usernameError(username),
            passwordError = passwordError(password),
        )

    fun validateSignup(
        username: String,
        password: String,
        email: String,
        phoneNumber: String,
    ): SignupValidationResult =
        SignupValidationResult(
            usernameError = usernameError(username),
            passwordError = passwordError(password),
            emailError = when {
                email.isBlank() -> "Please enter your email"
                !emailRegex.matches(email) -> "Please enter a valid email address"
                else -> null
            },
            phoneError = when {
                phoneNumber.isBlank() -> "Please enter your phone number"
                !phoneRegex.matches(phoneNumber) -> "Phone number must contain exactly 10 digits"
                else -> null
            },
        )

    private fun usernameError(username: String): String? =
        if (username.isBlank()) "Please enter your username" else null

    private fun passwordError(password: String): String? =
        when {
            password.isBlank() -> "Please enter your password"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
}
