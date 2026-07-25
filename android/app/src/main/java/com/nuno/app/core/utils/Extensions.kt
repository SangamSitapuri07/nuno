package com.nuno.app.core.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

@Composable
fun <T> Flow<T>.collectAsEffect(
    onCollect: suspend (T) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(context) {
        this@collectAsEffect.collectLatest(onCollect)
    }
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return this.matches(emailRegex)
}

fun String.isValidUsername(): Boolean {
    val regex = "^[a-zA-Z0-9_]{3,20}$".toRegex()
    return this.matches(regex)
}

fun String.isValidPassword(): Boolean {
    if (this.length < 8) return false
    if (!this.any { it.isUpperCase() }) return false
    if (!this.any { it.isLowerCase() }) return false
    if (!this.any { it.isDigit() }) return false
    if (!this.any { !it.isLetterOrDigit() }) return false
    return true
}

fun Int.formatCoins(): String {
    return when {
        this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format("%.1fK", this / 1_000.0)
        else -> this.toString()
    }
}