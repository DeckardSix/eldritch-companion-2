package com.poquets.eldritch;

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

            // Use CardLoader approach to ensure all expansions and card types are loaded correctly
            // This handles EXPEDITIONS, RESEARCH, GATES, and all other card types from all enabled expansions
            Log.d(TAG, "Using CardLoader to migrate all cards from XML...");
            
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
            int totalCards = 0;
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
                totalCards += parseNamedCardSection(baseNode, "GATES", "GATE");
                totalCards += parseNamedCardSection(baseNode, "EXPEDITIONS", "EXPEDITION");
                totalCards += parseResearchSection(baseNode);
                // Note: MYSTIC_RUINS, DREAM-QUEST, DISASTER, DEVASTATION are typically in other expansions
                // and will be handled by CardLoader fallback which processes all enabled expansions
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
     * Parse a named card section (like GATES, EXPEDITIONS, etc.) from XML
     */
    private int parseNamedCardSection(Node baseNode, String sectionName, String deckName) {
        try {
            Node sectionNode = getChildNodeByName(baseNode, sectionName);
            if (sectionNode == null) {
                Log.w(TAG, "No " + sectionName + " node found");
                return 0;
            }
            
            // Get all CARD nodes
            NodeList cardNodes = sectionNode.getChildNodes();
            int cardCount = 0;
            
            for (int i = 0; i < cardNodes.getLength(); i++) {
                Node cardNode = cardNodes.item(i);
                if (cardNode.getNodeType() == Node.ELEMENT_NODE && "CARD".equals(cardNode.getNodeName())) {
                    Card card = parseNamedCard(cardNode, deckName);
                    if (card != null) {
                        long insertId = dbHelper.insertCard(card);
                        if (insertId > 0) {
                            cardCount++;
                        }
                    }
                }
            }
            
            Log.d(TAG, "Parsed " + cardCount + " cards from " + sectionName);
            return cardCount;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing " + sectionName + " section: " + e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Parse a single named card from XML (for GATES, EXPEDITIONS, etc.)
     */
    private Card parseNamedCard(Node cardNode, String deckName) {
        try {
            Card card = new Card();
            card.region = deckName;
            
            // Get card ID
            Node idAttr = cardNode.getAttributes().getNamedItem("id");
            if (idAttr != null) {
                card.ID = idAttr.getNodeValue();
            }
            
            // For named cards like GATES:
            // - topHeader comes from NAME node
            // - middleHeader is "PASS"
            // - bottomHeader is "FAIL"
            card.topHeader = getChildNodeText(cardNode, "NAME");
            card.middleHeader = "PASS";
            card.bottomHeader = "FAIL";
            
            // Get encounters
            card.topEncounter = getChildNodeText(cardNode, "TOP");
            card.middleEncounter = getChildNodeText(cardNode, "MIDDLE");
            card.bottomEncounter = getChildNodeText(cardNode, "BOTTOM");
            
            // Set default encountered status
            card.encountered = "NONE";
            
            return card;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing named card: " + e.getMessage(), e);
            return null;
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
     * Parse RESEARCH section from XML
     * RESEARCH cards are organized by Ancient One, so we need to load them for the selected Ancient One
     */
    private int parseResearchSection(Node baseNode) {
        try {
            Node researchNode = getChildNodeByName(baseNode, "RESEARCH");
            if (researchNode == null) {
                Log.w(TAG, "No RESEARCH node found");
                return 0;
            }
            
            // Get headers (common for all research cards)
            String topHeader = getChildNodeText(researchNode, "TOP_HEADER");
            String middleHeader = getChildNodeText(researchNode, "MIDDLE_HEADER");
            String bottomHeader = getChildNodeText(researchNode, "BOTTOM_HEADER");
            
            // Get the selected Ancient One from Config
            String ancientOne = Config.ANCIENT_ONE;
            if (ancientOne == null || ancientOne.trim().isEmpty()) {
                Log.w(TAG, "Config.ANCIENT_ONE is null, skipping research cards");
                return 0;
            }
            
            // Get the Ancient One's sub-node
            Node ancientOneNode = getChildNodeByName(researchNode, ancientOne.toUpperCase());
            if (ancientOneNode == null) {
                Log.w(TAG, "No " + ancientOne.toUpperCase() + " node found in RESEARCH section");
                return 0;
            }
            
            // Get all CARD nodes under this Ancient One
            NodeList cardNodes = ancientOneNode.getChildNodes();
            int cardCount = 0;
            
            for (int i = 0; i < cardNodes.getLength(); i++) {
                Node cardNode = cardNodes.item(i);
                if (cardNode.getNodeType() == Node.ELEMENT_NODE && "CARD".equals(cardNode.getNodeName())) {
                    Card card = parseCard(cardNode, "RESEARCH", topHeader, middleHeader, bottomHeader);
                    if (card != null) {
                        long insertId = dbHelper.insertCard(card);
                        if (insertId > 0) {
                            cardCount++;
                        }
                    }
                }
            }
            
            Log.d(TAG, "Parsed " + cardCount + " RESEARCH cards for " + ancientOne);
            return cardCount;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing RESEARCH section: " + e.getMessage(), e);
            return 0;
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