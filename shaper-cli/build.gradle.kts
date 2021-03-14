/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("application")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.1")
    implementation(project(":shaper-core"))
}

tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "dev.icerock.tools.shaper.cli.ShaperCliKt"
            )
        )
    }
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
}
