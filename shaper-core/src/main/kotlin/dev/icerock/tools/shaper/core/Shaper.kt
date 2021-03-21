/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import java.io.File
import java.io.FileWriter
import java.io.StringWriter

class Shaper(private val templateConfig: TemplateConfig) {

    fun execute(outputPath: String): String {
        val handlebars = HandlebarsFactory.create()

        templateConfig.files.forEach { fileConfig ->
            val allParams = templateConfig.globalParams + fileConfig.templateParams

            val fileNameTemplate = handlebars.compileInline(fileConfig.pathTemplate)
            val filePath = fileNameTemplate.apply(allParams)

            val file = File(outputPath, filePath)
            with(file.parentFile) {
                if (!exists()) mkdirs()
            }

            if (fileConfig.contentTemplateName.endsWith(".hbs")) {
                val templateSource = TemplateSourceFactory.create(fileConfig.contentTemplateName)
                val contentTemplate = handlebars.compile(templateSource)
                FileWriter(file).use { fileWriter ->
                    contentTemplate.apply(allParams, fileWriter)
                }
            } else {
                val source = File(fileConfig.contentTemplateName)
                source.copyTo(file, overwrite = true)
            }
        }
        val resultWriter = StringWriter()
        templateConfig.outputs.forEach { outputConfig ->
            resultWriter.write(outputConfig.outputTitle)
            resultWriter.appendLine()
            val allParams = templateConfig.globalParams + outputConfig.templateParams
            val templateSource = TemplateSourceFactory.create(outputConfig.contentTemplateName)
            val contentTemplate = handlebars.compile(templateSource)

            contentTemplate.apply(allParams, resultWriter)
            resultWriter.appendLine()
        }
        return resultWriter.toString()
    }
}
