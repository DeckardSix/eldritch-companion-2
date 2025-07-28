package pqt.eldritch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CardDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "CardDatabaseHelper";
    private static final String DATABASE_NAME = "eldritch_cards.db";
    private static final int DATABASE_VERSION = 1;
    
    // Singleton instance with proper synchronization
    private static volatile CardDatabaseHelper instance;
    private static final Object lock = new Object();
    
    // Thread-safe read/write operations
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    // Table and column names
    private static final String TABLE_CARDS = "cards";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CARD_ID = "card_id";
    private static final String COLUMN_REGION = "region";
    private static final String COLUMN_TOP_HEADER = "top_header";
    private static final String COLUMN_TOP_ENCOUNTER = "top_encounter";
    private static final String COLUMN_MIDDLE_HEADER = "middle_header";
    private static final String COLUMN_MIDDLE_ENCOUNTER = "middle_encounter";
    private static final String COLUMN_BOTTOM_HEADER = "bottom_header";
    private static final String COLUMN_BOTTOM_ENCOUNTER = "bottom_encounter";
    private static final String COLUMN_ENCOUNTERED = "encountered";
    private static final String COLUMN_CARD_NAME = "card_name";

    // Create table SQL
    private static final String CREATE_TABLE_CARDS =
        "CREATE TABLE " + TABLE_CARDS + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_CARD_ID + " TEXT NOT NULL, " +
        COLUMN_REGION + " TEXT NOT NULL, " +
        COLUMN_TOP_HEADER + " TEXT, " +
        COLUMN_TOP_ENCOUNTER + " TEXT, " +
        COLUMN_MIDDLE_HEADER + " TEXT, " +
        COLUMN_MIDDLE_ENCOUNTER + " TEXT, " +
        COLUMN_BOTTOM_HEADER + " TEXT, " +
        COLUMN_BOTTOM_ENCOUNTER + " TEXT, " +
        COLUMN_ENCOUNTERED + " TEXT DEFAULT 'NONE', " +
        COLUMN_CARD_NAME + " TEXT" +
        ");";

    private CardDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Thread-safe singleton getInstance method
     */
    public static CardDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new CardDatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating cards table");
        db.execSQL(CREATE_TABLE_CARDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }

    /**
     * Thread-safe method to insert a card with automatic resource management
     */
    public long insertCard(Card card) {
        rwLock.writeLock().lock();
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CARD_ID, card.ID);
            values.put(COLUMN_REGION, card.region);
            values.put(COLUMN_TOP_HEADER, card.topHeader);
            values.put(COLUMN_TOP_ENCOUNTER, card.topEncounter);
            values.put(COLUMN_MIDDLE_HEADER, card.middleHeader);
            values.put(COLUMN_MIDDLE_ENCOUNTER, card.middleEncounter);
            values.put(COLUMN_BOTTOM_HEADER, card.bottomHeader);
            values.put(COLUMN_BOTTOM_ENCOUNTER, card.bottomEncounter);
            values.put(COLUMN_ENCOUNTERED, card.encountered);
            
            return db.insert(TABLE_CARDS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error inserting card: " + e.getMessage(), e);
            return -1;
        } finally {
            rwLock.writeLock().unlock();
            // Don't close db here as it's managed by SQLiteOpenHelper
        }
    }

    /**
     * Thread-safe method to get all cards grouped by region
     */
    public Map<String, List<Card>> getAllCards() {
        rwLock.readLock().lock();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_CARDS, null, null, null, null, null, COLUMN_REGION + ", " + COLUMN_ID);
            
            Map<String, List<Card>> cardMap = new HashMap<>();
            
            while (cursor.moveToNext()) {
                Card card = createCardFromCursor(cursor);
                if (card != null) {
                    List<Card> regionCards = cardMap.get(card.region);
                    if (regionCards == null) {
                        regionCards = new ArrayList<>();
                        cardMap.put(card.region, regionCards);
                    }
                    regionCards.add(card);
                }
            }
            
            Log.d(TAG, "Retrieved " + cardMap.size() + " regions from database");
            return cardMap;
            
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving all cards: " + e.getMessage(), e);
            return new HashMap<>();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            rwLock.readLock().unlock();
        }
    }

    /**
     * Thread-safe method to check if database has cards
     */
    public boolean hasCards() {
        rwLock.readLock().lock();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CARDS, null);
            
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                Log.d(TAG, "Card count in database: " + count);
                return count > 0;
            }
            return false;
            
        } catch (Exception e) {
            Log.e(TAG, "Error checking if database has cards: " + e.getMessage(), e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            rwLock.readLock().unlock();
        }
    }

    /**
     * Thread-safe method to clear all cards
     */
    public void clearAllCards() {
        rwLock.writeLock().lock();
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int deletedRows = db.delete(TABLE_CARDS, null, null);
            Log.d(TAG, "Cleared " + deletedRows + " cards from database");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing cards: " + e.getMessage(), e);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Thread-safe method to update card encountered status
     */
    public boolean updateCardEncountered(String cardId, String region, String encounteredStatus) {
        rwLock.writeLock().lock();
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ENCOUNTERED, encounteredStatus);
            
            String whereClause = COLUMN_CARD_ID + "=? AND " + COLUMN_REGION + "=?";
            String[] whereArgs = {cardId, region};
            
            int rowsUpdated = db.update(TABLE_CARDS, values, whereClause, whereArgs);
            Log.d(TAG, "Updated " + rowsUpdated + " card(s) with ID: " + cardId);
            return rowsUpdated > 0;
            
        } catch (Exception e) {
            Log.e(TAG, "Error updating card encountered status: " + e.getMessage(), e);
            return false;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Helper method to create Card object from cursor with proper error handling
     */
    private Card createCardFromCursor(Cursor cursor) {
        try {
            Card card = new Card();
            card.ID = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_ID));
            card.region = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REGION));
            card.topHeader = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOP_HEADER));
            card.topEncounter = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOP_ENCOUNTER));
            card.middleHeader = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIDDLE_HEADER));
            card.middleEncounter = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIDDLE_ENCOUNTER));
            card.bottomHeader = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOTTOM_HEADER));
            card.bottomEncounter = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOTTOM_ENCOUNTER));
            card.encountered = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENCOUNTERED));
            return card;
        } catch (Exception e) {
            Log.e(TAG, "Error creating card from cursor: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Cleanup method to be called when app is destroyed
     */
    public synchronized void cleanup() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
} 