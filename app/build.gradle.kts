import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleGmsGoogleServices)
}

val app_version= "2.0.0"
val app_version_code=16

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
val secrets_envPropertiesFile = rootProject.file("secrets_env.properties")
// Initialize a new Properties() object called keystoreProperties.
val secrets_envProperties = Properties()
// Load your keystore.properties file into the keystoreProperties object.
secrets_envProperties.load(FileInputStream(secrets_envPropertiesFile))

val key_Alias = secrets_envProperties["keyAlias"] as String
val key_Password = secrets_envProperties["keyPassword"] as String
val storeKeyFile = file(secrets_envProperties["storeFile"] as String)
val store_Password = secrets_envProperties["storePassword"] as String



val myDemoProperty = findProperty("myProperty") ?: "Hello, world!"
//secrets_envProperties["admob_app_id"] as String
val admob_app_id = secrets_envProperties["admob_app_id"] as String
val admob_ad_banner_id_normal = secrets_envProperties["admob_ad_banner_id_normal"] as String
val admob_ad_banner_id_med_rect = secrets_envProperties["admob_ad_banner_id_med_rect"] as String
val admob_ad_banner_id_large_rect = secrets_envProperties["admob_ad_banner_id_large_rect"] as String
val admob_ad_interstitial_id = secrets_envProperties["admob_ad_interstitial_id"] as String



android {
    signingConfigs {
        create("release") {
            storeFile = storeKeyFile
            storePassword = store_Password
            keyAlias = key_Alias
            keyPassword = key_Password
        }
    }
    namespace = "devesh.medic.dose"
    compileSdk = 34

    defaultConfig {
        applicationId = "devesh.medic.dose"
        minSdk = 24
        targetSdk = 34
        versionCode = app_version_code
        versionName = app_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug{
            resValue("string", "app_version", "${app_version} DEBUG")
            resValue("string", "app_build_no", "${app_version_code}")

            resValue("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")
            resValue("string", "admob_ad_banner_id_normal", "ca-app-pub-3940256099942544/9214589741")
            resValue("string", "admob_ad_banner_id_med_rect", "ca-app-pub-3940256099942544/9214589741")
            resValue("string", "admob_ad_banner_id_large_rect", "ca-app-pub-3940256099942544/9214589741")
            resValue("string", "admob_ad_interstitial_id", "ca-app-pub-3940256099942544/1033173712")

            applicationIdSuffix= ".debug"
            versionNameSuffix= "-DEBUG"
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )


        }
        release {
            resValue("string", "app_version", "${app_version}")
            resValue("string", "app_build_no", "${app_version_code}")

            resValue("string", "admob_app_id", "${admob_app_id}")
            resValue("string", "admob_ad_banner_id_normal", "${admob_ad_banner_id_normal}")
            resValue("string", "admob_ad_banner_id_med_rect", "${admob_ad_banner_id_med_rect}")
            resValue("string", "admob_ad_banner_id_large_rect", "${admob_ad_banner_id_large_rect}")

            resValue("string", "admob_ad_interstitial_id", "${admob_ad_interstitial_id}")

            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    flavorDimensions += listOf("appstore")
    productFlavors {
        create("free") {
            dimension = "appstore"
            resValue("string", "app_flavour", "free")

        }
        create("pro") {
            dimension = "appstore"
           // versionNameSuffix = "Pro"
            versionName= "${app_version} Pro"

            applicationId = "devesh.medic.plus"
            resValue("string", "app_flavour", "pro")

        }

    }
}

dependencies {
    implementation(project(":deveshrx_apps"))

    implementation(platform(libs.androidx.compose.bom))

   // implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    implementation(libs.firebase.analytics)
    implementation(libs.play.services.ads)
  //  implementation(libs.play.services.ads.lite)
    val nav_version = "2.7.7"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.compose.material3:material3:1.2.1")




    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    //api(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")


}