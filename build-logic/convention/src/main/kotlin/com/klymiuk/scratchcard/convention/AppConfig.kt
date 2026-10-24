package com.klymiuk.scratchcard.convention

import org.gradle.api.JavaVersion

object AppConfig {
    val JAVA_VERSION = JavaVersion.VERSION_17
    const val COMPILE_SDK = 37
    const val MIN_SDK = 26
    const val TARGET_SDK = 36
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"
    const val NAMESPACE: String = "com.klymiuk.scratchcard"
    const val APPLICATION_ID: String = "com.klymiuk.scratchcard"
}
