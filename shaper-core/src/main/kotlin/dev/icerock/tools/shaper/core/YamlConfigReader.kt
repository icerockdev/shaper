/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import org.yaml.snakeyaml.Yaml
import java.io.File

object YamlConfigReader {

    fun read(file: File): Config {
        val map = readYaml(file)
        return buildConfig(map = map, directory = file.parentFile)
    }

    private fun readYaml(file: File): Map<String, Any> {
        return file.inputStream().use { stream ->
            val yaml = Yaml()
            yaml.load(stream)
        } ?: throw IllegalArgumentException("configuration is empty at $file")
    }

    private fun buildConfig(map: Map<String, Any>, directory: File): Config {
        val globalParams = map["globalParams"] as? Map<String, Any>
        val files = map["files"]

        val filesList = files as? List<Map<String, Any>>
        val filesDirectory = files as? String

        val filesConfigs: List<Config.FileConfig> = if (filesList != null) {
            filesList.map { fileMap ->
                val templateFile = File(directory, fileMap["contentTemplateName"] as String)
                Config.FileConfig(
                    pathTemplate = fileMap["pathTemplate"] as String,
                    contentTemplateName = templateFile.path,
                    templateParams = (fileMap["templateParams"] as? Map<String, Any>).orEmpty()
                )
            }
        } else if (filesDirectory != null) {
            val filesDir = File(filesDirectory)
            val rootPrefix = filesDir.path + "/"

            filesDir.walkTopDown().filterNot { it.isDirectory }.map {
                Config.FileConfig(
                    pathTemplate = it.path.removeSuffix(".hbs").removePrefix(rootPrefix),
                    contentTemplateName = it.path,
                    templateParams = emptyMap()
                )
            }.toList()
        } else emptyList()

        val outputs = map["outputs"] as? List<Map<String, Any>>
        val outputsConfigs: List<Config.OutputConfig> = outputs?.map { fileMap ->
            val templateFile = File(directory, fileMap["contentTemplateName"] as String)
            Config.OutputConfig(
                outputTitle = fileMap["title"] as String,
                contentTemplateName = templateFile.path,
                templateParams = (fileMap["templateParams"] as? Map<String, Any>).orEmpty()
            )
        }.orEmpty()

        return Config(
            globalParams = globalParams.orEmpty(),
            files = filesConfigs.orEmpty(),
            outputs = outputsConfigs
        )
    }
}
