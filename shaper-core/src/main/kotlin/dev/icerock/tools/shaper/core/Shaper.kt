/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import java.io.File
import java.io.FileWriter
import java.io.StringWriter

class Shaper(private val config: Config) {

    fun execute(outputPath: String): String {
        val handlebars = HandlebarsFactory.create()
        val templatesRepository = TemplatesRepository()

        config.files.forEach { fileConfig ->
            val allParams = config.globalParams + fileConfig.templateParams

            val fileNameTemplate = handlebars.compileInline(fileConfig.pathTemplate)
            val filePath = fileNameTemplate.apply(allParams)

            val file = File(outputPath, filePath)
            with(file.parentFile) {
                if (!exists()) mkdirs()
            }

            val templateSource = templatesRepository.getTemplateSource(
                fileConfig.contentTemplateName
            )
            val contentTemplate = handlebars.compile(templateSource)
            FileWriter(file).use { fileWriter ->
                contentTemplate.apply(allParams, fileWriter)
            }
        }
        val resultWriter = StringWriter()
        config.outputs.forEach { outputConfig ->
            resultWriter.write(outputConfig.outputTitle)
            resultWriter.appendLine()
            val allParams = config.globalParams + outputConfig.templateParams
            val templateSource = templatesRepository.getTemplateSource(
                outputConfig.contentTemplateName
            )
            val contentTemplate = handlebars.compile(templateSource)

            contentTemplate.apply(allParams, resultWriter)
            resultWriter.appendLine()
        }
        return resultWriter.toString()
    }
}
