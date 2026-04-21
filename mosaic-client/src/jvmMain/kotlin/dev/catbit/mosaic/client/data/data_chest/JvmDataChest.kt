package dev.catbit.mosaic.client.data.data_chest

import java.io.File
import java.util.prefs.Preferences

class JvmDataChest(
    private val preferences: Preferences,
    private val dataDir: File,
) : DataChest {

    private fun fileFor(key: String) = File(dataDir, key.replace(Regex("[/\\\\:*?\"<>|]"), "_"))

    override fun clear() {
        dataDir.listFiles()?.forEach { it.delete() }
        preferences.clear()
    }

    override fun remove(key: String) {
        fileFor(key).delete()
        preferences.remove(key)
    }

    override fun hasKey(key: String): Boolean =
        fileFor(key).exists() || preferences.keys().contains(key)

    override fun putInt(key: String, value: Int) {
        preferences.putInt(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        getIntOrNull(key) ?: defaultValue

    override fun getIntOrNull(key: String): Int? =
        preferences.takeIf { hasKey(key) }?.getInt(key, Int.MAX_VALUE)

    override fun putLong(key: String, value: Long) {
        preferences.putLong(key, value)
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        getLongOrNull(key) ?: defaultValue

    override fun getLongOrNull(key: String): Long? =
        preferences.takeIf { hasKey(key) }?.getLong(key, Long.MAX_VALUE)

    override fun putString(key: String, value: String) {
        fileFor(key).writeText(value)
    }

    override fun getString(key: String, defaultValue: String): String =
        getStringOrNull(key) ?: defaultValue

    override fun getStringOrNull(key: String): String? {
        val file = fileFor(key)
        return if (file.exists()) file.readText() else null
    }

    override fun putFloat(key: String, value: Float) {
        preferences.putFloat(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float): Float =
        getFloatOrNull(key) ?: defaultValue

    override fun getFloatOrNull(key: String): Float? =
        preferences.takeIf { hasKey(key) }?.getFloat(key, Float.MAX_VALUE)

    override fun putBoolean(key: String, value: Boolean) {
        preferences.putBoolean(key, value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        getBooleanOrNull(key) ?: defaultValue

    override fun getBooleanOrNull(key: String): Boolean? =
        preferences.takeIf { hasKey(key) }?.getBoolean(key, false)
}
