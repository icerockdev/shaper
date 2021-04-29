package sub

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
}
# Sub-file {{test}}
dependencies {
    implements("dep1")
}
