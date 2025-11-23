# Consumer ProGuard rules for library dependencies

# Keep all public APIs that might be used by other modules
-keep public class com.poquets.eldritch.** {
    public *;
}

# Keep database models
-keep class com.poquets.eldritch.Card { *; }

# Keep custom exceptions
-keep class com.poquets.eldritch.**Exception { *; }