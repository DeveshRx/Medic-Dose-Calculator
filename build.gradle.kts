


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.googleGmsGoogleServices) apply false
    alias(libs.plugins.compose.compiler) apply false

}


ext {
    val myDemoProperty = findProperty("myProperty") ?: "Hello, world!"
}

//println "My property value: ${project.ext.myProperty}"


val admob_app_id by extra("xxxxxxxxx")
val admob_ad_banner1 by extra("adbanner1")
