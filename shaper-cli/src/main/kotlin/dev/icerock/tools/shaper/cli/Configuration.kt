/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.cli

import dev.icerock.tools.shaper.core.Config
import org.yaml.snakeyaml.Yaml
import java.io.File

class Configuration(private val map: Map<String, Any>) {

    fun buildShaperConfig(): Config {
        val globalParams = map["globalParams"] as? Map<String, Any>
        val files = map["files"]

        val filesList = files as? List<Map<String, Any>>
        val filesDirectory = files as? String

        val filesConfigs: List<Config.FileConfig>? = filesList?.map { fileMap ->
            Config.FileConfig(
                pathTemplate = fileMap["pathTemplate"] as String,
                contentTemplateName = fileMap["contentTemplateName"] as String,
                templateParams = (fileMap["templateParams"] as? Map<String, Any>).orEmpty()
            )
        }

        val outputs = map["outputs"] as? List<Map<String, Any>>
        val outputsConfigs: List<Config.OutputConfig> = outputs?.map { fileMap ->
            Config.OutputConfig(
                outputTitle = fileMap["title"] as String,
                contentTemplateName = fileMap["contentTemplateName"] as String,
                templateParams = (fileMap["templateParams"] as? Map<String, Any>).orEmpty()
            )
        }.orEmpty()

        return if (filesDirectory != null) {
            Config(
                globalParams = globalParams.orEmpty(),
                directory = filesDirectory,
                outputs = outputsConfigs
            )
        } else {
            Config(
                globalParams = globalParams.orEmpty(),
                files = filesConfigs.orEmpty(),
                outputs = outputsConfigs
            )
        }
    }

    companion object {
        fun read(filePath: String): Configuration {
            val file = File(filePath)
            if (file.exists().not()) {
                throw IllegalArgumentException("input configuration file not exist at path $filePath")
            }

            val map: Map<String, Any> = file.inputStream().use { stream ->
                val yaml = Yaml()
                yaml.load(stream)
            } ?: throw IllegalArgumentException("configuration is empty at $file")

            return Configuration(map)
        }
    }
}
