/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.tools.shaper.core

import java.io.File
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

class ShaperTest {
    @Test
    fun `generation of gradle module`() {
        val buildGradleFile = TemplateConfig.FileConfig(
            pathTemplate = "build.gradle.kts",
            contentTemplateName = "build.gradle.kts.hbs",
            templateParams = mapOf("dependencies" to listOf("dep1", "dep2"))
        )
        val sourceCodeFile = TemplateConfig.FileConfig(
            pathTemplate = "src/main/kotlin/{{dts packageName}}/Test.kt",
            contentTemplateName = "Test.kt.hbs",
            templateParams = mapOf("packageName" to "dev.icerock.test")
        )
        val config = TemplateConfig(
            globalParams = mapOf("packageName" to "dev.icerock"),
            files = listOf(buildGradleFile, sourceCodeFile),
            includes = listOf("includes", "second-includes"),
            outputs = emptyList()
        )
        val shaper = Shaper(templateConfig = config)

        shaper.execute("build/test")

        assertFileEquals(
            expectedResourceName = "build.gradle.kts",
            actualFilePath = "build/test/build.gradle.kts"
        )
        assertFileEquals(
            expectedResourceName = "Test.kt",
            actualFilePath = "build/test/src/main/kotlin/dev/icerock/test/Test.kt"
        )
    }

    private fun assertFileEquals(expectedResourceName: String, actualFilePath: String) {
        val expectedUrl: URL = this::class.java.classLoader.getResource(expectedResourceName)
        val actualFile = File(actualFilePath)

        assertEquals(
            expected = expectedUrl.readText(),
            actual = actualFile.readText()
        )
    }
}
