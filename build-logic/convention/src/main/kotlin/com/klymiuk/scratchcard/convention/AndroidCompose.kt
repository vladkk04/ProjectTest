package com.klymiuk.scratchcard.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        buildFeatures.compose = true
    }

    implementationBom("androidx-compose-bom")
    implementationLib("androidx-compose-ui")
    implementationLib("androidx-compose-material3")
    implementationLib("androidx-compose-ui-tooling-preview")
    debugImplementationLib("androidx-compose-ui-tooling")
}
