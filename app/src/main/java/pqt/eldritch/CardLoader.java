package pqt.eldritch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: classes.dex */
public class CardLoader {
    private Document doc;

    public Map<String, List<Card>> load() {
        try {
            if (!loadFile()) {
                System.exit(0);
            }
        } catch (Exception ex) {
            printError(ex);
            System.exit(0);
        }
        return loadCards();
    }

    public Map<String, List<Card>> loadCards() {
        Config.SPECIAL1 = "";
        Config.SPECIAL2 = "";
        Config.SPECIAL3 = "";
        Map<String, List<Card>> decks = new TreeMap<>();
        if (Config.BASE) {
            mergeDecks(decks, loadExpansion("BASE"));
        }
        if (Config.FORSAKEN_LORE) {
            mergeDecks(decks, loadExpansion("FORSAKEN_LORE"));
        }
        if (Config.MOUNTAINS_OF_MADNESS) {
            mergeDecks(decks, loadExpansion("MOUNTAINS_OF_MADNESS"));
        }
        if (Config.STRANGE_REMNANTS) {
            mergeDecks(decks, loadExpansion("STRANGE_REMNANTS"));
        }
        if (Config.UNDER_THE_PYRAMIDS) {
            mergeDecks(decks, loadExpansion("UNDER_THE_PYRAMIDS"));
        }
        if (Config.SIGNS_OF_CARCOSA) {
            mergeDecks(decks, loadExpansion("SIGNS_OF_CARCOSA"));
        }
        if (Config.THE_DREAMLANDS) {
            mergeDecks(decks, loadExpansion("THE_DREAMLANDS"));
        }
        if (Config.CITIES_IN_RUIN) {
            mergeDecks(decks, loadExpansion("CITIES_IN_RUIN"));
        }
        if (Config.MASKS_OF_NYARLATHOTEP) {
            mergeDecks(decks, loadExpansion("MASKS_OF_NYARLATHOTEP"));
        }
        return decks;
    }

    private void mergeDecks(Map<String, List<Card>> decks, Map<String, List<Card>> toMerge) {
        for (String key : toMerge.keySet()) {
            if (decks.containsKey(key)) {
                decks.get(key).addAll(toMerge.get(key));
            } else {
                decks.put(key, toMerge.get(key));
            }
        }
    }

    private Map<String, List<Card>> loadExpansion(String expansion) {
        Map<String, List<Card>> decks = new TreeMap<>();
        try {
            List<Node> EXPANSION_NODES = getChildNodes(this.doc.getElementsByTagName(expansion).item(0).getChildNodes());
            for (Node node : EXPANSION_NODES) {
                String name = getNodeName(node);
                if (name.equals("LOCATIONS")) {
                    decks.putAll(loadLocations(node));
                } else if (name.equals("GATES")) {
                    decks.put("GATE", loadNamedCard(node, "GATE"));
                } else if (name.equals("EXPEDITIONS")) {
                    decks.put("EXPEDITION", loadNamedCard(node, "EXPEDITION"));
                } else if (name.equals("RESEARCH")) {
                    decks.put("RESEARCH", loadResearch(node));
                } else if (name.equals("SPECIAL")) {
                    decks.putAll(loadSpecial(node));
                } else if (name.equals("MYSTIC_RUINS")) {
                    decks.put("MYSTIC_RUINS", loadNamedCard(node, "MYSTIC_RUINS"));
                } else if (name.equals("DREAM-QUEST")) {
                    decks.put("DREAM-QUEST", loadNamedCard(node, "DREAM-QUEST"));
                } else if (name.equals("DISASTER")) {
                    decks.put("DISASTER", loadNamedCardNoHeaders(node, "DISASTER"));
                } else if (name.equals("DEVASTATION")) {
                    decks.put("DEVASTATION", loadSpecial(node, "DEVASTATION"));
                }
            }
            return decks;
        } catch (Exception ex) {
            System.out.println("Failed at loadExpansion " + expansion);
            printError(ex);
            return null;
        }
    }

    private Map<String, List<Card>> loadLocations(Node node) {
        Map<String, List<Card>> locationDecks = new TreeMap<>();
        List<Node> LOCATIONS = getChildNodes(node.getChildNodes());
        for (Node location : LOCATIONS) {
            String regionName = getNodeName(location);
            String topHeader = getNodeText(getSubNode(location, "TOP_HEADER"));
            String middleHeader = getNodeText(getSubNode(location, "MIDDLE_HEADER"));
            String bottomHeader = getNodeText(getSubNode(location, "BOTTOM_HEADER"));
            locationDecks.put(regionName, new ArrayList<>());
            List<Node> cardNodes = getMultipleSubs(location, "CARD");
            for (Node cardNode : cardNodes) {
                Card card = new Card();
                card.region = regionName;
                card.ID = cardNode.getAttributes().getNamedItem("id").getNodeValue();
                card.topHeader = topHeader;
                card.topEncounter = getNodeText(getSubNode(cardNode, "TOP"));
                card.middleHeader = middleHeader;
                card.middleEncounter = getNodeText(getSubNode(cardNode, "MIDDLE"));
                card.bottomHeader = bottomHeader;
                card.bottomEncounter = getNodeText(getSubNode(cardNode, "BOTTOM"));
                locationDecks.get(regionName).add(card);
            }
        }
        return locationDecks;
    }

