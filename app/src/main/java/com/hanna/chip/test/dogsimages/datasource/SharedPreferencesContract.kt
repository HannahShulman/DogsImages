package com.hanna.chip.test.dogsimages.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesContract @Inject constructor(private val sharedPreferences: SharedPreferences) {

    @Suppress("PrivatePropertyName")
    private val LAST_BREEDS_REQUEST_TIME = "breeds_request_time"

    var breedsLastRequestTime: Long = 0L
        set(value) {
            field = value
            sharedPreferences.edit().putLong(LAST_BREEDS_REQUEST_TIME, value).apply()
        }
        get() = sharedPreferences.getLong(LAST_BREEDS_REQUEST_TIME, 0L)
}

