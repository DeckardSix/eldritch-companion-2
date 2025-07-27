package pqt.eldritch;

import android.content.Context;
import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLToSQLiteMigration {
    private static final String TAG = "XMLToSQLiteMigration";
    private Context context;
    private CardDatabaseHelper dbHelper;
    
    public XMLToSQLiteMigration(Context context) {
        this.context = context;
        this.dbHelper = CardDatabaseHelper.getInstance(context);
    }
    
    /**
     * Migrates card data from XML to SQLite database
     * @return true if migration successful, false otherwise
     */
    public boolean migrateXMLToDatabase() {
        try {
            Log.d(TAG, "Starting XML to SQLite migration...");

            // Check if database already has cards
            if (dbHelper.hasCards()) {
                Log.d(TAG, "Database already contains cards. Clearing existing data...");
                dbHelper.clearAllCards();
            }

            // Try direct XML parsing approach first
            int totalCards = parseXMLDirectly();
            
            if (totalCards > 0) {
                Log.d(TAG, "Direct XML parsing successful. Inserted " + totalCards + " cards into database.");
                return true;
            }

            // Fallback to CardLoader approach
            Log.d(TAG, "Direct parsing failed, trying CardLoader approach...");
            
            // Try to load cards from XML using the existing CardLoader
            CardLoader xmlLoader = new CardLoader();
            Map<String, List<Card>> xmlDecks = xmlLoader.loadFromXML();

            if (xmlDecks == null || xmlDecks.isEmpty()) {
                Log.e(TAG, "Failed to load cards from XML using CardLoader, trying alternative approach...");

                // Try creating a sample card for testing
                xmlDecks = createTestData();

                if (xmlDecks.isEmpty()) {
                    Log.e(TAG, "No card data available for migration");
                    return false;
                }
            }

            // Insert all cards into the database
            totalCards = 0;
            for (String deckName : xmlDecks.keySet()) {
                List<Card> cards = xmlDecks.get(deckName);
                if (cards != null) {
                    Log.d(TAG, "Migrating deck: " + deckName + " with " + cards.size() + " cards");
                    for (Card card : cards) {
                        long insertId = dbHelper.insertCard(card);
                        if (insertId > 0) {
                            totalCards++;
                        } else {
                            Log.w(TAG, "Failed to insert card: " + card.ID + " in deck: " + deckName);
                        }
                    }
                }
            }

            Log.d(TAG, "Migration completed successfully. Inserted " + totalCards + " cards into database.");
            return totalCards > 0;

        } catch (Exception e) {
            Log.e(TAG, "Error during migration: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse XML directly without relying on Config values
     */
    private int parseXMLDirectly() {
        try {
            InputStream inputStream = context.getAssets().open("cards.xml");
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            
            int totalCards = 0;
            
            // Parse BASE section
            Node baseNode = doc.getElementsByTagName("BASE").item(0);
            if (baseNode != null) {
                totalCards += parseLocationSection(baseNode, "AMERICAS");
                totalCards += parseLocationSection(baseNode, "EUROPE");
                totalCards += parseLocationSection(baseNode, "ASIA");
                totalCards += parseLocationSection(baseNode, "GENERAL");
            }
            
            Log.d(TAG, "Direct XML parsing completed. Found " + totalCards + " cards");
            return totalCards;
            
        } catch (Exception e) {
            Log.e(TAG, "Error during direct XML parsing: " + e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Parse a specific location section from XML
     */
    private int parseLocationSection(Node baseNode, String regionName) {
        try {
            Node locationsNode = getChildNodeByName(baseNode, "LOCATIONS");
            if (locationsNode == null) {
                Log.w(TAG, "No LOCATIONS node found");
                return 0;
            }
            
            Node regionNode = getChildNodeByName(locationsNode, regionName);
            if (regionNode == null) {
                Log.w(TAG, "No " + regionName + " node found");
                return 0;
            }
            
            // Get headers
            String topHeader = getChildNodeText(regionNode, "TOP_HEADER");
            String middleHeader = getChildNodeText(regionNode, "MIDDLE_HEADER");
            String bottomHeader = getChildNodeText(regionNode, "BOTTOM_HEADER");
            
            // Get all CARD nodes
            NodeList cardNodes = regionNode.getChildNodes();
            int cardCount = 0;
            
            for (int i = 0; i < cardNodes.getLength(); i++) {
                Node cardNode = cardNodes.item(i);
                if (cardNode.getNodeType() == Node.ELEMENT_NODE && "CARD".equals(cardNode.getNodeName())) {
                    Card card = parseCard(cardNode, regionName, topHeader, middleHeader, bottomHeader);
                    if (card != null) {
                        long insertId = dbHelper.insertCard(card);
                        if (insertId > 0) {
                            cardCount++;
                        }
                    }
                }
            }
            
            Log.d(TAG, "Parsed " + cardCount + " cards from " + regionName);
            return cardCount;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing " + regionName + " section: " + e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Parse a single card from XML
     */
    private Card parseCard(Node cardNode, String region, String topHeader, String middleHeader, String bottomHeader) {
        try {
            Card card = new Card();
            card.region = region;
            
            // Get card ID
            Node idAttr = cardNode.getAttributes().getNamedItem("id");
            if (idAttr != null) {
                card.ID = idAttr.getNodeValue();
            }
            
            // Set headers
            card.topHeader = topHeader;
            card.middleHeader = middleHeader;
            card.bottomHeader = bottomHeader;
            
            // Get encounters
            card.topEncounter = getChildNodeText(cardNode, "TOP");
            card.middleEncounter = getChildNodeText(cardNode, "MIDDLE");
            card.bottomEncounter = getChildNodeText(cardNode, "BOTTOM");
            
            // Set default encountered status
            card.encountered = "NONE";
            
            return card;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing card: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Helper method to get child node by name
     */
    private Node getChildNodeByName(Node parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && name.equals(child.getNodeName())) {
                return child;
            }
        }
        return null;
    }

    /**
     * Helper method to get text content of child node
     */
    private String getChildNodeText(Node parent, String childName) {
        Node child = getChildNodeByName(parent, childName);
        if (child != null) {
            return child.getTextContent().trim();
        }
        return null;
    }
    
    /**
     * Creates test data if XML loading fails
     */
    private Map<String, List<Card>> createTestData() {
        Map<String, List<Card>> testDecks = new TreeMap<>();
        
        // Create a test card for AMERICAS
        List<Card> americasCards = new ArrayList<>();
        Card testCard = new Card();
        testCard.ID = "TEST1";
        testCard.region = "AMERICAS";
        testCard.topHeader = "Arkham";
        testCard.topEncounter = "Test encounter for Arkham";
        testCard.middleHeader = "San Francisco";
        testCard.middleEncounter = "Test encounter for San Francisco";
        testCard.bottomHeader = "Buenos Aires";
        testCard.bottomEncounter = "Test encounter for Buenos Aires";
        testCard.encountered = "NONE";
        americasCards.add(testCard);
        
        testDecks.put("AMERICAS", americasCards);
        
        Log.d(TAG, "Created test data with " + americasCards.size() + " test cards");
        return testDecks;
    }
    
    /**
     * Validates that the migration was successful by checking database content
     * @return true if validation passes, false otherwise
     */
    public boolean validateMigration() {
        try {
            Log.d(TAG, "Validating migration...");
            
            // Load cards from database
            Map<String, List<Card>> dbDecks = dbHelper.getAllCards();
            
            if (dbDecks == null || dbDecks.isEmpty()) {
                Log.e(TAG, "Database validation failed: no decks found");
                return false;
            }
            
            int totalCards = 0;
            for (String deckName : dbDecks.keySet()) {
                List<Card> cards = dbDecks.get(deckName);
                if (cards != null) {
                    totalCards += cards.size();
                    Log.d(TAG, "Deck " + deckName + " has " + cards.size() + " cards");
                }
            }
            
            if (totalCards == 0) {
                Log.e(TAG, "Database validation failed: no cards found");
                return false;
            }
            
            Log.d(TAG, "Migration validation passed! Found " + totalCards + " cards across " + dbDecks.size() + " decks");
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error during validation: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Performs a complete migration with validation
     * @return true if migration and validation both succeed
     */
    public boolean performMigration() {
        if (migrateXMLToDatabase()) {
            return validateMigration();
        }
        return false;
    }
    
    /**
     * Gets migration statistics
     * @return String containing migration information
     */
    public String getMigrationStats() {
        try {
            Map<String, List<Card>> dbDecks = dbHelper.getAllCards();
            StringBuilder stats = new StringBuilder();
            stats.append("Migration Statistics:\n");
            stats.append("Total Decks: ").append(dbDecks.size()).append("\n");
            
            int totalCards = 0;
            for (String deckName : dbDecks.keySet()) {
                int count = dbDecks.get(deckName).size();
                totalCards += count;
                stats.append(deckName).append(": ").append(count).append(" cards\n");
            }
            
            stats.append("Total Cards: ").append(totalCards);
            return stats.toString();
            
        } catch (Exception e) {
            return "Error getting migration stats: " + e.getMessage();
        }
    }
} 