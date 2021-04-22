/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class YamlReaderTest {
    @Test
    fun `read yaml with files`() {
        val result = YamlConfigReader.read(File("src/test/resources/config.yaml"))
        val array = result.globalParams["androidMainDeps"] as ArrayList<String>
        val files = result.files
        val includes = result.includes
        val outputs = result.outputs

        assertEquals("dev.icerock.shaper.sample.kmm.auth", result.globalParams["packageName"])
        assertEquals("Auth", result.globalParams["moduleName"])
        assertEquals("lifecycle", array[0])
        assertEquals("recyclerView", array[1])
        assertEquals("build.gradle.kts", files[0].pathTemplate)
        assertEquals(
            "/Users/mac2/proj/shaper/shaper-core/src/test/resources/kmm-module/build.gradle.kts.hbs",
            files[0].contentTemplateName
        )
        assertEquals(0, files[0].templateParams.count())
        assertEquals("src/commonMain/kotlin/{{dts packageName}}/di/{{moduleName}}Factory.kt", files[1].pathTemplate)
        assertEquals(
            "/Users/mac2/proj/shaper/shaper-core/src/test/resources/kmm-module/Factory.kt.hbs",
            files[1].contentTemplateName
        )
        assertEquals(0, files[1].templateParams.count())
        assertEquals("/Users/mac2/proj/shaper/shaper-core/src/test/resources/includes", includes[0])
        assertEquals("=== Tips for feature setup ===", outputs[0].outputTitle)
        assertEquals(
            "/Users/mac2/proj/shaper/shaper-core/src/test/resources/kmm-module/console.output.hbs",
            outputs[0].contentTemplateName
        )
        assertEquals(0, outputs[0].templateParams.count())
    }

    @Test
    fun `read yaml with one directory`() {
        val result = YamlConfigReader.read(File("src/test/resources/configWithoutFiles.yaml"))
        val array = result.globalParams["androidMainDeps"] as ArrayList<String>
        val files = result.files
        val includes = result.includes
        val outputs = result.outputs

        assertEquals("dev.icerock.shaper.sample.kmm.auth", result.globalParams["packageName"])
        assertEquals("Auth", result.globalParams["moduleName"])
        assertEquals("lifecycle", array[0])
        assertEquals("recyclerView", array[1])
        assertEquals("yaml-test1", files[0].pathTemplate)
        assertEquals(
            "/Users/mac2/proj/shaper/shaper-core/src/test/resources/files/yaml-test1.hbs",
            files[0].contentTemplateName
        )
        assertEquals(0, files[0].templateParams.count())
        assertEquals("yaml-test2", files[1].pathTemplate)
        assertEquals(
            "/Users/mac2/proj/shaper/shaper-core/src/test/resources/files/yaml-test2.hbs",
            files[1].contentTemplateName
        )
        assertEquals(0, files[1].templateParams.count())
        assertEquals("/Users/mac2/proj/shaper/shaper-core/src/test/resources/includes", includes[0])
        assertEquals("/Users/mac2/proj/shaper/shaper-core/src/test/resources/not-includes/sub-includes", includes[1])
        assertEquals("=== Tips for feature setup ===", outputs[0].outputTitle)
        assertEquals(
            "/Users/mac2/proj/shaper/shaper-core/src/test/resources/kmm-module/console.output.hbs",
            outputs[0].contentTemplateName
        )
        assertEquals(0, outputs[0].templateParams.count())
    }
}
