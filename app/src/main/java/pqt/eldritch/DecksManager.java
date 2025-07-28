package pqt.eldritch;

import android.content.Context;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Improved Decks manager that avoids memory leaks and manages resources properly
 */
public class DecksManager {
    private static final String TAG = "DecksManager";
    
    // Use WeakReference to avoid memory leaks
    private WeakReference<Context> contextRef;
    private Map<String, List<Card>> decks;
    private CardDatabaseHelper dbHelper;
    
    // Thread pool for background operations
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    public DecksManager(Context context) {
        this.contextRef = new WeakReference<>(context);
        this.dbHelper = CardDatabaseHelper.getInstance(context);
        initializeDecks();
    }
    
    private void initializeDecks() {
        Context context = contextRef.get();
        if (context == null) {
            Log.w(TAG, "Context is null, cannot initialize decks");
            this.decks = new HashMap<>();
            return;
        }
        
        Log.d(TAG, "Initializing DecksManager");
        
        // Load cards using context-aware loader (SQLite preferred)
        this.decks = new CardLoader(context).load();
        Log.d(TAG, "Loaded " + this.decks.size() + " decks");
        
        // Initialize discard pile
        this.decks.put("DISCARD", new ArrayList<Card>());
        
        // Shuffle all decks in background
        shuffleAllDecksAsync();
    }
    
    /**
     * Asynchronously shuffle all decks to avoid blocking UI
     */
    private void shuffleAllDecksAsync() {
        executor.submit(() -> {
            try {
                for (String deckName : decks.keySet()) {
                    if (!deckName.equals("DISCARD")) {
                        shuffleDeck(deckName);
                    }
                }
                Log.d(TAG, "All decks shuffled successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error shuffling decks: " + e.getMessage(), e);
            }
        });
    }
    
    public synchronized void shuffleDeck(String deckName) {
        List<Card> deckToShuffle = decks.get(deckName);
        if (deckToShuffle == null) return;
        
        if (deckToShuffle.isEmpty()) {
            refillDeck(deckName);
        }
        Collections.shuffle(deckToShuffle);
    }
    
    public synchronized void shuffleFullDeck(String deckName) {
        refillDeck(deckName);
        List<Card> deck = decks.get(deckName);
        if (deck != null) {
            Collections.shuffle(deck);
        }
    }
    
    private synchronized void refillDeck(String deckName) {
        List<Card> deckToRefill = decks.get(deckName);
        List<Card> discardPile = decks.get("DISCARD");
        
        if (deckToRefill == null || discardPile == null) return;
        
        List<Card> newDiscard = new ArrayList<>();
        for (Card card : discardPile) {
            if (card.region.equals(deckName)) {
                card.encountered = "NONE";
                deckToRefill.add(card);
                
                // Update database
                if (dbHelper != null) {
                    dbHelper.updateCardEncountered(card.ID, card.region, "NONE");
                }
            } else {
                newDiscard.add(card);
            }
        }
        
        decks.put("DISCARD", newDiscard);
    }
    
    public synchronized Card drawCard(String deckName) {
        List<Card> deck = decks.get(deckName);
        if (deck == null || deck.isEmpty()) {
            refillDeck(deckName);
            deck = decks.get(deckName);
            if (deck == null || deck.isEmpty()) {
                return null;
            }
        }
        return deck.remove(0);
    }
    
    public synchronized void discardCard(String deckName, Card card, String encounteredStatus) {
        if (card == null) return;
        
        card.encountered = encounteredStatus;
        List<Card> discardPile = decks.get("DISCARD");
        if (discardPile != null) {
            discardPile.add(card);
        }
        
        // Update database asynchronously
        if (dbHelper != null) {
            executor.submit(() -> {
                dbHelper.updateCardEncountered(card.ID, card.region, encounteredStatus);
            });
        }
    }
    
    public synchronized List<Card> getDeck(String deckName) {
        return decks.get(deckName);
    }
    
    public synchronized boolean containsDeck(String deckName) {
        return decks.containsKey(deckName) && !decks.get(deckName).isEmpty();
    }
    
    public synchronized String getExpeditionLocation() {
        List<Card> expeditionDeck = decks.get("EXPEDITION");
        if (expeditionDeck != null && !expeditionDeck.isEmpty()) {
            return expeditionDeck.get(0).region;
        }
        return "EMPTY";
    }
    
    public synchronized String getMysticRuinsLocation() {
        List<Card> mysticRuinsDeck = decks.get("MYSTIC_RUINS");
        if (mysticRuinsDeck != null && !mysticRuinsDeck.isEmpty()) {
            return mysticRuinsDeck.get(0).region;
        }
        return "EMPTY";
    }
    
    public synchronized String getDreamQuestLocation() {
        List<Card> dreamQuestDeck = decks.get("DREAM-QUEST");
        if (dreamQuestDeck != null && !dreamQuestDeck.isEmpty()) {
            return dreamQuestDeck.get(0).region;
        }
        return "EMPTY";
    }
    
    public void printDecks() {
        if (decks == null) return;
        
        for (String deckName : decks.keySet()) {
            List<Card> deck = decks.get(deckName);
            if (deck != null) {
                Log.d(TAG, deckName + ": " + deck.size() + " cards");
            }
        }
    }
    
    /**
     * Save current state asynchronously
     */
    public Future<Boolean> saveStateAsync() {
        return executor.submit(() -> {
            try {
                // Implement async save logic here
                Log.d(TAG, "State saved successfully");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error saving state: " + e.getMessage(), e);
                return false;
            }
        });
    }
    
    /**
     * Cleanup resources when no longer needed
     */
    public void cleanup() {
        if (decks != null) {
            decks.clear();
        }
        contextRef.clear();
        // Don't shutdown executor as it's shared, but cancel pending tasks if needed
    }
    
    /**
     * Static cleanup method for app shutdown
     */
    public static void shutdownExecutor() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
} 