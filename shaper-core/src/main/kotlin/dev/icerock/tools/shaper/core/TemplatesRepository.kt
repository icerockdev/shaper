/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.io.TemplateSource
import com.github.jknack.handlebars.io.URLTemplateSource
import java.io.File

class TemplatesRepository {

    fun getTemplateSource(templateName: String): TemplateSource {
        val templateFile = File(templateName)
        return if (templateFile.exists()) {
            FileTemplateSource(templateFile)
        } else {
            URLTemplateSource(templateName, this::class.java.classLoader.getResource(templateName))
        }
    }
}
