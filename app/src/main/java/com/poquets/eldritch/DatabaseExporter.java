package com.poquets.eldritch;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class to export the database file for inclusion in the project
 * This allows the database to be pre-populated and stored in assets/databases/
 */
public class DatabaseExporter {
    private static final String TAG = "DatabaseExporter";
    
    /**
     * Exports the current database to a file that can be included in the project
     * @param context Android context
     * @param outputPath Path where to save the database file (e.g., external storage or project directory)
     * @return true if export successful, false otherwise
     */
    public static boolean exportDatabase(Context context, String outputPath) {
        try {
            CardDatabaseHelper dbHelper = CardDatabaseHelper.getInstance(context);
            
            // Ensure database is populated
            if (!dbHelper.hasCards()) {
                Log.w(TAG, "Database is empty, populating from XML first...");
                XMLToSQLiteMigration migration = new XMLToSQLiteMigration(context);
                if (!migration.performMigration()) {
                    Log.e(TAG, "Failed to populate database for export");
                    return false;
                }
            }
            
            // Get the database file path
            String dbPath = context.getDatabasePath("eldritch_cards.db").getAbsolutePath();
            File dbFile = new File(dbPath);
            
            if (!dbFile.exists()) {
                Log.e(TAG, "Database file does not exist at: " + dbPath);
                return false;
            }
            
            // Copy database file to output path
            File outputFile = new File(outputPath);
            File outputDir = outputFile.getParentFile();
            if (outputDir != null && !outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            try (FileInputStream inputStream = new FileInputStream(dbFile);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                
                Log.d(TAG, "Database exported successfully to: " + outputPath);
                Log.d(TAG, "File size: " + outputFile.length() + " bytes");
                return true;
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error exporting database: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Exports database to external storage (for easy access)
     * @param context Android context
     * @return Path to exported file, or null if failed
     */
    public static String exportToExternalStorage(Context context) {
        try {
            File externalDir = context.getExternalFilesDir(null);
            if (externalDir == null) {
                Log.e(TAG, "External storage not available");
                return null;
            }
            
            String outputPath = new File(externalDir, "eldritch_cards.db").getAbsolutePath();
            if (exportDatabase(context, outputPath)) {
                Log.d(TAG, "Database exported to: " + outputPath);
                return outputPath;
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error exporting to external storage: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Copies database from assets to app database directory
     * This is called automatically by CardDatabaseHelper.onCreate()
     */
    public static boolean copyDatabaseFromAssets(Context context) {
        try {
            String dbPath = context.getDatabasePath("eldritch_cards.db").getAbsolutePath();
            File dbFile = new File(dbPath);
            
            // If database already exists and has data, don't overwrite
            if (dbFile.exists() && dbFile.length() > 0) {
                Log.d(TAG, "Database already exists, skipping copy from assets");
                return true;
            }
            
            // Try to copy from assets/databases/eldritch_cards.db
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = context.getAssets().open("databases/eldritch_cards.db");
                
                // Ensure database directory exists
                File dbDir = dbFile.getParentFile();
                if (dbDir != null && !dbDir.exists()) {
                    dbDir.mkdirs();
                }
                
                outputStream = new FileOutputStream(dbPath);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                
                outputStream.flush();
                Log.d(TAG, "Successfully copied pre-populated database from assets");
                return true;
                
            } catch (IOException e) {
                // Database file not in assets - this is OK
                Log.d(TAG, "Pre-populated database not found in assets (will use migration): " + e.getMessage());
                return false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing input stream", e);
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing output stream", e);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error copying database from assets: " + e.getMessage(), e);
            return false;
        }
    }
}


