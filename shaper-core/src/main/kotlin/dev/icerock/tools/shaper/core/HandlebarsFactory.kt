/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.io.FileTemplateLoader
import java.io.File
import java.nio.file.Paths

object HandlebarsFactory {
    fun create(includePath: String = "_includes"): Handlebars {
        val partialDir = File("${Paths.get("").toAbsolutePath()}/$includePath")
        partialDir.mkdirs()
        val handlebars = Handlebars(FileTemplateLoader(partialDir, ".hbs"))

        handlebars.registerHelper("dts", Helper<String> { context, _ ->
            context.replace('.', '/')
        })
        handlebars.registerHelper("lcs", Helper<String> { context, _ ->
            context.toLowerCase()
        })
        handlebars.registerHelper("cap", Helper<String> { context, _ ->
            context.capitalize()
        })
        handlebars.registerHelper("ucs", Helper<String> { context, _ ->
            context.toUpperCase()
        })
        handlebars.registerHelper("cts", Helper<String> { context, _ ->
            context.camelToSnakeCase()
        })
        handlebars.registerHelper("stl", Helper<String> { context, _ ->
            context.snakeToLowerCamelCase()
        })
        handlebars.registerHelper("stu", Helper<String> { context, _ ->
            context.snakeToUpperCamelCase()
        })
        handlebars.registerHelper("eq", Helper<String> { context, options ->
            context == options.params[0]
        })

        registerPartials(partialDir, handlebars, includePath)

        return handlebars
    }

    private fun registerPartials(partialDir: File, handlebars: Handlebars, includePath: String) {
        partialDir.listFiles()?.forEach {
            if (it.isDirectory) {
                registerPartials(File(it.absoluteFile.toString()), handlebars, includePath)
            } else {
                handlebars.compile(
                    it.absoluteFile.toString().substringBeforeLast(".").substringAfterLast("$includePath/")
                )
            }
        }
    }
}
