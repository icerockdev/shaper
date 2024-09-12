/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

internal fun String.camelToSnakeCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.lowercase()
}

internal fun String.camelToCapitalizeWithWhitespace(): String {
    return camelRegex.replace(this) { matchResult ->
        " " + matchResult.value.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }
}

internal fun String.snakeToLowerCamelCase(): String {
    return snakeRegex.replace(this) {
        it.value.replace("_", "")
            .uppercase()
    }.replaceFirstChar { it.lowercase() }
}

internal fun String.snakeToUpperCamelCase(): String {
    return this.snakeToLowerCamelCase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}