    private List<Card> loadNamedCard(Node node, String deckName) {
        List<Card> deck = new ArrayList<>();
        List<Node> CARDS = getChildNodes(node.getChildNodes());
        for (Node this_card : CARDS) {
            Card card = new Card();
            card.region = deckName;
            card.ID = this_card.getAttributes().getNamedItem("id").getNodeValue();
            card.topHeader = getNodeText(getSubNode(this_card, "NAME"));
            card.topEncounter = getNodeText(getSubNode(this_card, "TOP"));
            card.middleHeader = "PASS";
            card.middleEncounter = getNodeText(getSubNode(this_card, "MIDDLE"));
            card.bottomHeader = "FAIL";
            card.bottomEncounter = getNodeText(getSubNode(this_card, "BOTTOM"));
            deck.add(card);
        }
        return deck;
    }

    private List<Card> loadResearch(Node node) {
        List<Card> researchDeck = new ArrayList<>();
        List<Node> CARDS = getMultipleSubs(getSubNode(node, Config.ANCIENT_ONE.toUpperCase()), "CARD");
        if (CARDS != null) {
            String topHeader = getNodeText(getSubNode(node, "TOP_HEADER"));
            String middleHeader = getNodeText(getSubNode(node, "MIDDLE_HEADER"));
            String bottomHeader = getNodeText(getSubNode(node, "BOTTOM_HEADER"));
            for (Node this_card : CARDS) {
                Card card = new Card();
                card.region = "RESEARCH";
                card.ID = this_card.getAttributes().getNamedItem("id").getNodeValue();
                card.topHeader = topHeader;
                card.topEncounter = getNodeText(getSubNode(this_card, "TOP"));
                card.middleHeader = middleHeader;
                card.middleEncounter = getNodeText(getSubNode(this_card, "MIDDLE"));
                card.bottomHeader = bottomHeader;
                card.bottomEncounter = getNodeText(getSubNode(this_card, "BOTTOM"));
                researchDeck.add(card);
            }
        }
        return researchDeck;
    }

    private Map<String, List<Card>> loadSpecial(Node node) {
        Map<String, List<Card>> special = new TreeMap<>();
        Node ancientOneNode = getSubNode(node, Config.ANCIENT_ONE.toUpperCase());
        if (ancientOneNode != null) {
            Node special1 = getSubNode(ancientOneNode, "SPECIAL-1");
            special.put("SPECIAL-1", loadSpecial(special1, "SPECIAL-1"));
            Config.SPECIAL1 = special.get("SPECIAL-1").get(0).topHeader;
            Node special2 = getSubNode(ancientOneNode, "SPECIAL-2");
            if (special2 != null) {
                special.put("SPECIAL-2", loadSpecial(special2, "SPECIAL-2"));
                Config.SPECIAL2 = special.get("SPECIAL-2").get(0).topHeader;
            }
            Node special3 = getSubNode(ancientOneNode, "SPECIAL-3");
            if (special3 != null) {
                special.put("SPECIAL-3", loadSpecial(special3, "SPECIAL-3"));
                Config.SPECIAL3 = special.get("SPECIAL-3").get(0).topHeader;
            }
        }
        return special;
    }

    private List<Card> loadSpecial(Node specialNode, String deck) {
        List<Node> CARDS = getMultipleSubs(specialNode, "CARD");
        List<Card> specialCards = new ArrayList<>();
        String topHeader = getNodeText(getSubNode(specialNode, "NAME"));
        for (Node this_card : CARDS) {
            Card card = new Card();
            card.region = deck;
            card.ID = this_card.getAttributes().getNamedItem("id").getNodeValue();
            card.topHeader = topHeader;
            card.topEncounter = getNodeText(getSubNode(this_card, "TOP"));
            card.middleHeader = "PASS";
            card.middleEncounter = getNodeText(getSubNode(this_card, "MIDDLE"));
            card.bottomHeader = "FAIL";
            card.bottomEncounter = getNodeText(getSubNode(this_card, "BOTTOM"));
            specialCards.add(card);
        }
        return specialCards;
    }

    private List<Card> loadNamedCardNoHeaders(Node node, String deckName) {
        List<Card> deck = loadNamedCard(node, deckName);
        for (Card card : deck) {
            card.middleHeader = null;
            card.bottomHeader = null;
        }
        return deck;
    }

    private String getNodeText(Node node) {
        return node == null ? "" : node.getTextContent().trim();
    }

    private Node getSubNode(Node parent, String name) {
        if (parent == null) {
            return null;
        }
        NodeList subNodes = parent.getChildNodes();
        for (int i = 0; i < subNodes.getLength(); i++) {
            Node subNode = subNodes.item(i);
            if (subNode.getNodeName().equals(name)) {
                return subNode;
            }
        }
        return null;
    }

    private List<Node> getMultipleSubs(Node node, String name) {
        if (node == null) {
            return null;
        }
        List<Node> subs = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals(name)) {
                subs.add(childNode);
            }
        }
        return subs;
    }

    private List<Node> getChildNodes(NodeList nodes) {
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (!getNodeName(nodes.item(i)).equals("#text")) {
                children.add(nodes.item(i));
            }
        }
        return children;
    }

    private String getNodeName(Node node) {
        return node == null ? "" : node.getNodeName();
    }

    private boolean loadFile() throws ParserConfigurationException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(getClass().getClassLoader().getResourceAsStream("assets/cards.xml"));
            this.doc.getDocumentElement().normalize();
            return true;
        } catch (Exception ex) {
            System.out.println("Failed at Loading File!");
            printError(ex);
            return false;
        }
    }

    private void printError(Exception ex) {
        System.out.println(ex.getMessage());
        System.out.println(ex.toString());
        for (StackTraceElement x : ex.getStackTrace()) {
            System.out.println(x.toString());
        }
    }
}
