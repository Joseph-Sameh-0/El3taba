plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.el3taba"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.el3taba"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
}

dependencies {

    // Import the BoM for the Firebase platform
    implementation(libs.firebase.bom.v3340)

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.google.firebase.auth)

    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.core.i18n)
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Firebase Firestore

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.database)
    implementation(libs.firebase.config)

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // https://firebase.google.com/docs/android/setup#available-libraries

    // for svg files
//    implementation(libs.appcompat.v7)

    implementation(libs.dagger) // Use the latest version
    kapt(libs.dagger.compiler)

    implementation(libs.hilt.android) // Use the latest version
    kapt(libs.hilt.android.compiler)
    // For instrumentation tests
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    // For local unit teststestImplementation("com.google.dagger:hilt-android-testing:2.44")
    kaptTest(libs.hilt.android.compiler)
    implementation(libs.google.firebase.firestore)

    implementation(libs.circleindicator)

    implementation(libs.glide)
    kapt(libs.compiler)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// Apply the Google services plugin at the bottom
apply(plugin = "com.google.gms.google-services")