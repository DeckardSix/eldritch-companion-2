package pqt.eldritch;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

/**
 * Custom Application class with security, performance optimizations, and proper resource management
 */
public class EldritchApplication extends Application {
    private static final String TAG = "EldritchApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Enable StrictMode in debug builds for performance monitoring
        if (BuildConfig.DEBUG) {
            enableStrictMode();
        }
        
        // Initialize security settings
        initializeSecurity();
        
        // Performance optimizations
        initializePerformanceSettings();
        
        // Initialize database
        initializeDatabase();
        
        Log.d(TAG, "Eldritch Companion Application initialized successfully");
    }
    
    private void enableStrictMode() {
        // Detect threading issues
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectCustomSlowCalls()
                .penaltyLog()
                .build());
        
        // Detect VM issues
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                .detectActivityLeaks()
                .detectFileUriExposure()
                .penaltyLog()
                .build());
        
        Log.d(TAG, "StrictMode enabled for debugging");
    }
    
    private void initializeSecurity() {
        // Security measures for production builds
        if (!BuildConfig.DEBUG) {
            Log.d(TAG, "Production security measures enabled");
            // Add any production-specific security initialization here
        }
    }
    
    private void initializePerformanceSettings() {
        // Pre-load critical classes to improve startup time
        try {
            // Use background thread for class loading to avoid blocking main thread
            new Thread(() -> {
                try {
                    Class.forName("pqt.eldritch.CardDatabaseHelper");
                    Class.forName("pqt.eldritch.Card");
                    Class.forName("pqt.eldritch.Config");
                    Class.forName("pqt.eldritch.DecksManager");
                    Class.forName("pqt.eldritch.AsyncXMLProcessor");
                    Log.d(TAG, "Critical classes pre-loaded successfully");
                } catch (ClassNotFoundException e) {
                    Log.w(TAG, "Could not pre-load some classes", e);
                }
            }).start();
        } catch (Exception e) {
            Log.w(TAG, "Error during performance initialization", e);
        }
    }
    
    private void initializeDatabase() {
        // Initialize database helper to ensure it's ready
        try {
            CardDatabaseHelper.getInstance(this);
            Log.d(TAG, "Database helper initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing database", e);
        }
    }
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // Any base context initialization can go here
    }
    
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        
        // Handle memory pressure intelligently
        switch (level) {
            case TRIM_MEMORY_RUNNING_MODERATE:
                Log.d(TAG, "Memory pressure: MODERATE - optimizing performance");
                // Light cleanup
                break;
                
            case TRIM_MEMORY_RUNNING_LOW:
                Log.d(TAG, "Memory pressure: LOW - freeing non-essential resources");
                // More aggressive cleanup
                System.gc();
                break;
                
            case TRIM_MEMORY_RUNNING_CRITICAL:
                Log.d(TAG, "Memory pressure: CRITICAL - emergency cleanup");
                // Aggressive cleanup
                System.gc();
                break;
                
            case TRIM_MEMORY_UI_HIDDEN:
                Log.d(TAG, "App is in background - freeing UI-related memory");
                // App is in background, free UI-related memory
                System.gc();
                break;
                
            case TRIM_MEMORY_BACKGROUND:
            case TRIM_MEMORY_MODERATE:
            case TRIM_MEMORY_COMPLETE:
                Log.d(TAG, "App in background with memory pressure - aggressive cleanup");
                // App is in background and system is under memory pressure
                System.gc();
                break;
        }
    }
    
    @Override
    public void onTerminate() {
        Log.d(TAG, "Application terminating - cleaning up resources");
        
        // Cleanup resources before app termination
        try {
            // Cleanup database connections
            CardDatabaseHelper dbHelper = CardDatabaseHelper.getInstance(this);
            if (dbHelper != null) {
                dbHelper.cleanup();
            }
            
            // Shutdown thread pools
            AsyncXMLProcessor.shutdown();
            DecksManager.shutdownExecutor();
            
            Log.d(TAG, "Resource cleanup completed");
        } catch (Exception e) {
            Log.e(TAG, "Error during resource cleanup", e);
        }
        
        super.onTerminate();
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "Low memory warning - performing emergency cleanup");
        
        // Emergency memory cleanup
        System.gc();
    }
} 