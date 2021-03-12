/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import java.io.File

data class Config(
    val globalParams: Map<String, Any>,
    val files: List<FileConfig>,
    val outputs: List<OutputConfig>
) {
    data class FileConfig(
        val pathTemplate: String,
        val contentTemplateName: String,
        val templateParams: Map<String, Any>
    )

    data class OutputConfig(
        val outputTitle: String,
        val contentTemplateName: String,
        val templateParams: Map<String, Any>
    )

    companion object {
        operator fun invoke(
            globalParams: Map<String, Any>,
            directory: String,
            outputs: List<OutputConfig>
        ): Config {
            val filesDir = File(directory)
            val rootPrefix = filesDir.path + "/"

            val files = filesDir.walkTopDown().filterNot { it.isDirectory }.map {
                FileConfig(
                    pathTemplate = it.path.removeSuffix(".hbs").removePrefix(rootPrefix),
                    contentTemplateName = it.path,
                    templateParams = emptyMap()
                )
            }.toList()

            return Config(
                globalParams = globalParams,
                files = files,
                outputs = outputs
            )
        }
    }
}
