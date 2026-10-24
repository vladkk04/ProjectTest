plugins {
    alias(libs.plugins.custom.android.library)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)

    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(projects.core.testing)
    testImplementation(libs.ktor.client.mock)
}
