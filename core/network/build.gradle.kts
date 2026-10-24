plugins {
    alias(libs.plugins.custom.android.library)
    alias(libs.plugins.custom.hilt)
}

android {
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.ktor.client.core)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlinx.serialization.json)
}
