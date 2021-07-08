/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import java.util.Base64

plugins {
    id("org.gradle.maven-publish")
    id("signing")
}

publishing {
    repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "OSSRH"

        credentials {
            username = System.getenv("OSSRH_USER")
            password = System.getenv("OSSRH_KEY")
        }
    }

    publications.withType<MavenPublication> {
        // Provide artifacts information requited by Maven Central
        pom {
            name.set("Shaper")
            description.set("File structure generation tool from templates on Handlebars")
            url.set("https://github.com/icerockdev/shaper")
            licenses {
                license {
                    name.set("Apache-2.0")
                    distribution.set("repo")
                    url.set("https://github.com/icerockdev/shaper/blob/master/LICENSE.md")
                }
            }

            developers {
                developer {
                    id.set("Alex009")
                    name.set("Aleksey Mikhailov")
                    email.set("aleksey.mikhailov@icerockdev.com")
                }
                developer {
                    id.set("oyakovlev")
                    name.set("Oleg Yakovlev")
                    email.set("oyakovlev@icerockdev.com")
                }
                developer {
                    id.set("ATchernov")
                    name.set("Andrey Tchernov")
                    email.set("tchernov@icerockdev.com")
                }
                developer {
                    id.set("RezMike")
                    name.set("Mikhail Reznichenko")
                    email.set("mreznichenko@icerockdev.com")
                }
            }

            scm {
                connection.set("scm:git:ssh://github.com/icerockdev/shaper.git")
                developerConnection.set("scm:git:ssh://github.com/icerockdev/shaper.git")
                url.set("https://github.com/icerockdev/shaper")
            }
        }
    }
}


signing {
    val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
        String(Base64.getDecoder().decode(base64Key))
    }
    if (signingKeyId != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}
