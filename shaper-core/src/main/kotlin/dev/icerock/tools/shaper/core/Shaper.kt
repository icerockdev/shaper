/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.io.FileTemplateLoader
import com.github.jknack.handlebars.io.URLTemplateLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.StringWriter

class Shaper(private val templateConfig: TemplateConfig) {

    fun execute(outputPath: String): String {
        val handlebars = HandlebarsFactory.create()

        val list = mutableListOf<URLTemplateLoader>(TemplateClassPathLoader("/", ""))
        templateConfig.includes.forEach { includeDir ->
            val file = File(includeDir)

            if (file.exists()) {
                file.walkTopDown().filter { it.isDirectory }.forEach {
                    list.add(FileTemplateLoader(it.absolutePath, ".hbs"))
                }
            } else {
                File(
                    this::class.java.classLoader.getResource("$includeDir/")
                        ?.file ?: throw FileNotFoundException(includeDir)
                ).walkTopDown().filter { it.isDirectory }.forEach {
                    list.add(FileTemplateLoader(it.absolutePath, ".hbs"))
                }
            }
        }

        handlebars.with(*list.toTypedArray())

        templateConfig.files.forEach { fileConfig ->
            val allParams = templateConfig.globalParams + fileConfig.templateParams

            val fileNameTemplate = handlebars.compileInline(fileConfig.pathTemplate)
            val filePath = fileNameTemplate.apply(allParams)

            val file = File(outputPath, filePath)
            with(file.parentFile) {
                if (!exists()) mkdirs()
            }

            if (fileConfig.contentTemplateName.endsWith(".hbs")) {
                val contentTemplate = handlebars.compile(fileConfig.contentTemplateName)
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
            val contentTemplate = handlebars.compile(outputConfig.contentTemplateName)

            contentTemplate.apply(allParams, resultWriter)
            resultWriter.appendLine()
        }
        return resultWriter.toString()
    }
}
