/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object HandlebarsFactory {
    private const val TIME_MILLIS_MULTIPLIER = 1000
    fun create(): Handlebars {
        val handlebars = Handlebars().with(ConcurrentMapTemplateCache())

        handlebars.registerHelper("incl", Helper<Boolean> { include, _ ->
            if (include) "" else Shaper.NOT_INCLUDE
        })

        handlebars.registerHelper("raw", Helper<Map<String, String>> { _, options ->
            options.fn()
        })

        handlebars.registerHelper("renderPartial", Helper<String> { context, options ->
            handlebars.compile(context).apply(options.context.combine(options.hash))
        })

        registerConditionHelpers(handlebars)
        registerStringHelpers(handlebars)
        registerArrayHelpers(handlebars)
        registerDateTimeHelpers(handlebars)

        return handlebars
    }

    private fun registerConditionHelpers(handlebars: Handlebars) {
        handlebars.registerHelper("eq", Helper<String> { context, options ->
            context == options.params[0]
        })

        handlebars.registerHelper("or", Helper<Boolean> { context, options ->
            context || options.params[0] as Boolean
        })

        handlebars.registerHelper("and", Helper<Boolean> { context, options ->
            context && options.params[0] as Boolean
        })
    }

    private fun registerStringHelpers(handlebars: Handlebars) {
        handlebars.registerHelper("dts", Helper<String> { context, _ ->
            context.replace('.', '/')
        })
        handlebars.registerHelper("lcs", Helper<String> { context, _ ->
            context.lowercase()
        })
        handlebars.registerHelper("cap", Helper<String> { context, _ ->
            context.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        })
        handlebars.registerHelper("ucs", Helper<String> { context, _ ->
            context.uppercase()
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
        handlebars.registerHelper("ctwcap", Helper<String> { context, _ ->
            context.snakeToUpperCamelCase().camelToCapitalizeWithWhitespace()
        })
    }

    private fun registerArrayHelpers(handlebars: Handlebars) {
        handlebars.registerHelper("filterByAllOf", Helper<ArrayList<Map<Any, Any>>> { context, options ->
            if (options.hash.isEmpty()) {
                return@Helper arrayListOf<Map<Any, Any>>()
            }

            val keyValueMap = options.hash.toMap()
            context.filter { map: Map<Any, Any> ->
                keyValueMap.filter { entry: Map.Entry<String, Any> ->
                    map.containsKey(entry.key) && map[entry.key] == entry.value
                }.size == keyValueMap.size
            }
        })

        handlebars.registerHelper("filterByOneOf", Helper<ArrayList<Map<Any, Any>>> { context, options ->
            if (options.hash.isEmpty()) {
                return@Helper arrayListOf<Map<Any, Any>>()
            }

            val keyValueMap = options.hash.toMap()
            context.filter { map: Map<Any, Any> ->
                keyValueMap.filter { entry: Map.Entry<String, Any> ->
                    map.containsKey(entry.key) && map[entry.key] == entry.value
                }.isNotEmpty()
            }
        })

        handlebars.registerHelper("containsAllOf", Helper<ArrayList<Map<Any, Any>>> { context, options ->
            if (options.hash.isEmpty()) {
                return@Helper false
            }

            val keyValueMap = options.hash.toMap()
            context.any { map: Map<Any, Any> ->
                keyValueMap.filter { entry: Map.Entry<String, Any> ->
                    map.containsKey(entry.key) && map[entry.key] == entry.value
                }.size == keyValueMap.size
            }
        })

        handlebars.registerHelper("containsOneOf", Helper<ArrayList<Map<Any, Any>>> { context, options ->
            if (options.hash.isEmpty()) {
                return@Helper false
            }

            val keyValueMap = options.hash.toMap()
            context.any { map: Map<Any, Any> ->
                keyValueMap.filter { entry: Map.Entry<String, Any> ->
                    map.containsKey(entry.key) && map[entry.key] == entry.value
                }.isNotEmpty()
            }
        })

        handlebars.registerHelper("containsKey", Helper<ArrayList<Map<Any, Any>>> { context, options ->
            context.any { map: Map<Any, Any> ->
                map.keys.contains(options.params[0])
            }
        })

        handlebars.registerHelper("containsValue", Helper<ArrayList<Map<Any, Any>>> { context, options ->
            context.any { map: Map<Any, Any> ->
                map.values.contains(options.params[0])
            }
        })
    }

    private fun registerDateTimeHelpers(handlebars: Handlebars) {
        handlebars.registerHelper("currentTimestamp", Helper<Any> { _, _ ->
            System.currentTimeMillis() / TIME_MILLIS_MULTIPLIER
        })

        handlebars.registerHelper("currentDateTime", Helper<String> { context, _ ->
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(context))
        })
    }
}
