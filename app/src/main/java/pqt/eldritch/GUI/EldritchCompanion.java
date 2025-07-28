package pqt.eldritch.GUI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
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
import pqt.eldritch.CardLoader;
import pqt.eldritch.Config;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class EldritchCompanion extends AppCompatActivity {
    private Button expeditionButton;
    private Button mysticRuinsButton;
    private Button dreamQuestButton;
    
    public void removeExpedition(View view) {
        if (Decks.CARDS == null || Decks.CARDS.getExpeditionLocation() == null || Decks.CARDS.getExpeditionLocation().equals("EMPTY")) {
            Toast.makeText(getApplicationContext(), "Expedition Deck is Empty.", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getApplicationContext(), (Class<?>) RemoveExpedition.class));
        }
    }

    public void drawCard(View view) {
        Intent intent = null;
        if (view.getId() == R.id.discardButton) {
            if (Decks.CARDS == null || Decks.CARDS.getDeck("DISCARD") == null || Decks.CARDS.getDeck("DISCARD").isEmpty()) {
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
        
        // Use the XML layout instead of programmatic creation
        setContentView(R.layout.activity_eldritch_companion);
        
        // Ensure ActionBar is properly displayed
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        
        // Get references to buttons from the XML layout
        expeditionButton = findViewById(R.id.expeditionButton);
        mysticRuinsButton = findViewById(R.id.mysticRuinsButton);
        dreamQuestButton = findViewById(R.id.dreamQuestButton);
        
        // Set click listeners for all buttons
        findViewById(R.id.americasButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard(v);
            }
        });
        
        findViewById(R.id.europeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard(v);
            }
        });
        
        findViewById(R.id.asiaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard(v);
            }
        });
        
        // Set click listeners for conditional buttons if they exist
        if (findViewById(R.id.antWestButton) != null) {
            findViewById(R.id.antWestButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.antEastButton) != null) {
            findViewById(R.id.antEastButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.africaButton) != null) {
            findViewById(R.id.africaButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.egyptButton) != null) {
            findViewById(R.id.egyptButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.dreamlandsButton) != null) {
            findViewById(R.id.dreamlandsButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        findViewById(R.id.generalButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard(v);
            }
        });
        
        if (findViewById(R.id.gateButton) != null) {
            findViewById(R.id.gateButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (expeditionButton != null) {
            expeditionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        Button removeButton = findViewById(R.id.removeButton);
        if (removeButton != null) {
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeExpedition(v);
                }
            });
        }
        
        if (mysticRuinsButton != null) {
            mysticRuinsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (dreamQuestButton != null) {
            dreamQuestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.researchButton) != null) {
            findViewById(R.id.researchButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.antResearchButton) != null) {
            findViewById(R.id.antResearchButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.special1Button) != null) {
            findViewById(R.id.special1Button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.special2Button) != null) {
            findViewById(R.id.special2Button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.special3Button) != null) {
            findViewById(R.id.special3Button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.disasterButton) != null) {
            findViewById(R.id.disasterButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.devastationButton) != null) {
            findViewById(R.id.devastationButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        if (findViewById(R.id.discardButton) != null) {
            findViewById(R.id.discardButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCard(v);
                }
            });
        }
        
        // Set button visibility based on Config settings and Decks state
        
        // Antarctica buttons - only visible if ANTARCTICA is enabled or specific Ancient One
        if (!(Config.ANTARCTICA || (Config.ANCIENT_ONE != null && Config.ANCIENT_ONE.equals("Rise_of_the_Elder_Things")))) {
            View antWestButton = findViewById(R.id.antWestButton);
            if (antWestButton != null && antWestButton.getParent() instanceof View) {
                ((View) antWestButton.getParent()).setVisibility(View.GONE);
            }
            View antEastButton = findViewById(R.id.antEastButton);
            if (antEastButton != null && antEastButton.getParent() instanceof View) {
                ((View) antEastButton.getParent()).setVisibility(View.GONE);
            }
            View antResearchButton = findViewById(R.id.antResearchButton);
            if (antResearchButton != null && antResearchButton.getParent() instanceof View) {
                ((View) antResearchButton.getParent()).setVisibility(View.GONE);
            }
        }
        
        // Egypt buttons - only visible if EGYPT is enabled or specific Ancient One
        if (!(Config.EGYPT || (Config.ANCIENT_ONE != null && Config.ANCIENT_ONE.equals("Nephren-Ka")))) {
            View africaButton = findViewById(R.id.africaButton);
            if (africaButton != null && africaButton.getParent() instanceof View) {
                ((View) africaButton.getParent()).setVisibility(View.GONE);
            }
            View egyptButton = findViewById(R.id.egyptButton);
            if (egyptButton != null && egyptButton.getParent() instanceof View) {
                ((View) egyptButton.getParent()).setVisibility(View.GONE);
            }
        }
        
        // Dreamlands buttons - only visible if DREAMLANDS_BOARD is enabled or specific Ancient One
        if (!(Config.DREAMLANDS_BOARD || (Config.ANCIENT_ONE != null && Config.ANCIENT_ONE.equals("Hypnos")))) {
            View dreamlandsButton = findViewById(R.id.dreamlandsButton);
            if (dreamlandsButton != null && dreamlandsButton.getParent() instanceof View) {
                ((View) dreamlandsButton.getParent()).setVisibility(View.GONE);
            }
            View dreamQuestButton = findViewById(R.id.dreamQuestButton);
            if (dreamQuestButton != null && dreamQuestButton.getParent() instanceof View) {
                ((View) dreamQuestButton.getParent()).setVisibility(View.GONE);
            }
        }
        
        // Mystic Ruins button - only visible if COSMIC_ALIGNMENT or specific Ancient Ones
        if (!(Config.COSMIC_ALIGNMENT || (Config.ANCIENT_ONE != null && (Config.ANCIENT_ONE.equals("Syzygy") || Config.ANCIENT_ONE.equals("Antediluvium"))))) {
            View mysticRuinsButton = findViewById(R.id.mysticRuinsButton);
            if (mysticRuinsButton != null && mysticRuinsButton.getParent() instanceof View) {
                ((View) mysticRuinsButton.getParent()).setVisibility(View.GONE);
            }
        }
        
        // Special buttons - only visible if the corresponding decks exist
        if (!(Decks.CARDS != null && Decks.CARDS.containsDeck("SPECIAL-1"))) {
            View special1Button = findViewById(R.id.special1Button);
            if (special1Button != null && special1Button.getParent() instanceof View) {
                ((View) special1Button.getParent()).setVisibility(View.GONE);
            }
        }
        if (!(Decks.CARDS != null && Decks.CARDS.containsDeck("SPECIAL-2"))) {
            View special2Button = findViewById(R.id.special2Button);
            if (special2Button != null && special2Button.getParent() instanceof View) {
                ((View) special2Button.getParent()).setVisibility(View.GONE);
            }
        }
        if (!(Decks.CARDS != null && Decks.CARDS.containsDeck("SPECIAL-3"))) {
            View special3Button = findViewById(R.id.special3Button);
            if (special3Button != null && special3Button.getParent() instanceof View) {
                ((View) special3Button.getParent()).setVisibility(View.GONE);
            }
        }
        
        // Disaster buttons - only visible if CITIES_IN_RUIN is enabled
        if (!Config.CITIES_IN_RUIN) {
            View disasterButton = findViewById(R.id.disasterButton);
            if (disasterButton != null && disasterButton.getParent() instanceof View) {
                ((View) disasterButton.getParent()).setVisibility(View.GONE);
            }
            View devastationButton = findViewById(R.id.devastationButton);
            if (devastationButton != null && devastationButton.getParent() instanceof View) {
                ((View) devastationButton.getParent()).setVisibility(View.GONE);
            }
        }
        
        if (Config.ANCIENT_ONE != null) {
            setTitle(Config.ANCIENT_ONE.replace("_", " ").replace(".", "'"));
        } else {
            setTitle("Eldritch Companion");
        }
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
            if (Decks.CARDS != null && Decks.CARDS.getDeck("DISCARD") != null) {
                for (Card card : Decks.CARDS.getDeck("DISCARD")) {
                    Element cardNode = documentNewDocument.createElement("CARD");
                    cardNode.setAttribute("region", card.region);
                    cardNode.setAttribute("id", card.ID);
                    if (card.encountered != null) {
                        cardNode.setAttribute("encountered", card.encountered);
                    }
                    discardPile.appendChild(cardNode);
                }
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
        if (Decks.CARDS != null) {
            Decks.CARDS.printDecks();
            if (expeditionButton != null) {
                expeditionButton.setText("EXPEDITION [" + Decks.CARDS.getExpeditionLocation() + "]");
            }
            if (mysticRuinsButton != null && (Config.ANCIENT_ONE != null && (Config.ANCIENT_ONE.equals("Syzygy") || Config.ANCIENT_ONE.equals("Antediluvium")) || Config.COSMIC_ALIGNMENT)) {
                mysticRuinsButton.setText("Mystic Ruins [" + Decks.CARDS.getMysticRuinsLocation() + "]");
            }
            if (dreamQuestButton != null && (Config.ANCIENT_ONE != null && Config.ANCIENT_ONE.equals("Hypnos") || Config.DREAMLANDS_BOARD)) {
                dreamQuestButton.setText("Dream-Quest [" + Decks.CARDS.getDreamQuestLocation() + "]");
            }
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
        // Temporarily disabled to fix menu parsing crash
        // getMenuInflater().inflate(R.menu.menu_eldritch_companion, menu);
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
