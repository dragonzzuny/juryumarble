plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Dependency Injection (for @Inject annotation)
    implementation("javax.inject:javax.inject:1")

    testImplementation(libs.junit)
}
