package pqt.eldritch;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class Decks {
    public static Decks CARDS;
    private Map<String, List<Card>> decks = new CardLoader().load();

    public Decks() {
        this.decks.put("DISCARD", new ArrayList());
        shuffleAllDecks();
        CARDS = this;
    }

    private void shuffleAllDecks() {
        for (String deckName : this.decks.keySet()) {
            shuffleDeck(deckName);
        }
    }

    public void shuffleDeck(String deckName) {
        List<Card> deckToShuffle = this.decks.get(deckName);
        if (deckToShuffle.isEmpty()) {
            refillDeck(deckName);
        }
        Collections.shuffle(this.decks.get(deckName));
    }

    public void shuffleFullDeck(String deckName) {
        refillDeck(deckName);
        Collections.shuffle(this.decks.get(deckName));
    }

    private void refillDeck(String deckName) {
        List<Card> deckToRefill = this.decks.get(deckName);
        List<Card> newDiscard = new ArrayList<>();
        for (Card card : this.decks.get("DISCARD")) {
            if (card.region.equals(deckName)) {
                card.encountered = "NONE";
                deckToRefill.add(card);
            } else {
                newDiscard.add(card);
            }
        }
        this.decks.put(deckName, deckToRefill);
        this.decks.put("DISCARD", newDiscard);
    }

    public boolean containsDeck(String deckName) {
        return this.decks.containsKey(deckName);
    }

    public List<Card> getDeck(String deckName) {
        List<Card> deck = this.decks.get(deckName);
        if (deck == null) {
            return null;
        }
        if (deck.isEmpty()) {
            shuffleDeck(deckName);
            return deck;
        }
        return deck;
    }

    public String getExpeditionLocation() {
        List<Card> expeditionDeck = this.decks.get("EXPEDITION");
        if (expeditionDeck == null || expeditionDeck.isEmpty()) {
            return "EMPTY";
        }
        if (!Config.LITANY_OF_SECRETS) {
            return expeditionDeck.get(0).topHeader;
        }
        String location = expeditionDeck.get(0).topHeader;
        if (expeditionDeck.size() > 1) {
            return location + ", " + expeditionDeck.get(1).topHeader;
        }
        return location;
    }

    public String getMysticRuinsLocation() {
        List<Card> mysticRuinsDeck = this.decks.get("MYSTIC_RUINS");
        return (mysticRuinsDeck == null || mysticRuinsDeck.isEmpty()) ? "EMPTY" : mysticRuinsDeck.get(0).topHeader;
    }

    public String getDreamQuestLocation() {
        List<Card> dreamQuestDeck = this.decks.get("DREAM-QUEST");
        return (dreamQuestDeck == null || dreamQuestDeck.isEmpty()) ? "EMPTY" : dreamQuestDeck.get(0).topHeader;
    }

    public void printDecks() {
        Log.d("DECKS", "=============");
        for (String deckName : this.decks.keySet()) {
            Log.d("DECKS", deckName + "\t\t" + this.decks.get(deckName).size());
        }
        Log.d("DECKS", "=============");
        for (Card card : this.decks.get("DISCARD")) {
            System.out.println(card.region);
        }
        Log.d("DECKS", "=============");
    }

    public void removeExpeditions(String region) {
        List<Card> expeditionDeck = this.decks.get("EXPEDITION");
        List<Card> newExpedition = new ArrayList<>();
        for (Card card : expeditionDeck) {
            if (card.topHeader.equals(region)) {
                card.encountered = "removed";
                this.decks.get("DISCARD").add(card);
            } else {
                newExpedition.add(card);
            }
        }
        this.decks.put("EXPEDITION", newExpedition);
        if (!newExpedition.isEmpty()) {
            Collections.shuffle(newExpedition);
        }
    }

    public void discardCard(String deck, String ID, String encountered) {
        List<Card> deckToDiscardFrom = this.decks.get(deck);
        for (int i = 0; i < deckToDiscardFrom.size(); i++) {
            Card check = deckToDiscardFrom.get(i);
            if (check.ID.equals(ID)) {
                check.encountered = encountered;
                this.decks.get("DISCARD").add(0, deckToDiscardFrom.remove(i));
                return;
            }
        }
    }

    public void removeCardFromDiscard(int position) {
        Card card = this.decks.get("DISCARD").remove(position);
        card.encountered = "NONE";
        this.decks.get(card.region).add(card);
        shuffleDeck(card.region);
    }

    public String getRegion(String deck, int position) {
        String region = this.decks.get(deck).get(position).region;
        if (deck.equals("DISCARD")) {
            return "Discard - " + region;
        }
        return region;
    }
}
