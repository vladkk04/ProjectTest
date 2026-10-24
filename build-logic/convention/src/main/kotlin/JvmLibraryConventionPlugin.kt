import com.klymiuk.scratchcard.convention.configureKotlinJvm
import com.klymiuk.scratchcard.convention.configureUnitTests
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            configureKotlinJvm()
            configureUnitTests()
        }
    }
}
