/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import java.io.File
import java.io.FileWriter

class Shaper(private val config: Config) {

    fun execute(output: String) {
        val handlebars = Handlebars()
        handlebars.registerHelper("packagePath", Helper<String> { context, _ ->
            context.replace('.', '/')
        })

        config.files.forEach { fileConfig ->
            val allParams = config.globalParams + fileConfig.templateParams

            val fileNameTemplate = handlebars.compileInline(fileConfig.pathTemplate)
            val filePath = fileNameTemplate.apply(allParams)

            val file = File(output, filePath)
            with(file.parentFile) {
                if (!exists()) mkdirs()
            }

            val contentTemplate = handlebars.compile(fileConfig.contentTemplateName)
            FileWriter(file).use { fileWriter ->
                contentTemplate.apply(allParams, fileWriter)
            }
        }
    }
}
