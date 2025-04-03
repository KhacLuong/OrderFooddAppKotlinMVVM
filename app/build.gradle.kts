plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mycinemamanagerkotlinmvc"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mycinemamanagerkotlinmvc"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    // implementation(platform("com.google.firebase:firebase-bom:31.0.3"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")

    // MaterialDialog
    implementation("com.afollestad.material-dialogs:core:0.9.6.0")
    //Gson
    implementation("com.google.code.gson:gson:2.10")
    //Glide ImageLoader
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    // Indicator
    implementation("me.relex:circleindicator:2.1.6")
    // QR code
    implementation ("com.google.zxing:core:3.4.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.1.0")
    //event bus
    implementation ("org.greenrobot:eventbus:3.0.0")
    // Play movie
    implementation ("androidx.media3:media3-ui:1.5.1")
    implementation ("androidx.media3:media3-session:1.5.1")
    implementation ("androidx.media3:media3-exoplayer:1.5.1")
    implementation ("androidx.media3:media3-common:1.5.1")

    // Flowlayout for category
    implementation ("com.wefika:flowlayout:0.4.1")

    // Paypal
    implementation ("com.paypal.sdk:paypal-android-sdk:2.16.0")

    configurations.all {
        exclude(group = "com.intellij", module = "annotations")
    }


}