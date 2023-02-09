

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
```
