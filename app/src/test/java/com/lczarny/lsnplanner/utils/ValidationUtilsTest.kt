package com.lczarny.lsnplanner.utils

import kotlinx.coroutines.test.runTest
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun testValidEmail() = runTest {
        assert("test@mail.com".isValidEmail())

        assert("test".isValidEmail().not())
        assert("test@".isValidEmail().not())
        assert("test@mail".isValidEmail().not())
        assert("test@mail.".isValidEmail().not())
    }

    @Test
    fun testValidPassword() = runTest {
        assert("Test1234".isValidPassword())

        // Too short
        assert("Test123".isValidPassword().not())
        // No uppercase letter
        assert("test1234".isValidPassword().not())
        // No number
        assert("testtest".isValidPassword().not())
        // No letters
        assert("12345678".isValidPassword().not())
    }
}