/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache

object HandlebarsFactory {
    fun create(): Handlebars {
        val handlebars = Handlebars().with(ConcurrentMapTemplateCache())

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
        handlebars.registerHelper("incl", Helper<Boolean> { include, _ ->
            if (include) "" else Shaper.NOT_INCLUDE
        })

        handlebars.registerHelper("or", Helper<Boolean> { context, options ->
            context || options.params[0] as Boolean
        })

        handlebars.registerHelper("and", Helper<Boolean> { context, options ->
            context && options.params[0] as Boolean
        })

        handlebars.registerHelper("raw", Helper<Map<String, String>> { context, options ->
            options.fn()
        })

        return handlebars
    }
}
