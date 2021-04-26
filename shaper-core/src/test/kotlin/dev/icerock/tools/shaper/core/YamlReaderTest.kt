/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.assertThat
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class YamlReaderTest {
    @Test
    fun `read yaml with files`() {
        val result = YamlConfigReader.read(File("src/test/resources/config.yaml"))
        val array = result.globalParams["androidMainDeps"] as ArrayList<String>
        val files = result.files.sortedBy { it.pathTemplate }
        val includes = result.includes
        val outputs = result.outputs

        assertEquals("dev.icerock.shaper.sample.kmm.auth", result.globalParams["packageName"])
        assertEquals("Auth", result.globalParams["moduleName"])
        assertEquals("lifecycle", array[0])
        assertEquals("recyclerView", array[1])
        assertEquals("build.gradle.kts", files[0].pathTemplate)
        assertThat(
            files[0].contentTemplateName,
            containsString("shaper/shaper-core/src/test/resources/kmm-module/build.gradle.kts.hbs")
        )
        assertEquals(0, files[0].templateParams.count())
        assertEquals("src/commonMain/kotlin/{{dts packageName}}/di/{{moduleName}}Factory.kt", files[1].pathTemplate)
        assertThat(
            files[1].contentTemplateName,
            containsString("shaper/shaper-core/src/test/resources/kmm-module/Factory.kt.hbs")
        )
        assertEquals(0, files[1].templateParams.count())
        assertThat(
            includes[0],
            containsString("shaper/shaper-core/src/test/resources/includes")
        )
        assertEquals("=== Tips for feature setup ===", outputs[0].outputTitle)
        assertThat(
            outputs[0].contentTemplateName,
            containsString("shaper/shaper-core/src/test/resources/kmm-module/console.output.hbs")
        )
        assertEquals(0, outputs[0].templateParams.count())
    }

    @Test
    fun `read yaml with one directory`() {
        val result = YamlConfigReader.read(File("src/test/resources/configWithoutFiles.yaml"))
        val array = result.globalParams["androidMainDeps"] as ArrayList<String>
        val files = result.files.sortedBy { it.pathTemplate }
        val includes = result.includes
        val outputs = result.outputs

        assertEquals("dev.icerock.shaper.sample.kmm.auth", result.globalParams["packageName"])
        assertEquals("Auth", result.globalParams["moduleName"])
        assertEquals("lifecycle", array[0])
        assertEquals("recyclerView", array[1])
        assertEquals("1yaml-test", files[0].pathTemplate)
        assertThat(
            files[0].contentTemplateName,
            containsString("shaper/shaper-core/src/test/resources/files/1yaml-test.hbs")
        )
        assertEquals(0, files[0].templateParams.count())
        assertEquals("2yaml-test", files[1].pathTemplate)
        assertThat(
            files[1].contentTemplateName,
            containsString("shaper/shaper-core/src/test/resources/files/2yaml-test.hbs")
        )
        assertEquals(0, files[1].templateParams.count())
        assertThat(
            includes[0],
            containsString("shaper/shaper-core/src/test/resources/includes")
        )
        assertThat(
            includes[1],
            containsString("shaper/shaper-core/src/test/resources/not-includes/sub-includes")
        )
        assertEquals("=== Tips for feature setup ===", outputs[0].outputTitle)
        assertThat(
            outputs[0].contentTemplateName,
            containsString("shaper/shaper-core/src/test/resources/kmm-module/console.output.hbs")
        )
        assertEquals(0, outputs[0].templateParams.count())
    }
}
