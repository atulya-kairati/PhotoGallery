

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
