plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform(libs.kotlin.bom))
    // Use the Kotlin JDK 8 standard library.
    implementation(libs.kotlin.stdlib.jvm)

    implementation(libs.tuprolog.solve.classic)
    implementation(libs.tuprolog.parser.theory)

    // Use the Kotlin test library.
    testImplementation(libs.kotlin.test)
    // Use the Kotlin JUnit integration.
    testImplementation(libs.kotlin.test.junit)
}

tasks.register<JavaExec>("run") {
    dependsOn("assemble")
    group = "run"
    sourceSets {
        main {
            classpath = runtimeClasspath
        }
    }
    mainClass.set("it.unibo.ise.lab.strips.Main")
    standardInput = System.`in`

    val arguments = mutableListOf<String>().also {
        if (properties.containsKey("verbose") && properties["verbose"] == "true") {
            it.add("--verbose")
        }
        if (properties.containsKey("initialState")) {
            it.addAll(listOf("--initialState", "${properties["initialState"]}"))
        }
        if (properties.containsKey("goal")) {
            it.addAll(listOf("--goal", "${properties["goal"]}"))
        }
        if (properties.containsKey("world")) {
            it.addAll(listOf("--world", "${properties["world"]}"))
        }
        if (properties.containsKey("maxDepth")) {
            it.addAll(listOf("--maxDepth", "${properties["maxDepth"]}"))
        }
    }

    args = arguments

    doFirst {
        println("Running `${mainClass.get()}` with arguments `${arguments.joinToString(" ")}`")
    }
}
