plugins {
    kotlin("multiplatform") version "1.7.10"
}

group = "org.wordandahalf"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    // Defines targets for this application. Supported targets can be found at
    // https://kotlinlang.org/docs/native-overview.html#target-platforms.
    val nativeTarget = when (System.getProperty("os.name")) {
        "Linux" -> linuxX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            @Suppress("UNUSED_VARIABLE")
            cinterops {
                val antlr3c by creating {
                    // ANTLR3c (rather annoyingly) installs itself to /usr/local/include with no subdirectory.
                    includeDirs("/usr/local/include", "${System.getProperty("user.dir")}/grammar/include")
                }
                val llvm by creating
            }
        }

        binaries {
            executable {
                // Define the entrypoint for the generated application
                entryPoint = "main"
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        // Define the default source sets.
        val nativeMain by getting
        val nativeTest by getting
    }
}
