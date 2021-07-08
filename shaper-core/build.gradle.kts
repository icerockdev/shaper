/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("publication-convention")
    id("jvm-publication-convention")
}

dependencies {
    implementation("com.github.jknack:handlebars:4.2.0")
    implementation("org.yaml:snakeyaml:1.21")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.31")
}
