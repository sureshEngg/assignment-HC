-keep public class com.google.common.**
-keep class javax.inject.** { *; }

-keep class com.google.**
-dontwarn com.google.**
-dontwarn android.support.**

# Room database
-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource

# Unit test
-dontwarn org.mockito.**
-dontwarn kotlin.Unit

# Retrofit
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn retrofit2.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

# Glide
-keep public class * extends com.bumptech.glide.Glide
