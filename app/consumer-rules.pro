# Consumer ProGuard rules for library dependencies

# Keep all public APIs that might be used by other modules
-keep public class pqt.eldritch.** {
    public *;
}

# Keep database models
-keep class pqt.eldritch.Card { *; }

# Keep custom exceptions
-keep class pqt.eldritch.**Exception { *; } 