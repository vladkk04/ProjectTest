package com.klymiuk.scratchcard.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        namespace = autoNamespaceModule()
        compileSdk = AppConfig.COMPILE_SDK

        defaultConfig.minSdk = AppConfig.MIN_SDK

        compileOptions.sourceCompatibility = AppConfig.JAVA_VERSION
        compileOptions.targetCompatibility = AppConfig.JAVA_VERSION

        testOptions.unitTests.isReturnDefaultValues = true
    }

    configureKotlin()
}
