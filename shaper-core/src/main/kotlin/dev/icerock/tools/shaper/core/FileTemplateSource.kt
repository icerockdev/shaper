/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import com.github.jknack.handlebars.io.AbstractTemplateSource
import java.io.File
import java.nio.charset.Charset

class FileTemplateSource(private val file: File, private val filename: String? = null) : AbstractTemplateSource() {
    override fun content(charset: Charset?): String {
        return file.readText(charset ?: Charsets.UTF_8)
    }

    override fun filename(): String = filename ?: file.name

    override fun lastModified(): Long = 0L
}
