package ru.kpfu.itis.gureva.homeworks_android.utils

import java.util.regex.Pattern

object RegexUtil {
    const val EMAIL = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" +
            "*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"

    const val PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"

    const val NAME = "^[a-zA-Z\\s0-9]+$"

    const val PHONE = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$"

    fun check(regex: String, input: String): Boolean {
        return Pattern.compile(regex).matcher(input).matches()
    }
}
