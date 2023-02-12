

##PhotoGallery App

```groovy
def fragment_version = "1.5.5"
implementation "androidx.fragment:fragment-ktx:$fragment_version"

implementation "androidx.recyclerview:recyclerview:1.2.1"

// ViewModel
def lifecycle_version = "2.5.1"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

// Lifecycles only (without ViewModel or LiveData)
implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"

// Retrofit
implementation 'com.squareup.retrofit2:retrofit:2.9.0'

// Okhttp
implementation 'com.squareup.okhttp3:okhttp:4.10.0'

// Coroutines
// To include core functions of coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
// To include android specific parts
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
```

####To add Moshi:
- Add kapt plugin in project level `build.gradle`, Make sure it is compatible with `kotlin.android`:
```groovy
plugins {
//    ...
//    ...
    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false
    id 'org.jetbrains.kotlin.kapt' version '1.8.0' apply false
}
```
- In, `app/build.gradle`:
```groovy
plugins {
//    ...
    id 'org.jetbrains.kotlin.kapt'
}
```
- Add Moshi dependency:
```groovy
// core moshi
implementation 'com.squareup.moshi:moshi:1.14.0'

// moshi-kotlin-codegen for maping JSON to kotlin classes
kapt 'com.squareup.moshi:moshi-kotlin-codegen:1.14.0'

// converter-moshi to deserialize response body 
implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
```