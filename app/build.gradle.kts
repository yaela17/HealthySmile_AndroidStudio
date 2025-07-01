plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.googleService)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.healthysmile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.healthysmile"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val apiBaseUrl: String = project.findProperty("API_BASE_URL") as String
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
    packagingOptions{
        exclude ("META-INF/DEPENDENCIES")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation("com.google.firebase:firebase-database:20.0.4");
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0");
    implementation("com.google.firebase:firebase-messaging:23.1.0")
    implementation("io.socket:socket.io-client:2.0.1")
    implementation("org.rajawali3d:rajawali:1.0.325@aar")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation ("io.github.sceneview:arsceneview:2.2.1")
    implementation("com.google.ar:core:1.30.0")
    implementation("io.github.sceneview:arsceneview:1.2.2")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.prolificinteractive:material-calendarview:2.0.0") {
        exclude(group = "com.android.support", module = "support-compat")
        exclude(group = "com.android.support", module = "appcompat-v7")
    }
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")
    implementation("com.google.android.filament:filament-android:1.12.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.google.android.filament:gltfio-android:1.32.1")
    implementation("com.google.android.filament:filament-utils-android:1.32.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation ("com.github.clans:fab:1.6.4")
    implementation(files("libs/openpay-android-release.aar"))
    implementation ("com.android.support:support-fragment:28.0.0")
    implementation ("com.google.http-client:google-http-client:1.28.0")
    implementation ("com.google.http-client:google-http-client-android:1.28.0")
    implementation ("com.google.http-client:google-http-client-jackson2:1.28.0")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.9.8")
    implementation ("org.slf4j:slf4j-log4j12:1.7.25")

    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    // Lombok
    implementation ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
}