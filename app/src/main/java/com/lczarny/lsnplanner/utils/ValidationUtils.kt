package com.lczarny.lsnplanner.utils

import androidx.core.util.PatternsCompat

fun String.isValidEmail(): Boolean = this.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = this.isNotEmpty() && Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$").matches(this)