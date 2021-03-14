/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.cli

class ConfigOverrider {
    fun override(input: Map<String, Any>): Map<String, Any> {
        return overrideMap(input)
    }

    private fun overrideMap(map: Map<String, Any>, path: String? = null): Map<String, Any> {
        val prefix = path?.plus(":") ?: ""
        return map.mapValues {
            overrideValue(prefix + it.key, it.value)
        }
    }

    private fun overrideValue(path: String, value: Any): Any {
        return when (value) {
            is String -> overrideString(path, value)
            is List<*> -> overrideList(path, value)
            is Map<*, *> -> overrideMap(map = value as Map<String, Any>, path = path)
            is Int -> overrideInt(path, value)
            is Double -> overrideDouble(path, value)
            is Boolean -> overrideBoolean(path, value)
            else -> value
        }
    }

    private fun overrideString(path: String, value: String): String {
        return overridePrimitive(path, value) { it }
    }

    private fun overrideBoolean(path: String, value: Boolean): Boolean {
        return overridePrimitive(path, value) { it == "true" }
    }

    private fun overrideInt(path: String, value: Int): Int {
        return overridePrimitive(path, value) { it.toInt() }
    }

    private fun overrideDouble(path: String, value: Double): Double {
        return overridePrimitive(path, value) { it.toDouble() }
    }

    private fun <T> overridePrimitive(path: String, value: T, mapper: (String) -> T): T {
        println("Input $path value")
        println("Default: $value (Press enter to skip change)")
        val input = readLine() ?: return value
        val newValue = if (input.isBlank()) value else mapper(input)
        println("$path = $newValue")
        return newValue
    }

    // TODO #11 maybe give user option to write own list?
    private fun overrideList(path: String, value: List<*>): List<*> {
        return value.mapIndexed { index, item ->
            overrideValue("$path:$index", item as Any)
        }
    }
}
