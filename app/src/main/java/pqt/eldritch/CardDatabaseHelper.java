package pqt.eldritch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CardDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eldritch_cards.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table name
    private static final String TABLE_CARDS = "cards";
    
    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CARD_ID = "card_id";
    private static final String COLUMN_REGION = "region";
    private static final String COLUMN_TOP_HEADER = "top_header";
    private static final String COLUMN_TOP_ENCOUNTER = "top_encounter";
    private static final String COLUMN_MIDDLE_HEADER = "middle_header";
    private static final String COLUMN_MIDDLE_ENCOUNTER = "middle_encounter";
    private static final String COLUMN_BOTTOM_HEADER = "bottom_header";
    private static final String COLUMN_BOTTOM_ENCOUNTER = "bottom_encounter";
    private static final String COLUMN_ENCOUNTERED = "encountered";
    private static final String COLUMN_CARD_NAME = "card_name"; // For named cards like Gates
    
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
    
    private static CardDatabaseHelper instance;
    
    private CardDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static synchronized CardDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CardDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CARDS);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }
    
    // Insert a card into the database
    public long insertCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        
        long id = db.insert(TABLE_CARDS, null, values);
        db.close();
        return id;
    }
    
    // Insert a named card (like Gates) into the database
    public long insertNamedCard(String cardId, String region, String cardName, String topEncounter, String middleEncounter, String bottomEncounter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_CARD_ID, cardId);
        values.put(COLUMN_REGION, region);
        values.put(COLUMN_TOP_ENCOUNTER, topEncounter);
        values.put(COLUMN_MIDDLE_ENCOUNTER, middleEncounter);
        values.put(COLUMN_BOTTOM_ENCOUNTER, bottomEncounter);
        values.put(COLUMN_ENCOUNTERED, "NONE");
        values.put(COLUMN_CARD_NAME, cardName);
        
        long id = db.insert(TABLE_CARDS, null, values);
        db.close();
        return id;
    }
    
    // Get all cards for a specific region/deck
    public List<Card> getCardsByRegion(String region) {
        List<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selection = COLUMN_REGION + " = ?";
        String[] selectionArgs = {region};
        
        Cursor cursor = db.query(TABLE_CARDS, null, selection, selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Card card = createCardFromCursor(cursor);
                cards.add(card);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return cards;
    }
    
    // Get all cards organized by region (like the original XML loader)
    public Map<String, List<Card>> getAllCards() {
        Map<String, List<Card>> decks = new TreeMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_CARDS, null, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Card card = createCardFromCursor(cursor);
                String region = card.region;
                
                if (!decks.containsKey(region)) {
                    decks.put(region, new ArrayList<Card>());
                }
                decks.get(region).add(card);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return decks;
    }
    
    // Update a card's encountered status
    public int updateCardEncountered(String cardId, String region, String encountered) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENCOUNTERED, encountered);
        
        String selection = COLUMN_CARD_ID + " = ? AND " + COLUMN_REGION + " = ?";
        String[] selectionArgs = {cardId, region};
        
        int count = db.update(TABLE_CARDS, values, selection, selectionArgs);
        db.close();
        return count;
    }
    
    // Get a specific card by ID and region
    public Card getCard(String cardId, String region) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selection = COLUMN_CARD_ID + " = ? AND " + COLUMN_REGION + " = ?";
        String[] selectionArgs = {cardId, region};
        
        Cursor cursor = db.query(TABLE_CARDS, null, selection, selectionArgs, null, null, null);
        
        Card card = null;
        if (cursor.moveToFirst()) {
            card = createCardFromCursor(cursor);
        }
        
        cursor.close();
        db.close();
        return card;
    }
    
    // Helper method to create Card object from cursor
    private Card createCardFromCursor(Cursor cursor) {
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
    }
    
    // Clear all cards from database
    public void clearAllCards() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDS, null, null);
        db.close();
    }
    
    // Check if database has any cards
    public boolean hasCards() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CARDS, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            android.util.Log.d("CardDatabaseHelper", "Card count in database: " + count);
            return count > 0;
        } catch (Exception e) {
            android.util.Log.e("CardDatabaseHelper", "Error checking card count: " + e.getMessage(), e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
    
    // Get count of cards in a specific region
    public int getCardCountForRegion(String region) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_REGION + " = ?";
        String[] selectionArgs = {region};
        
        Cursor cursor = db.query(TABLE_CARDS, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
} 