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

application {
    mainClass.set("dev.icerock.tools.shaper.cli.ShaperCliKt")
}

tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
}

// export PATH=~/.shaper/shaper-cli/bin:$PATH
tasks.create("install") {
    dependsOn(tasks.getByName("installDist"))
    group = "distribution"

    doFirst {
        copy {
            val userHome = System.getProperty("user.home")
            from(file("$buildDir/install"))
            into(file("$userHome/.shaper"))
        }
    }
}
