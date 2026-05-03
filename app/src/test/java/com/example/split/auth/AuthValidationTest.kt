package com.example.split.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidationTest {
    @Test
    fun loginRequiresUsernameAndSixCharacterPassword() {
        val result = AuthValidation.validateLogin(username = "", password = "123")

        assertFalse(result.isValid)
        assertEquals("Please enter your username", result.usernameError)
        assertEquals("Password must be at least 6 characters", result.passwordError)
    }

    @Test
    fun loginAcceptsValidCredentials() {
        val result = AuthValidation.validateLogin(username = "dung", password = "123456")

        assertTrue(result.isValid)
        assertNull(result.usernameError)
        assertNull(result.passwordError)
    }

    @Test
    fun signupRequiresValidEmailAndTenDigitPhone() {
        val result = AuthValidation.validateSignup(
            username = "dung",
            password = "123456",
            email = "not-an-email",
            phoneNumber = "12345",
        )

        assertFalse(result.isValid)
        assertEquals("Please enter a valid email address", result.emailError)
        assertEquals("Phone number must contain exactly 10 digits", result.phoneError)
    }
}
