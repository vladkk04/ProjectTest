plugins {
    alias(libs.plugins.custom.android.application.compose)
    alias(libs.plugins.custom.hilt)
}

dependencies {
    implementation(projects.navigation)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(projects.data)
    implementation(projects.core.network)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
}
