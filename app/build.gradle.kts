plugins {
    alias(libs.plugins.android.application)
    // هنا نحذف رقم الإصدار لأننا وضعناه في الملف الرئيسي
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.animapedia"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.animapedia"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    // Firebase
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

    // UI + Android basics
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // EXTRA
    implementation("com.google.android.material:material:1.11.0")

    // Image loading libraries
    implementation("com.squareup.picasso:picasso:2.71828")

    // ⭐ Correct Glide statement (Kotlin DSL)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("androidx.cardview:cardview:1.0.0")
}
