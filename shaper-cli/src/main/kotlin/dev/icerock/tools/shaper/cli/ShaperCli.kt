/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.cli

import dev.icerock.tools.shaper.core.Shaper
import dev.icerock.tools.shaper.core.ShaperConfig
import dev.icerock.tools.shaper.core.TemplatesRepository
import dev.icerock.tools.shaper.core.YamlConfigReader
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.io.File

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
    val shaper = Shaper(templateConfig = config)
    val consoleResult = shaper.execute(output)
    println(consoleResult)
}
