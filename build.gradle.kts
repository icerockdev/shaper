plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31" apply false
}

subprojects {
    group = "dev.icerock.tools"
    version = "0.1.0-SNAPSHOT"

    apply(plugin = "org.jetbrains.kotlin.jvm")
}
