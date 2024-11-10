plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleService)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.tiendavirtualapp_kotlin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tiendavirtualapp_kotlin"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }




    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"

    }
   viewBinding{
       enable = true
   }
    buildFeatures {
        viewBinding = true
    }

}


dependencies {

    implementation(libs.lottie)/*Animaciones*/
    implementation(libs.firebaseBoom)/*Autenticacion con google */
    implementation(libs.googlePlayServices)/*Servicios de google */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)/*Localizacion*/
    testImplementation(libs.junit)
    implementation(libs.realTimeDatabase)/*Base de datos en tiempo real*/
    implementation(libs.firebaseAuth)/*Autenticacion con firebase*/
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}