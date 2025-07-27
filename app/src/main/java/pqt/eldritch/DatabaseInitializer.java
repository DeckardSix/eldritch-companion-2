package pqt.eldritch;

import android.content.Context;
import android.util.Log;
import java.util.List;
import java.util.Map;

public class DatabaseInitializer {
    private static final String TAG = "DatabaseInitializer";
    
    /**
     * Forces database initialization/re-initialization
     * This will clear existing data and reload from XML
     */
    public static boolean initializeDatabase(Context context) {
        return initializeDatabase(context, true);
    }
    
    /**
     * Initialize database with option to preserve existing data
     * @param context Android context
     * @param forceReinit If true, clears existing data first
     * @return true if successful, false otherwise
     */
    public static boolean initializeDatabase(Context context, boolean forceReinit) {
        try {
            Log.d(TAG, "Starting database initialization (forceReinit=" + forceReinit + ")");
            
            CardDatabaseHelper dbHelper = CardDatabaseHelper.getInstance(context);
            
            if (forceReinit) {
                Log.d(TAG, "Clearing existing database...");
                dbHelper.clearAllCards();
            }
            
            // Check if we need to populate the database
            if (!dbHelper.hasCards()) {
                Log.d(TAG, "Database is empty, starting migration...");
                XMLToSQLiteMigration migration = new XMLToSQLiteMigration(context);
                
                if (migration.performMigration()) {
                    Log.d(TAG, "Database initialization successful!");
                    Log.d(TAG, migration.getMigrationStats());
                    return true;
                } else {
                    Log.e(TAG, "Database initialization failed during migration");
                    return false;
                }
            } else {
                Log.d(TAG, "Database already contains data, initialization not needed");
                return true;
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error during database initialization: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Check database status and return information
     */
    public static String getDatabaseStatus(Context context) {
        try {
            CardDatabaseHelper dbHelper = CardDatabaseHelper.getInstance(context);
            StringBuilder status = new StringBuilder();
            
            status.append("Database Status:\n");
            status.append("Has Cards: ").append(dbHelper.hasCards()).append("\n");
            
            if (dbHelper.hasCards()) {
                XMLToSQLiteMigration migration = new XMLToSQLiteMigration(context);
                status.append(migration.getMigrationStats());
            } else {
                status.append("Database is empty\n");
            }
            
            return status.toString();
            
        } catch (Exception e) {
            return "Error checking database status: " + e.getMessage();
        }
    }
    
    /**
     * Test database operations
     */
    public static String testDatabase(Context context) {
        try {
            Log.d(TAG, "Running database tests...");
            
            CardDatabaseHelper dbHelper = CardDatabaseHelper.getInstance(context);
            
            // Test 1: Check if database exists and is accessible
            boolean hasCards = dbHelper.hasCards();
            Log.d(TAG, "Test 1 - Database accessible: " + hasCards);
            
            // Test 2: Try to get all cards
            if (hasCards) {
                Map<String, List<Card>> allCards = dbHelper.getAllCards();
                Log.d(TAG, "Test 2 - Retrieved " + allCards.size() + " card decks");
                
                // Test 3: Check a specific region
                if (allCards.containsKey("AMERICAS")) {
                    List<Card> americasCards = allCards.get("AMERICAS");
                    Log.d(TAG, "Test 3 - AMERICAS deck has " + americasCards.size() + " cards");
                }
            }
            
            return getDatabaseStatus(context);
            
        } catch (Exception e) {
            String error = "Database test failed: " + e.getMessage();
            Log.e(TAG, error, e);
            return error;
        }
    }
    
    /**
     * Static method to initialize database when called from other contexts
     * Can be used for programmatic initialization
     */
    public static void forceInitialization(Context context) {
        Log.d(TAG, "Force initialization requested");
        boolean success = initializeDatabase(context, true);
        Log.d(TAG, "Force initialization result: " + success);
        
        if (success) {
            Log.d(TAG, getDatabaseStatus(context));
        }
    }
} 