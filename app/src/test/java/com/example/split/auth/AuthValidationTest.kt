package com.example.split.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidationTest {
    @Test
    fun loginRequiresValidEmailAndSixCharacterPassword() {
        val result = AuthValidation.validateLogin(email = "not-an-email", password = "123")

        assertFalse(result.isValid)
        assertEquals("Please enter a valid email address", result.emailError)
        assertEquals("Password must be at least 6 characters", result.passwordError)
    }

    @Test
    fun loginAcceptsValidCredentials() {
        val result = AuthValidation.validateLogin(email = "dung@example.com", password = "123456")

        assertTrue(result.isValid)
        assertNull(result.emailError)
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
