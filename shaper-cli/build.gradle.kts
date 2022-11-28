/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.1")
    /** github.jknack:handlebars uses a slf4j-api package which is working only with slf4j-simple
     * if github.jknack:handlebars version updated, slf4j-simple needs up version too **/
    implementation("org.slf4j:slf4j-simple:1.7.25")
    implementation(project(":shaper-core"))
}

application {
    mainClass.set("dev.icerock.tools.shaper.cli.ShaperCliKt")
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
