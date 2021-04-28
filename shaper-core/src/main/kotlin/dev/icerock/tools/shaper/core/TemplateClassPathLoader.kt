/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.io.TemplateSource
import com.github.jknack.handlebars.io.URLTemplateLoader
import com.github.jknack.handlebars.io.URLTemplateSource
import java.io.File
import java.io.FileNotFoundException
import java.net.URL

class TemplateClassPathLoader() : URLTemplateLoader() {

    constructor(prefix: String, suffix: String) : this() {
        setPrefix(prefix)
        setSuffix(suffix)
    }

    override fun sourceAt(location: String?): TemplateSource? {
        if (location == null) throw Exception("The uri is required.")

        val templateFile = if (PathLocator.getBaseDir(location) == null) {
            File(location)
        } else {
            File(PathLocator.getAbsolutePath(location))
        }

        return if (templateFile.exists()) {
            FileTemplateSource(templateFile, location)
        } else {
            URLTemplateSource(location, getResource(location) ?: throw FileNotFoundException(location))
        }
    }

    override fun getResource(location: String?): URL? {
        return this::class.java.classLoader.getResource(location) ?: this::class.java.classLoader.getResource(
            resolve(
                normalize(location)
            )
        )
    }
}
