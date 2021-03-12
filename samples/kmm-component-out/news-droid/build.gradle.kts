plugins {
    plugin(Deps.Plugins.androidLibrary)
    plugin(Deps.Plugins.kotlinAndroid)
}

dependencies {
    implementation(Deps.Libs.Android.appCompat)
    implementation(Deps.Libs.Android.material)
    implementation(Deps.Libs.Android.constraintLayout)
    implementation(Deps.Libs.Android.recyclerView)
    implementation(Deps.Libs.Android.mokoMvvmViewBinding)
    implementation(Deps.Libs.Android.androidKtx)
    implementation(Deps.Libs.Android.lifecycleRuntimeKtx)

    implementation(project(parent.path + ":news"))
}
