package com.klymiuk.scratchcard.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.autoNamespaceModule(): String =
    AppConfig.NAMESPACE + project.path
        .replace(":", ".")
        .replace(Regex("-(.)")) { it.groupValues[1].uppercase() }

fun Project.implementation(dependencyNotation: Any) = dependencies.add("implementation", dependencyNotation)
fun Project.testImplementation(dependencyNotation: Any) = dependencies.add("testImplementation", dependencyNotation)
fun Project.debugImplementation(dependencyNotation: Any) = dependencies.add("debugImplementation", dependencyNotation)

fun Project.implementationLib(libName: String) = implementation(libs.findLibrary(libName).get())
fun Project.implementationBom(libName: String) = implementation(dependencies.platform(libs.findLibrary(libName).get()))
fun Project.debugImplementationLib(libName: String) = debugImplementation(libs.findLibrary(libName).get())
fun Project.testImplementationBundle(bundleName: String) = testImplementation(libs.findBundle(bundleName).get())
