plugins {
    id("org.asciidoctor.convert") version Versions.asciiDoctorConvertPlugin
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
            println("=====start asciidoctor")
            delete { file(snippetsDir) }
        }

        doLast {
            println("asciidoctor is deleted!")
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
    }
}
