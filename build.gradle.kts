import java.util.Locale;

plugins {
    alias(libs.plugins.kotlin.jvm)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(libs.tuprolog.ide)
    implementation(libs.tuprolog.repl)
}

listOf("gui", "repl").forEach {
    task<JavaExec>("runTuprolog${it.capitalized()}") {
        group = "tuprolog"
        mainClass.set("it.unibo.tuprolog.ui.$it.Main")
        sourceSets.main { classpath = runtimeClasspath }
        standardInput = System.`in`
    }
}

fun String.capitalized(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
