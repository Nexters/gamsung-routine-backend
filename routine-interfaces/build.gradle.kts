plugins {
    id("org.asciidoctor.convert")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(project(":routine-domain"))
    asciidoctor("org.springframework.restdocs:spring-restdocs-ascildoctor")
}

val snippetsDir by extra { file("build/generated-snippets") }

tasks {
    test {
        useJUnitPlatform()
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        dependsOn(test)

        doFirst {
            delete { file(snippetsDir) }
        }
    }

    val copyHTML = register<Copy>("copyHTML") {
        dependsOn(asciidoctor)
        from("build/asciidoc/html5")
        into("src/main/resources/static/docs")
    }

    build {
        dependsOn(copyHTML)
    }

    bootJar {
        dependsOn(asciidoctor)
//        from ("${asciidoctor}/html5")
//        into("BOOT-INF/classes/static/docs")
        enabled = true
        archiveFileName.set("app.jar")
    }
}

jib {
    from {
        image = "amazoncorretto:11"
    }
    to {
        image = "gamsung-routine"
        tags = setOf("latest")
    }
    container {
        ports = listOf("8080")
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}
