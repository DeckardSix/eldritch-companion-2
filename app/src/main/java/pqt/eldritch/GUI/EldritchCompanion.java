package pqt.eldritch.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pqt.eldritch.Card;
import pqt.eldritch.Config;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class EldritchCompanion extends Activity {
    private Button expeditionButton;
    private Button mysticRuinsButton;
    private Button dreamQuestButton;
    
    public void removeExpedition(View view) {
        if (Decks.CARDS.getExpeditionLocation().equals("EMPTY")) {
            Toast.makeText(getApplicationContext(), "Expedition Deck is Empty.", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getApplicationContext(), (Class<?>) RemoveExpedition.class));
        }
    }

    public void drawCard(View view) {
        Intent intent = null;
        if (view.getId() == R.id.discardButton) {
            if (Decks.CARDS.getDeck("DISCARD").isEmpty()) {
                Toast.makeText(getApplicationContext(), "Discard Pile is Empty.", Toast.LENGTH_SHORT).show();
            } else {
                intent = new Intent(getApplicationContext(), (Class<?>) DiscardGallery.class);
            }
        } else {
            intent = new Intent(getApplicationContext(), (Class<?>) DeckGallery.class);
        }
        if (intent != null) {
            switch (view.getId()) {
                case R.id.africaButton /* 2130968580 */:
                    intent.putExtra("DECK", "AFRICA");
                    break;
                case R.id.americasButton /* 2130968581 */:
                    intent.putExtra("DECK", "AMERICAS");
                    break;
                case R.id.antEastButton /* 2130968583 */:
                    intent.putExtra("DECK", "ANTARCTICA-EAST");
                    break;
                case R.id.antResearchButton /* 2130968584 */:
                    intent.putExtra("DECK", "ANTARCTICA-RESEARCH");
                    break;
                case R.id.antWestButton /* 2130968585 */:
                    intent.putExtra("DECK", "ANTARCTICA-WEST");
                    break;
                case R.id.asiaButton /* 2130968587 */:
                    intent.putExtra("DECK", "ASIA");
                    break;
                case R.id.devastationButton /* 2130968596 */:
                    intent.putExtra("DECK", "DEVASTATION");
                    break;
                case R.id.disasterButton /* 2130968597 */:
                    intent.putExtra("DECK", "DISASTER");
                    break;
                case R.id.discardButton /* 2130968598 */:
                    intent.putExtra("DECK", "DISCARD");
                    break;
                case R.id.dreamQuestButton /* 2130968599 */:
                    intent.putExtra("DECK", "DREAM-QUEST");
                    break;
                case R.id.dreamlandsButton /* 2130968601 */:
                    intent.putExtra("DECK", "DREAMLANDS");
                    break;
                case R.id.egyptButton /* 2130968603 */:
                    intent.putExtra("DECK", "EGYPT");
                    break;
                case R.id.europeButton /* 2130968604 */:
                    intent.putExtra("DECK", "EUROPE");
                    break;
                case R.id.expeditionButton /* 2130968607 */:
                    intent.putExtra("DECK", "EXPEDITION");
                    break;
                case R.id.gateButton /* 2130968610 */:
                    intent.putExtra("DECK", "GATE");
                    break;
                case R.id.generalButton /* 2130968611 */:
                    intent.putExtra("DECK", "GENERAL");
                    break;
                case R.id.mysticRuinsButton /* 2130968620 */:
                    intent.putExtra("DECK", "MYSTIC_RUINS");
                    break;
                case R.id.researchButton /* 2130968623 */:
                    intent.putExtra("DECK", "RESEARCH");
                    break;
                case R.id.special1Button /* 2130968626 */:
                    intent.putExtra("DECK", "SPECIAL-1");
                    break;
                case R.id.special2Button /* 2130968627 */:
                    intent.putExtra("DECK", "SPECIAL-2");
                    break;
                case R.id.special3Button /* 2130968628 */:
                    intent.putExtra("DECK", "SPECIAL-3");
                    break;
            }
            startActivity(intent);
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create the main layout programmatically
        ScrollView scrollView = new ScrollView(this);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(50, 50, 50, 50);
        
        // Create buttons for different regions
        createButton(mainLayout, "americasButton", "Arkham - San Francisco - Buenos Aires", "#ff00741e", "drawCard");
        createButton(mainLayout, "europeButton", "London - Rome - Istanbul", "#ffc96900", "drawCard");
        createButton(mainLayout, "asiaButton", "Shanghai - Tokyo - Sydney", "#ff5a0e8a", "drawCard");
        
        // Antarctica buttons (conditional)
        if (Config.ANTARCTICA || Config.ANCIENT_ONE.equals("Rise_of_the_Elder_Things")) {
            createButton(mainLayout, "antWestButton", "City of the Elder Things - Plateau of Leng - Snowy Mountains", "#ffb897bb", "drawCard");
            createButton(mainLayout, "antEastButton", "Frozen Waste - Lake Camp - Miskatonic Outpost", "#a1746100", "drawCard");
        }
        
        // Egypt buttons (conditional)
        if (Config.EGYPT || Config.ANCIENT_ONE.equals("Nephren-Ka")) {
            createButton(mainLayout, "africaButton", "The Sahara Desert - Tel el-Amarna - The Nile River", "#7b5000", "drawCard");
            createButton(mainLayout, "egyptButton", "Alexandria - The Bent Pyramid - Cairo", "#b60101", "drawCard");
        }
        
        // Dreamlands buttons (conditional)
        if (Config.DREAMLANDS_BOARD || Config.ANCIENT_ONE.equals("Hypnos")) {
            createButton(mainLayout, "dreamlandsButton", "Celephaïs - Ulthar - Dylath-Leen", "#8caa2f", "drawCard");
        }
        
        createButton(mainLayout, "generalButton", "City - Wilderness - Sea", "#5e000000", "drawCard");
        createButton(mainLayout, "gateButton", "Gate", "#fe00cd5b", "drawCard");
        
        // Expedition section with horizontal layout
        LinearLayout expLayout = new LinearLayout(this);
        expLayout.setOrientation(LinearLayout.HORIZONTAL);
        expLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        
        expeditionButton = new Button(this);
        expeditionButton.setText("Expedition [LOCATION]");
        expeditionButton.setTextSize(15);
        expeditionButton.setTextColor(android.graphics.Color.WHITE);
        expeditionButton.setBackgroundColor(android.graphics.Color.parseColor("#ff1a0077"));
        expeditionButton.setId(R.id.expeditionButton);
        expeditionButton.setLayoutParams(new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 4));
        expeditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard(v);
            }
        });
        expLayout.addView(expeditionButton);
        
        Button removeButton = new Button(this);
        removeButton.setText("Remove");
        removeButton.setTextSize(15);
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeExpedition(v);
            }
        });
        expLayout.addView(removeButton);
        
        mainLayout.addView(expLayout);
        
        // Mystic Ruins button (conditional)
        if (Config.COSMIC_ALIGNMENT || Config.ANCIENT_ONE.equals("Syzygy") || Config.ANCIENT_ONE.equals("Antediluvium")) {
            mysticRuinsButton = new Button(this);
            mysticRuinsButton.setText("Mystic Ruins [LOCATION]");
            mysticRuinsButton.setTextSize(15);
            mysticRuinsButton.setTextColor(android.graphics.Color.WHITE);
            mysticRuinsButton.setBackgroundColor(android.graphics.Color.parseColor("#a21d00f2"));
            mysticRuinsButton.setId(R.id.mysticRuinsButton);
            mysticRuinsButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT));
            mysticRuinsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
            mainLayout.addView(mysticRuinsButton);
        }
        
        // Dream Quest button (conditional)
        if (Config.DREAMLANDS_BOARD || Config.ANCIENT_ONE.equals("Hypnos")) {
            dreamQuestButton = new Button(this);
            dreamQuestButton.setText("Dream-Quest [LOCATION]");
            dreamQuestButton.setTextSize(15);
            dreamQuestButton.setTextColor(android.graphics.Color.WHITE);
            dreamQuestButton.setBackgroundColor(android.graphics.Color.parseColor("#5e30ad"));
            dreamQuestButton.setId(R.id.dreamQuestButton);
            dreamQuestButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT));
            dreamQuestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
            mainLayout.addView(dreamQuestButton);
        }
        
        createButton(mainLayout, "researchButton", "Research", "#ff000000", "drawCard");
        
        // Antarctica Research button (conditional)
        if (Config.ANTARCTICA || Config.ANCIENT_ONE.equals("Rise_of_the_Elder_Things")) {
            createButton(mainLayout, "antResearchButton", "Antarctica Research", "#d98b4512", "drawCard");
        }
        
        // Special buttons (conditional)
        if (Decks.CARDS.containsDeck("SPECIAL-1")) {
            createButton(mainLayout, "special1Button", Config.SPECIAL1, "#d9578b84", "drawCard");
        }
        if (Decks.CARDS.containsDeck("SPECIAL-2")) {
            createButton(mainLayout, "special2Button", Config.SPECIAL2, "#d92c8b5e", "drawCard");
        }
        if (Decks.CARDS.containsDeck("SPECIAL-3")) {
            createButton(mainLayout, "special3Button", Config.SPECIAL3, "#d9075e35", "drawCard");
        }
        
        // Disaster buttons (conditional)
        if (Config.CITIES_IN_RUIN) {
            createButton(mainLayout, "disasterButton", "Disaster", "#ff0000ff", "drawCard");
            createButton(mainLayout, "devastationButton", "Devastation", "#ffff0000", "drawCard");
        }
        
        createButton(mainLayout, "discardButton", "Discard", "#ff3d4876", "drawCard");
        
        scrollView.addView(mainLayout);
        setContentView(scrollView);
        
        setTitle(Config.ANCIENT_ONE.replace("_", " ").replace(".", "'"));
    }
    
    private void createButton(LinearLayout parent, String id, String text, String color, String onClickMethod) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(15);
        button.setTextColor(android.graphics.Color.WHITE);
        button.setBackgroundColor(android.graphics.Color.parseColor(color));
        button.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        
        // Set the button ID based on the id parameter
        if (id.equals("americasButton")) {
            button.setId(R.id.americasButton);
        } else if (id.equals("europeButton")) {
            button.setId(R.id.europeButton);
        } else if (id.equals("asiaButton")) {
            button.setId(R.id.asiaButton);
        } else if (id.equals("antWestButton")) {
            button.setId(R.id.antWestButton);
        } else if (id.equals("antEastButton")) {
            button.setId(R.id.antEastButton);
        } else if (id.equals("africaButton")) {
            button.setId(R.id.africaButton);
        } else if (id.equals("egyptButton")) {
            button.setId(R.id.egyptButton);
        } else if (id.equals("dreamlandsButton")) {
            button.setId(R.id.dreamlandsButton);
        } else if (id.equals("generalButton")) {
            button.setId(R.id.generalButton);
        } else if (id.equals("gateButton")) {
            button.setId(R.id.gateButton);
        } else if (id.equals("researchButton")) {
            button.setId(R.id.researchButton);
        } else if (id.equals("antResearchButton")) {
            button.setId(R.id.antResearchButton);
        } else if (id.equals("special1Button")) {
            button.setId(R.id.special1Button);
        } else if (id.equals("special2Button")) {
            button.setId(R.id.special2Button);
        } else if (id.equals("special3Button")) {
            button.setId(R.id.special3Button);
        } else if (id.equals("disasterButton")) {
            button.setId(R.id.disasterButton);
        } else if (id.equals("devastationButton")) {
            button.setId(R.id.devastationButton);
        } else if (id.equals("discardButton")) {
            button.setId(R.id.discardButton);
        }
        
        if (onClickMethod.equals("drawCard")) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        } else if (onClickMethod.equals("removeExpedition")) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeExpedition(v);
                }
            });
        }
        
        parent.addView(button);
    }

    private void saveGame() throws TransformerException, DOMException, TransformerFactoryConfigurationError, IOException, IllegalArgumentException {
        try {
            Document documentNewDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element elementCreateElement = documentNewDocument.createElement("GAME");
            documentNewDocument.appendChild(elementCreateElement);
            Element node = documentNewDocument.createElement("ANCIENT_ONE");
            node.appendChild(documentNewDocument.createTextNode(Config.ANCIENT_ONE));
            elementCreateElement.appendChild(node);
            Element node2 = documentNewDocument.createElement("BASE");
            node2.appendChild(documentNewDocument.createTextNode(Config.BASE + ""));
            elementCreateElement.appendChild(node2);
            Element node3 = documentNewDocument.createElement("FORSAKEN_LORE");
            node3.appendChild(documentNewDocument.createTextNode(Config.FORSAKEN_LORE + ""));
            elementCreateElement.appendChild(node3);
            Element node4 = documentNewDocument.createElement("MOUNTAINS_OF_MADNESS");
            node4.appendChild(documentNewDocument.createTextNode(Config.MOUNTAINS_OF_MADNESS + ""));
            elementCreateElement.appendChild(node4);
            Element node5 = documentNewDocument.createElement("ANTARCTICA");
            node5.appendChild(documentNewDocument.createTextNode(Config.ANTARCTICA + ""));
            elementCreateElement.appendChild(node5);
            Element node6 = documentNewDocument.createElement("STRANGE_REMNANTS");
            node6.appendChild(documentNewDocument.createTextNode(Config.STRANGE_REMNANTS + ""));
            elementCreateElement.appendChild(node6);
            Element node7 = documentNewDocument.createElement("COSMIC_ALIGNMENT");
            node7.appendChild(documentNewDocument.createTextNode(Config.COSMIC_ALIGNMENT + ""));
            elementCreateElement.appendChild(node7);
            Element node8 = documentNewDocument.createElement("UNDER_THE_PYRAMIDS");
            node8.appendChild(documentNewDocument.createTextNode(Config.UNDER_THE_PYRAMIDS + ""));
            elementCreateElement.appendChild(node8);
            Element node9 = documentNewDocument.createElement("EGYPT");
            node9.appendChild(documentNewDocument.createTextNode(Config.EGYPT + ""));
            elementCreateElement.appendChild(node9);
            Element node10 = documentNewDocument.createElement("LITANY_OF_SECRETS");
            node10.appendChild(documentNewDocument.createTextNode(Config.LITANY_OF_SECRETS + ""));
            elementCreateElement.appendChild(node10);
            Element node11 = documentNewDocument.createElement("SIGNS_OF_CARCOSA");
            node11.appendChild(documentNewDocument.createTextNode(Config.SIGNS_OF_CARCOSA + ""));
            elementCreateElement.appendChild(node11);
            Element node12 = documentNewDocument.createElement("THE_DREAMLANDS");
            node12.appendChild(documentNewDocument.createTextNode(Config.THE_DREAMLANDS + ""));
            elementCreateElement.appendChild(node12);
            Element node13 = documentNewDocument.createElement("DREAMLANDS_BOARD");
            node13.appendChild(documentNewDocument.createTextNode(Config.DREAMLANDS_BOARD + ""));
            elementCreateElement.appendChild(node13);
            Element node14 = documentNewDocument.createElement("CITIES_IN_RUIN");
            node14.appendChild(documentNewDocument.createTextNode(Config.CITIES_IN_RUIN + ""));
            elementCreateElement.appendChild(node14);
            Element node15 = documentNewDocument.createElement("MASKS_OF_NYARLATHOTEP");
            node15.appendChild(documentNewDocument.createTextNode(Config.MASKS_OF_NYARLATHOTEP + ""));
            elementCreateElement.appendChild(node15);
            Element discardPile = documentNewDocument.createElement("DISCARD_PILE");
            elementCreateElement.appendChild(discardPile);
            for (Card card : Decks.CARDS.getDeck("DISCARD")) {
                Element cardNode = documentNewDocument.createElement("CARD");
                cardNode.setAttribute("region", card.region);
                cardNode.setAttribute("id", card.ID);
                if (card.encountered != null) {
                    cardNode.setAttribute("encountered", card.encountered);
                }
                discardPile.appendChild(cardNode);
            }
            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "discard.xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(documentNewDocument);
            StreamResult result = new StreamResult(new OutputStreamWriter(fos, "utf-8"));
            transformer.transform(source, result);
            fos.close();
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.setOutputProperty("indent", "yes");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StreamResult res = new StreamResult(new StringWriter());
            DOMSource sour = new DOMSource(documentNewDocument);
            transformer.transform(sour, res);
            String xmlString = res.getWriter().toString();
            System.out.println(xmlString);
        } catch (Exception ex) {
            System.out.println("Failed at saveGame");
            System.out.println(ex.getMessage());
        }
    }

    @Override // android.app.Activity
    public void onResume() throws DOMException, TransformerFactoryConfigurationError, IllegalArgumentException {
        super.onResume();
        Decks.CARDS.printDecks();
        if (expeditionButton != null) {
            expeditionButton.setText("EXPEDITION [" + Decks.CARDS.getExpeditionLocation() + "]");
        }
        if (mysticRuinsButton != null && (Config.ANCIENT_ONE.equals("Syzygy") || Config.ANCIENT_ONE.equals("Antediluvium") || Config.COSMIC_ALIGNMENT)) {
            mysticRuinsButton.setText("Mystic Ruins [" + Decks.CARDS.getMysticRuinsLocation() + "]");
        }
        if (dreamQuestButton != null && (Config.ANCIENT_ONE.equals("Hypnos") || Config.DREAMLANDS_BOARD)) {
            dreamQuestButton.setText("Dream-Quest [" + Decks.CARDS.getDreamQuestLocation() + "]");
        }
        try {
            saveGame();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_eldritch_companion, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_end_game /* 2130968577 */:
                File file = new File(getFilesDir(), "discard.xml");
                file.delete();
                super.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
