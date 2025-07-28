# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt

# Keep your application class
-keep public class pqt.eldritch.** { *; }

# Keep your database classes
-keep class pqt.eldritch.Card { *; }
-keep class pqt.eldritch.CardDatabaseHelper { *; }

# Keep AndroidX and Support Library classes
-keep class androidx.** { *; }
-dontwarn androidx.**

# Keep SQLite classes
-keep class android.database.** { *; }
-keep class android.database.sqlite.** { *; }

# Keep XML parsing classes
-keep class javax.xml.** { *; }
-keep class org.w3c.dom.** { *; }
-dontwarn javax.xml.**
-dontwarn org.w3c.dom.**

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Optimization
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile 