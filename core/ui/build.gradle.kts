plugins {
    alias(libs.plugins.custom.android.library.compose)
}

dependencies {
    api(projects.domain)
    implementation(projects.core.designsystem)
}
