plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.loancalculator.finance.manager"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sfsa.bbb.ffff.loan"
        minSdk = 24
        targetSdk = 36
        versionCode = 10
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
    buildFeatures {
        compose = false
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.getActivity:MultiLanguages:10.2")
    implementation("com.github.OCNYang.ImmersionBar:immersionbar:3.4.6")
    implementation("com.github.OCNYang.ImmersionBar:immersionbar-ktx:3.4.6")
    implementation("com.airbnb.android:lottie:6.7.1")
    implementation("io.coil-kt:coil-video:2.7.0")
    implementation("io.coil-kt:coil:2.7.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")
    implementation("com.github.getActivity:XXPermissions:23.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("org.greenrobot:eventbus:3.3.1")
}