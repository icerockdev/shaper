/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("java")
    id("org.gradle.maven-publish")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing.publications.register("mavenJava", MavenPublication::class) {
    from(components["java"])
}
