package pqt.eldritch;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Async XML processor to handle heavy operations off the main thread
 */
public class AsyncXMLProcessor {
    private static final String TAG = "AsyncXMLProcessor";
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    /**
     * Interface for XML processing callbacks
     */
    public interface XMLProcessingCallback {
        void onSuccess(Map<String, List<Card>> result);
        void onError(Exception error);
        void onProgress(String status);
    }
    
    /**
     * Async method to load cards from XML
     */
    public static CompletableFuture<Map<String, List<Card>>> loadCardsAsync(Context context) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.d(TAG, "Starting async XML loading");
                CardLoader loader = new CardLoader(context);
                Map<String, List<Card>> result = loader.loadFromXML();
                Log.d(TAG, "Async XML loading completed");
                return result;
            } catch (Exception e) {
                Log.e(TAG, "Error during async XML loading", e);
                throw new RuntimeException(e);
            }
        }, executor);
    }
    
    /**
     * Async method to migrate XML to SQLite
     */
    public static CompletableFuture<Boolean> migrateXMLToSQLiteAsync(Context context, XMLProcessingCallback callback) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (callback != null) {
                    callback.onProgress("Starting XML to SQLite migration...");
                }
                
                XMLToSQLiteMigration migration = new XMLToSQLiteMigration(context);
                boolean result = migration.migrateXMLToDatabase();
                
                if (callback != null) {
                    callback.onProgress("Migration completed");
                }
                
                return result;
            } catch (Exception e) {
                Log.e(TAG, "Error during async migration", e);
                if (callback != null) {
                    callback.onError(e);
                }
                return false;
            }
        }, executor);
    }
    
    /**
     * Async method to validate migration
     */
    public static CompletableFuture<Boolean> validateMigrationAsync(Context context) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.d(TAG, "Starting async migration validation");
                XMLToSQLiteMigration migration = new XMLToSQLiteMigration(context);
                boolean result = migration.validateMigration();
                Log.d(TAG, "Async migration validation completed: " + result);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "Error during async validation", e);
                return false;
            }
        }, executor);
    }
    
    /**
     * Async method to initialize database
     */
    public static CompletableFuture<Boolean> initializeDatabaseAsync(Context context, boolean forceReinit, XMLProcessingCallback callback) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (callback != null) {
                    callback.onProgress("Initializing database...");
                }
                
                boolean result = DatabaseInitializer.initializeDatabase(context, forceReinit);
                
                if (callback != null) {
                    callback.onProgress("Database initialization completed");
                }
                
                return result;
            } catch (Exception e) {
                Log.e(TAG, "Error during async database initialization", e);
                if (callback != null) {
                    callback.onError(e);
                }
                return false;
            }
        }, executor);
    }
    
    /**
     * Deprecated AsyncTask wrapper for backward compatibility
     */
    @Deprecated
    public static class XMLLoadingTask extends AsyncTask<Void, String, Map<String, List<Card>>> {
        private WeakReference<Context> contextRef;
        private XMLProcessingCallback callback;
        private Exception error;
        
        public XMLLoadingTask(Context context, XMLProcessingCallback callback) {
            this.contextRef = new WeakReference<>(context);
            this.callback = callback;
        }
        
        @Override
        protected void onPreExecute() {
            if (callback != null) {
                callback.onProgress("Starting XML processing...");
            }
        }
        
        @Override
        protected Map<String, List<Card>> doInBackground(Void... voids) {
            Context context = contextRef.get();
            if (context == null) {
                return null;
            }
            
            try {
                publishProgress("Loading XML file...");
                CardLoader loader = new CardLoader(context);
                
                publishProgress("Parsing XML content...");
                Map<String, List<Card>> result = loader.loadFromXML();
                
                publishProgress("XML processing completed");
                return result;
                
            } catch (Exception e) {
                this.error = e;
                return null;
            }
        }
        
        @Override
        protected void onProgressUpdate(String... values) {
            if (callback != null && values.length > 0) {
                callback.onProgress(values[0]);
            }
        }
        
        @Override
        protected void onPostExecute(Map<String, List<Card>> result) {
            if (callback != null) {
                if (error != null) {
                    callback.onError(error);
                } else if (result != null) {
                    callback.onSuccess(result);
                } else {
                    callback.onError(new RuntimeException("Unknown error occurred"));
                }
            }
        }
    }
    
    /**
     * Shutdown executor when app is destroyed
     */
    public static void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
} 