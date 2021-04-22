/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import org.yaml.snakeyaml.Yaml
import java.io.File

object YamlConfigReader {

    fun read(file: File): TemplateConfig {
        val absoluteFile = file.absoluteFile
        val map = readYaml(absoluteFile)
        return buildConfig(map = map, directory = absoluteFile.parentFile)
    }

    private fun readYaml(file: File): Map<String, Any> {
        return file.inputStream().use { stream ->
            val yaml = Yaml()
            yaml.load(stream)
        } ?: throw IllegalArgumentException("configuration is empty at $file")
    }

    private fun buildConfig(map: Map<String, Any>, directory: File): TemplateConfig {
        val globalParams = map["globalParams"] as? Map<String, Any>
        val files = map["files"]
        val includes = map["includes"]

        val filesConfigs: List<TemplateConfig.FileConfig> = collectFileConfig(files, directory)
        val outputsConfigs: List<TemplateConfig.OutputConfig> = collectOutputConfig(
            map["outputs"] as? List<Map<String, Any>>,
            directory
        )
        val includesConfigs = collectIncludeConfig(includes as List<String>, directory)

        return TemplateConfig(
            globalParams = globalParams.orEmpty(),
            files = filesConfigs,
            includes = includesConfigs,
            outputs = outputsConfigs
        )
    }

    private fun collectIncludeConfig(includes: List<String>?, directory: File) =
        includes?.map { File(directory, it).absolutePath }.orEmpty()

    private fun collectOutputConfig(
        outputs: List<Map<String, Any>>?,
        directory: File
    ): List<TemplateConfig.OutputConfig> {
        return outputs?.map { fileMap ->
            val templateFile = File(directory, fileMap["contentTemplateName"] as String)
            TemplateConfig.OutputConfig(
                outputTitle = fileMap["title"] as String,
                contentTemplateName = templateFile.path,
                templateParams = (fileMap["templateParams"] as? Map<String, Any>).orEmpty()
            )
        }.orEmpty()
    }

    private fun collectFileConfig(files: Any?, directory: File): List<TemplateConfig.FileConfig> {
        val filesList = files as? List<Map<String, Any>>
        val filesDirectory = files as? String
        return filesList?.map { fileMap ->
            val templateFile = File(directory, fileMap["contentTemplateName"] as String)
            TemplateConfig.FileConfig(
                pathTemplate = fileMap["pathTemplate"] as String,
                contentTemplateName = templateFile.path,
                templateParams = (fileMap["templateParams"] as? Map<String, Any>).orEmpty()
            )
        }
            ?: if (filesDirectory != null) {
                val filesDir = File(directory, filesDirectory)
                val rootPrefix = filesDir.path + "/"

                filesDir.walkTopDown().filterNot { it.isDirectory }.map {
                    TemplateConfig.FileConfig(
                        pathTemplate = it.path.removeSuffix(".hbs").removePrefix(rootPrefix),
                        contentTemplateName = it.path,
                        templateParams = emptyMap()
                    )
                }.toList()
            } else emptyList()
    }
}
