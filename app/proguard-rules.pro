# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to the flags specified
# in the main proguard-rules.txt file that ships with the Android SDK.
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.kts.

# Uncomment this to preserve class names with @Serializable when using kotlinx.serialization
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <fields>;
}
