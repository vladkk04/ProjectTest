plugins {
    alias(libs.plugins.custom.android.library.compose)
    alias(libs.plugins.custom.hilt)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.scratch)
    implementation(projects.feature.activation)
    implementation(projects.core.navigation)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.serialization.json)
}
