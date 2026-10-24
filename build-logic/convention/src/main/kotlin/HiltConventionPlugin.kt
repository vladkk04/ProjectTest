import com.klymiuk.scratchcard.convention.implementationLib
import com.klymiuk.scratchcard.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                dependencies.add("ksp", libs.findLibrary("hilt-compiler").get())

                withPlugin("org.jetbrains.kotlin.jvm") {
                    implementationLib("hilt-core")
                }

                withPlugin("com.android.base") {
                    apply("com.google.dagger.hilt.android")
                    implementationLib("hilt-android")
                }
            }
        }
    }
}
