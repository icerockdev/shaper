/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.cli

import org.yaml.snakeyaml.Yaml
import java.io.File

class Configuration(private val map: Map<String, Any>) {
    val packageName: String get() = map["packageName"] as String

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
