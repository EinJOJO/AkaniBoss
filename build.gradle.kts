plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
}

group = "it.einjojo.akani"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "LiteCommands"
        url = uri("https://repo.panda-lang.org/releases")
    }
    maven {
        name = "packet-events"
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }

}

dependencies {
    implementation(libs.guava)
    implementation(libs.gson)
    compileOnly(libs.paper)
    compileOnly(libs.decentholograms)
}


tasks {
    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        minimize()
    }

    runServer {
        minecraftVersion("1.20.4")

    }
}