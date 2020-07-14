plugins {
//    kotlin("multiplatform") version "1.3.71"
//    kotlin("plugin.serialization") version "1.3.71"
//    id("kotlin")
//    id("multiplatform")
    id("kotlin-multiplatform")
}

//repositories {
//    mavenCentral()
//    jcenter()
//}

kotlin {
//    macosX64("native") {
//        val main by compilations.getting
//
//        binaries {
//            sharedLib {
//                baseName = "native"
//            }
//        }
//    }
    mingwX64("native") {
        val main by compilations.getting

        binaries {
            sharedLib {
                baseName = "native"
            }
        }
    }
//    linuxX64("native") {
//        val main by compilations.getting
//
//        binaries {
//            sharedLib {
//                baseName = "native"
//            }
//        }
//    }

    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}


