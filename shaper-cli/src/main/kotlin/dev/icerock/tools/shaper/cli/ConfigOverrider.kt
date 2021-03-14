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
            // TODO support int, double, boolean overriding
            else -> value
        }
    }

    private fun overrideString(path: String, value: String): String {
        println("Input $path value")
        println("Default: $value (Press enter to skip change)")
        val input = readLine() ?: return value
        val overridenValue = input.ifBlank { value }
        println("$path = $overridenValue")
        return overridenValue
    }

    // TODO maybe give user option to write own list?
    private fun overrideList(path: String, value: List<*>): List<*> {
        return value.mapIndexed { index, item ->
            overrideValue("$path:$index", item as Any)
        }
    }
}
