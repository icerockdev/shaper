rootProject.name = "shaper"

dependencyResolutionManagement {
    repositories {
        mavenCentral()

        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    }
}

include("shaper-core")
include("shaper-cli")