import com.android.build.api.dsl.ApplicationExtension
import com.klymiuk.scratchcard.convention.AppConfig
import com.klymiuk.scratchcard.convention.configureKotlinAndroid
import com.klymiuk.scratchcard.convention.configureUnitTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)

                namespace = AppConfig.NAMESPACE

                defaultConfig {
                    applicationId = AppConfig.APPLICATION_ID
                    targetSdk = AppConfig.TARGET_SDK
                    versionCode = AppConfig.VERSION_CODE
                    versionName = AppConfig.VERSION_NAME
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }

            configureUnitTests()
        }
    }
}
