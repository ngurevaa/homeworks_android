package ru.kpfu.itis.gureva.homeworks_android.utils

import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object PasswordUtil {
    fun encrypt(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hashInBytes = md.digest(password.toByteArray(StandardCharsets.UTF_8))
        val sb = StringBuilder()
        for (b in hashInBytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }
}
