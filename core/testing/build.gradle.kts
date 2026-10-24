plugins {
    alias(libs.plugins.custom.android.library)
}

dependencies {
    api(projects.data)
    api(projects.core.navigation)
    api(libs.bundles.unit.test)
}
