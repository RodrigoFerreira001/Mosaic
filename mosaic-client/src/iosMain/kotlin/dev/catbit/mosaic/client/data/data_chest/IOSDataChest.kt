package dev.catbit.mosaic.client.data.data_chest

import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import platform.Foundation.valueForKey

class IOSDataChest(
    private val userDefaults: NSUserDefaults
) : DataChest {

    override fun clear() {
        userDefaults.removePersistentDomainForName(NSBundle.mainBundle.bundleIdentifier.orEmpty())
    }

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    override fun hasKey(key: String): Boolean =
        userDefaults.objectForKey(key) != null

    override fun putInt(key: String, value: Int) {
        with(userDefaults) {
            setInteger(value.toLong(), key)
            synchronize()
        }
    }

    override fun getInt(
        key: String,
        defaultValue: Int
    ): Int = getIntOrNull(key) ?: defaultValue

    override fun getIntOrNull(key: String): Int? =
        userDefaults.takeIf { it.valueForKey(key) != null }?.integerForKey(key)?.toInt()

    override fun putLong(key: String, value: Long) {
        with(userDefaults) {
            setInteger(value, key)
            synchronize()
        }
    }

    override fun getLong(
        key: String,
        defaultValue: Long
    ): Long = getLongOrNull(key) ?: defaultValue

    override fun getLongOrNull(key: String): Long? =
        userDefaults.takeIf { it.valueForKey(key) != null }?.integerForKey(key)

    override fun putString(key: String, value: String) {
        with(userDefaults) {
            setValue(value = value, forKey = key)
            synchronize()
        }
    }

    override fun getString(
        key: String,
        defaultValue: String
    ): String =  getStringOrNull(key) ?: defaultValue

    override fun getStringOrNull(key: String): String? = userDefaults.stringForKey(key)

    override fun putFloat(key: String, value: Float) {
        with(userDefaults) {
            setFloat(value, key)
            synchronize()
        }
    }

    override fun getFloat(
        key: String,
        defaultValue: Float
    ): Float = getFloatOrNull(key) ?: defaultValue

    override fun getFloatOrNull(key: String): Float? =
        userDefaults.takeIf { it.valueForKey(key) != null }?.floatForKey(key)

    override fun putBoolean(key: String, value: Boolean) {
        with(userDefaults) {
            setBool(value, key)
            synchronize()
        }
    }

    override fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean = getBooleanOrNull(key) ?: defaultValue

    override fun getBooleanOrNull(key: String): Boolean? =
        userDefaults.takeIf { it.valueForKey(key) != null }?.boolForKey(key)
}