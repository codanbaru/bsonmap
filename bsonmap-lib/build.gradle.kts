val serializationCoreVersion: String = "1.7.3"
val bsonVersion: String = "5.2.1"

plugins {
    kotlin("jvm")

    id("maven-publish")
    id("tech.yanand.maven-central-publish") version "1.3.0"

    signing
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationCoreVersion")
    implementation("org.mongodb:bson:$bsonVersion")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("MavenJava") {
            groupId = "com.codanbaru.kotlin"
            artifactId = "bsonmap"
            version = project.findProperty("lib.version") as String? ?: "0"

            from(components["java"])

            pom {
                name = "BSONMap"
                description = "Library to serialize and deserialize documents from BSON using kotlinx.serialization."
                url = "https://github.com/codanbaru/bsonmap"

                licenses {
                    license {
                        name = "GPL-v3.0"
                        url = "http://www.gnu.org/licenses/gpl-3.0.txt"
                    }
                }

                developers {
                    developer {
                        id = "diegofer"
                        name = "Diego Fernandez"
                        email = "diego@diegofer.com"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/codanbaru/bsonmap.git"
                    developerConnection = "scm:git:ssh://github.com:codanbaru/bsonmap.git"
                    url = "https://github.com/codanbaru/bsonmap"
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["MavenJava"])
}

mavenCentral {
    authToken = project.findProperty("sonartype.central.token") as String? ?: ""

    publishingType = "USER_MANAGED"

    maxWait = 120
}
