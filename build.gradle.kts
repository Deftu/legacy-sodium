import dev.deftu.gradle.utils.includeOrShade

plugins {
    java
    kotlin("jvm") version("2.0.0")
    val dgtVersion = "2.2.3"
    id("dev.deftu.gradle.tools") version(dgtVersion)
    id("dev.deftu.gradle.tools.resources") version(dgtVersion)
    id("dev.deftu.gradle.tools.shadow") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.loom") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.releases") version(dgtVersion)
}

loom {
    accessWidenerPath = file("src/main/resources/sodium.accesswidener")
}

sourceSets {
    val main = getByName("main")
    val api = create("api")
    val desktop = create("desktop")

    api.apply {
        java {
            compileClasspath += main.compileClasspath
        }
    }

    desktop.apply {
        java {
            srcDir("src/desktop/java")
        }
    }

    main.apply {
        java {
            compileClasspath += api.output
            runtimeClasspath += api.output
        }
    }
}

dependencies {
    implementation(includeOrShade("com.lodborg:interval-tree:1.0.0")!!)
    implementation(includeOrShade("it.unimi.dsi:fastutil:8.5.6")!!)
}

tasks {

    jar {
        from("${rootProject.projectDir}/LICENSE.md")

        val api = sourceSets.getByName("api")
        from(api.output.classesDirs)
        from(api.output.resourcesDir)

        val desktop = sourceSets.getByName("desktop")
        from(desktop.output.classesDirs)
        from(desktop.output.resourcesDir)

        manifest.attributes["Main-Class"] = "net.caffeinemc.mods.sodium.desktop.LaunchWarn"
    }

}
