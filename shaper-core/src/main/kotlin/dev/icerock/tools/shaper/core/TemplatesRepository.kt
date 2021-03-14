/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import java.io.File

class TemplatesRepository(
    private val shaperConfig: ShaperConfig?
) {
    fun getTemplateConfig(templateName: String): TemplateConfig {
        val templateConfigFile = shaperConfig?.templateRepositories
            .orEmpty()
            .map { File(it, templateName) }
            .firstOrNull { it.exists() } ?: File(templateName)

        if (templateConfigFile.exists().not()) {
            throw IllegalArgumentException("configuration $templateName not found")
        }

        return YamlConfigReader.read(templateConfigFile)
    }
}
