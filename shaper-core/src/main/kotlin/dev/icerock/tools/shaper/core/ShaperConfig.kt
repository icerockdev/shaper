/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import org.yaml.snakeyaml.Yaml
import java.io.File

data class ShaperConfig(
    val templateRepositories: List<File>
) {
    companion object {
        fun read(): ShaperConfig? {
            val userHome = System.getProperty("user.home")
            val globalConfigFile = File(userHome, ".shaper/config.yaml")
            if (globalConfigFile.exists().not()) return null

            val config: Map<String, Any> = globalConfigFile.inputStream().use { stream ->
                val yaml = Yaml()
                yaml.load(stream)
            } ?: throw IllegalArgumentException("configuration can't be read at $globalConfigFile")

            val repositories = config["repositories"] as? List<String>

            return ShaperConfig(
                templateRepositories = repositories?.map { File(it) }.orEmpty()
            )
        }
    }
}
