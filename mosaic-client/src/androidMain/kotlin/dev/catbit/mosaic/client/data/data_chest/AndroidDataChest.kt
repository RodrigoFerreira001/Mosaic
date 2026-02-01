package dev.catbit.mosaic.client.data.data_chest

import android.content.SharedPreferences
import androidx.core.content.edit

class AndroidDataChest(
    private val sharedPreferences: SharedPreferences
) : DataChest {

    override fun clear() {
        sharedPreferences.edit {
            clear()
            commit()
        }
    }

    override fun remove(key: String) {
        sharedPreferences.edit {
            remove(key)
            commit()
        }
    }

    override fun hasKey(key: String) = sharedPreferences.contains(key)

    override fun putInt(
        key: String,
        value: Int
    ) {
        sharedPreferences.edit {
            putInt(key, value)
            commit()
        }
    }

    override fun getInt(
        key: String,
        defaultValue: Int
    ): Int = getIntOrNull(key) ?: defaultValue

    override fun getIntOrNull(key: String): Int? =
        sharedPreferences.takeIf { it.contains(key) }?.getInt(key, Int.MAX_VALUE)

    override fun putLong(key: String, value: Long) {
        sharedPreferences.edit {
            putLong(key, value)
            commit()
        }
    }

    override fun getLong(
        key: String,
        defaultValue: Long
    ): Long = getLongOrNull(key) ?: defaultValue

    override fun getLongOrNull(key: String): Long? =
        sharedPreferences.takeIf { it.contains(key) }?.getLong(key, Long.MAX_VALUE)

    override fun putString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
            commit()
        }
    }

    override fun getString(
        key: String,
        defaultValue: String
    ): String = getStringOrNull(key) ?: defaultValue

    override fun getStringOrNull(key: String): String? =
        sharedPreferences.getString(key, null)

    override fun putFloat(key: String, value: Float) {
        sharedPreferences.edit {
            putFloat(key, value)
            commit()
        }
    }

    override fun getFloat(
        key: String,
        defaultValue: Float
    ): Float = getFloatOrNull(key) ?: defaultValue

    override fun getFloatOrNull(key: String): Float? =
        sharedPreferences.takeIf { it.contains(key) }?.getFloat(key, Float.MAX_VALUE)

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
            commit()
        }
    }

    override fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean = getBooleanOrNull(key) ?: defaultValue

    override fun getBooleanOrNull(key: String): Boolean? =
        sharedPreferences.takeIf { it.contains(key) }?.getBoolean(key, false)
}