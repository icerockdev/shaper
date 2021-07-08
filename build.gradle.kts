/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

buildscript {
    repositories {
        mavenCentral()
        google()

        gradlePluginPortal()
    }
    dependencies {
        classpath(":build-logic")
    }
}

subprojects {
    group = "dev.icerock.tools"
    version = "0.4.0"
}
