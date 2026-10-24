import com.klymiuk.scratchcard.convention.implementation
import com.klymiuk.scratchcard.convention.implementationLib
import com.klymiuk.scratchcard.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("custom.android.library.compose")
                apply("custom.hilt")
            }

            implementation(project(":domain"))
            implementation(project(":core:common"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:navigation"))
            implementation(project(":core:ui"))
            testImplementation(project(":core:testing"))

            implementationLib("androidx-lifecycle-runtime-compose")
            implementationLib("androidx-lifecycle-viewmodel-compose")
            implementationLib("androidx-hilt-lifecycle-viewmodel-compose")
        }
    }
}
