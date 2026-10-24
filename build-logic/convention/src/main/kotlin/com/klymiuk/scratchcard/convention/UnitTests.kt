package com.klymiuk.scratchcard.convention

import org.gradle.api.Project

internal fun Project.configureUnitTests() {
    testImplementationBundle("unit-test")
}
