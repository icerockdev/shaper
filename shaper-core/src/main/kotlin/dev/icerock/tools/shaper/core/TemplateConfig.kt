/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

data class TemplateConfig(
    val globalParams: Map<String, Any>,
    val files: List<FileConfig>,
    val includes: List<String>,
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
}
