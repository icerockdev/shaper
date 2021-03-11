/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.io.TemplateSource
import com.github.jknack.handlebars.io.URLTemplateSource
import java.io.File
import java.io.FileWriter
import java.io.StringWriter

class Shaper(private val config: Config) {

    fun execute(outputPath: String): String {
        val handlebars = Handlebars()
        handlebars.registerHelper("packagePath", Helper<String> { context, _ ->
            context.replace('.', '/')
        })

        //Use with module name
        handlebars.registerHelper("packageIncludeName", Helper<String> { context, _ ->
            context.toLowerCase()
        })

        config.files.forEach { fileConfig ->
            val allParams = config.globalParams + fileConfig.templateParams

            val fileNameTemplate = handlebars.compileInline(fileConfig.pathTemplate)
            val filePath = fileNameTemplate.apply(allParams)

            val file = File(outputPath, filePath)
            with(file.parentFile) {
                if (!exists()) mkdirs()
            }

            val templateSource = getTemplateSource(fileConfig.contentTemplateName)
            val contentTemplate = handlebars.compile(templateSource)
            FileWriter(file).use { fileWriter ->
                contentTemplate.apply(allParams, fileWriter)
            }
        }
        val resultWriter = StringWriter()
        config.outputs.forEach() { outputConfig ->
            resultWriter.write(outputConfig.outputTitle)
            resultWriter.appendLine()
            val allParams = config.globalParams + outputConfig.templateParams
            val templateSource = getTemplateSource(outputConfig.contentTemplateName)
            val contentTemplate = handlebars.compile(templateSource)

            contentTemplate.apply(allParams,resultWriter)
            resultWriter.appendLine()
        }
        return resultWriter.toString()
    }

    private fun getTemplateSource(templateName: String): TemplateSource {
        val templateFile = File(templateName)
        return if (templateFile.exists()) {
            FileTemplateSource(templateFile)
        } else {
            URLTemplateSource(templateName, this::class.java.classLoader.getResource(templateName))
        }
    }
}
