/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.cli

import dev.icerock.tools.shaper.core.Shaper
import dev.icerock.tools.shaper.core.ShaperConfig
import dev.icerock.tools.shaper.core.TemplatesRepository
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

fun main(args: Array<String>) {
    val parser = ArgParser("shaper")

    val input: String by parser.option(
        type = ArgType.String,
        shortName = "i",
        description = "input configuration file",
    ).default("shaper.yaml")

    val output: String by parser.option(
        type = ArgType.String,
        shortName = "o",
        description = "output files directory",
    ).default(".")

    parser.parse(args)

    val shaperConfig = ShaperConfig.read()
    val templatesRepository = TemplatesRepository(shaperConfig)
    val config = templatesRepository.getTemplateConfig(input)

    val overridenConfig = config.copy(
        globalParams = overrideMap(config.globalParams)
    )

    val shaper = Shaper(templateConfig = overridenConfig)
    val consoleResult = shaper.execute(output)
    println(consoleResult)
}

private fun overrideMap(map: Map<String, Any>, path: String? = null): Map<String, Any> {
    val prefix = path?.plus(":") ?: ""
    return map.mapValues {
        overrideValue(prefix + it.key, it.value)
    }
}

private fun overrideValue(path: String, value: Any): Any {
    return when(value) {
        is String -> overrideString(path, value)
        is List<*> -> overrideList(path, value)
        is Map<*, *> -> overrideMap(map = value as Map<String, Any>, path = path)
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

private fun overrideList(path: String, value: List<*>): List<*> {
    return value.mapIndexed { index, item ->
        overrideValue("$path:$index", item as Any)
    }
}
