plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31" apply false
}

subprojects {
    group = "dev.icerock.tools"
    version = "0.2.0"

    apply(plugin = "org.jetbrains.kotlin.jvm")
}
