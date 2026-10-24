plugins {
    alias(libs.plugins.custom.android.library)
    alias(libs.plugins.custom.hilt)
}

dependencies {
    api(projects.core.common)
    implementation(projects.data)

    testImplementation(projects.core.testing)
}
