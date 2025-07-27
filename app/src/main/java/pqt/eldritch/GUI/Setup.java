package pqt.eldritch.GUI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pqt.eldritch.Config;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class Setup extends Activity {
    public List<String> base;
    public List<String> citiesInRuin;
    public List<String> forsakenLore;
    public List<String> masksOfNyarlathotep;
    public List<String> mountainsOfMadness;
    public List<String> signsOfCarcosa;
    public List<String> strangeRemnants;
    public List<String> theDreamlands;
    public List<String> underThePyramids;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a functional setup layout programmatically
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        
        // Add eldritch horror image at the top
        ImageView eldritchHorrorImage = new ImageView(this);
        eldritchHorrorImage.setImageResource(R.drawable.eldritch_horror);
        eldritchHorrorImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        eldritchHorrorImage.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        
        // Add 20sp spacing below the image
        int spacingInPixels = (int) (20 * getResources().getDisplayMetrics().scaledDensity);
        eldritchHorrorImage.setPadding(0, 0, 0, spacingInPixels);
        layout.addView(eldritchHorrorImage);
        
        // Title
        TextView title = new TextView(this);
        title.setText("Select Expansions");
        title.setTextSize(25);
        title.setGravity(android.view.Gravity.CENTER);
        title.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(title);
        
        // Checkboxes for expansions
        CheckBox baseBox = new CheckBox(this);
        baseBox.setText("Base");
        baseBox.setChecked(true);
        baseBox.setTextSize(20);
        baseBox.setId(R.id.baseBox);
        baseBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSpinner();
            }
        });
        layout.addView(baseBox);
        
        CheckBox forsakenLoreBox = new CheckBox(this);
        forsakenLoreBox.setText("Forsaken Lore");
        forsakenLoreBox.setTextSize(20);
        forsakenLoreBox.setId(R.id.forsakenLoreBox);
        forsakenLoreBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSpinner();
            }
        });
        layout.addView(forsakenLoreBox);
        
        CheckBox mountainsOfMadnessBox = new CheckBox(this);
        mountainsOfMadnessBox.setText("Mountains of Madness");
        mountainsOfMadnessBox.setTextSize(20);
        mountainsOfMadnessBox.setId(R.id.mountainsOfMadnessBox);
        mountainsOfMadnessBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAntarctica(null);
            }
        });
        layout.addView(mountainsOfMadnessBox);
        
        CheckBox antarcticaBox = new CheckBox(this);
        antarcticaBox.setText("Antarctica");
        antarcticaBox.setTextSize(20);
        antarcticaBox.setId(R.id.antarcticaBox);
        antarcticaBox.setEnabled(false);
        layout.addView(antarcticaBox);
        
        CheckBox strangeRemnantsBox = new CheckBox(this);
        strangeRemnantsBox.setText("Strange Remnants");
        strangeRemnantsBox.setTextSize(20);
        strangeRemnantsBox.setId(R.id.strangeRemnantsBox);
        strangeRemnantsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCosmicAlignment(null);
            }
        });
        layout.addView(strangeRemnantsBox);
        
        CheckBox cosmicAlignmentBox = new CheckBox(this);
        cosmicAlignmentBox.setText("Cosmic Alignment - Prelude Card");
        cosmicAlignmentBox.setTextSize(20);
        cosmicAlignmentBox.setId(R.id.cosmicAlignmentBox);
        cosmicAlignmentBox.setEnabled(false);
        layout.addView(cosmicAlignmentBox);
        
        CheckBox underThePyramidsBox = new CheckBox(this);
        underThePyramidsBox.setText("Under the Pyramids");
        underThePyramidsBox.setTextSize(20);
        underThePyramidsBox.setId(R.id.underThePyramidsBox);
        underThePyramidsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEgyptAndLitany(null);
            }
        });
        layout.addView(underThePyramidsBox);
        
        CheckBox egyptBox = new CheckBox(this);
        egyptBox.setText("Egypt");
        egyptBox.setTextSize(20);
        egyptBox.setId(R.id.egyptBox);
        egyptBox.setEnabled(false);
        layout.addView(egyptBox);
        
        CheckBox litanyOfSecretsBox = new CheckBox(this);
        litanyOfSecretsBox.setText("Litany of Secrets - Prelude Card");
        litanyOfSecretsBox.setTextSize(20);
        litanyOfSecretsBox.setId(R.id.litanyOfSecretsBox);
        litanyOfSecretsBox.setEnabled(false);
        layout.addView(litanyOfSecretsBox);
        
        CheckBox signsOfCarcosaBox = new CheckBox(this);
        signsOfCarcosaBox.setText("Signs of Carcosa");
        signsOfCarcosaBox.setTextSize(20);
        signsOfCarcosaBox.setId(R.id.signsOfCarcosaBox);
        signsOfCarcosaBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSpinner();
            }
        });
        layout.addView(signsOfCarcosaBox);
        
        CheckBox theDreamlandsBox = new CheckBox(this);
        theDreamlandsBox.setText("The Dreamlands");
        theDreamlandsBox.setTextSize(20);
        theDreamlandsBox.setId(R.id.theDreamlandsBox);
        theDreamlandsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDreamlandsBoard(null);
            }
        });
        layout.addView(theDreamlandsBox);
        
        CheckBox dreamlandsBoardBox = new CheckBox(this);
        dreamlandsBoardBox.setText("Dreamlands Board");
        dreamlandsBoardBox.setTextSize(20);
        dreamlandsBoardBox.setId(R.id.dreamlandsBoardBox);
        dreamlandsBoardBox.setEnabled(false);
        layout.addView(dreamlandsBoardBox);
        
        CheckBox citiesInRuinBox = new CheckBox(this);
        citiesInRuinBox.setText("Cities in Ruin");
        citiesInRuinBox.setTextSize(20);
        citiesInRuinBox.setId(R.id.citiesInRuinBox);
        citiesInRuinBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSpinner();
            }
        });
        layout.addView(citiesInRuinBox);
        
        CheckBox masksOfNyarlathotepBox = new CheckBox(this);
        masksOfNyarlathotepBox.setText("Masks of Nyarlathotep");
        masksOfNyarlathotepBox.setTextSize(20);
        masksOfNyarlathotepBox.setId(R.id.masksOfNyarlathotepBox);
        masksOfNyarlathotepBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSpinner();
            }
        });
        layout.addView(masksOfNyarlathotepBox);
        
        // Ancient One selection title
        TextView ancientHeader = new TextView(this);
        ancientHeader.setText("Select Ancient One");
        ancientHeader.setTextSize(25);
        ancientHeader.setGravity(android.view.Gravity.CENTER);
        ancientHeader.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(ancientHeader);
        
        // Spinner for Ancient One selection
        Spinner spinner = new Spinner(this);
        spinner.setId(R.id.spinner);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(spinner);
        
        // Buttons
        Button startButton = new Button(this);
        startButton.setText("Start");
        startButton.setTextSize(25);
        startButton.setId(R.id.startButton);
        startButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startGame(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        layout.addView(startButton);
        
        Button continueButton = new Button(this);
        continueButton.setText("Continue");
        continueButton.setTextSize(25);
        continueButton.setId(R.id.continueButton);
        continueButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    continueGame(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        layout.addView(continueButton);
        
        scrollView.addView(layout);
        setContentView(scrollView);
        
        // Initialize the data structures
        populateAncientOnes();
        populateSpinner();
    }

    private void populateAncientOnes() {
        this.base = new ArrayList();
        this.base.add("Azathoth");
        this.base.add("Yog-Sothoth");
        this.base.add("Shub-Niggurath");
        this.base.add("Cthulhu");
        this.forsakenLore = new ArrayList();
        this.forsakenLore.add("Yig");
        this.mountainsOfMadness = new ArrayList();
        this.mountainsOfMadness.add("Rise of the Elder Things");
        this.mountainsOfMadness.add("Ithaqua");
        this.strangeRemnants = new ArrayList();
        this.strangeRemnants.add("Syzygy");
        this.underThePyramids = new ArrayList();
        this.underThePyramids.add("Abhoth");
        this.underThePyramids.add("Nephren-Ka");
        this.signsOfCarcosa = new ArrayList();
        this.signsOfCarcosa.add("Hastur");
        this.theDreamlands = new ArrayList();
        this.theDreamlands.add("Hypnos");
        this.theDreamlands.add("Atlach-Nacha");
        this.citiesInRuin = new ArrayList();
        this.citiesInRuin.add("Shudde M'ell");
        this.masksOfNyarlathotep = new ArrayList();
        this.masksOfNyarlathotep.add("Nyarlathotep");
        this.masksOfNyarlathotep.add("Antediluvium");
    }

    private List<String> getAncientOnes() {
        List<String> ancientOnes = new ArrayList<>();
        CheckBox box = (CheckBox) findViewById(R.id.baseBox);
        if (box.isChecked()) {
            ancientOnes.addAll(this.base);
        }
        CheckBox box2 = (CheckBox) findViewById(R.id.forsakenLoreBox);
        if (box2.isChecked()) {
            ancientOnes.addAll(this.forsakenLore);
        }
        CheckBox box3 = (CheckBox) findViewById(R.id.mountainsOfMadnessBox);
        if (box3.isChecked()) {
            ancientOnes.addAll(this.mountainsOfMadness);
        }
        CheckBox box4 = (CheckBox) findViewById(R.id.strangeRemnantsBox);
        if (box4.isChecked()) {
            ancientOnes.addAll(this.strangeRemnants);
        }
        CheckBox box5 = (CheckBox) findViewById(R.id.underThePyramidsBox);
        if (box5.isChecked()) {
            ancientOnes.addAll(this.underThePyramids);
        }
        CheckBox box6 = (CheckBox) findViewById(R.id.signsOfCarcosaBox);
        if (box6.isChecked()) {
            ancientOnes.addAll(this.signsOfCarcosa);
        }
        CheckBox box7 = (CheckBox) findViewById(R.id.theDreamlandsBox);
        if (box7.isChecked()) {
            ancientOnes.addAll(this.theDreamlands);
        }
        CheckBox box8 = (CheckBox) findViewById(R.id.citiesInRuinBox);
        if (box8.isChecked()) {
            ancientOnes.addAll(this.citiesInRuin);
        }
        CheckBox box9 = (CheckBox) findViewById(R.id.masksOfNyarlathotepBox);
        if (box9.isChecked()) {
            ancientOnes.addAll(this.masksOfNyarlathotep);
        }
        return ancientOnes;
    }

    private void populateSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String selected = (String) spinner.getSelectedItem();
        List<String> ancientOnes = getAncientOnes();
        ancientOnes.add(0, "Random");
        // ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, ancientOnes);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ancientOnes);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((SpinnerAdapter) spinnerArrayAdapter);
        if (ancientOnes.contains(selected)) {
            spinner.setSelection(ancientOnes.indexOf(selected));
        }
    }

    public void populateSpinner(View view) {
        populateSpinner();
    }

    public void toggleAntarctica(View view) {
        CheckBox antarctica = (CheckBox) findViewById(R.id.antarcticaBox);
        if (((CheckBox) findViewById(R.id.mountainsOfMadnessBox)).isChecked()) {
            antarctica.setEnabled(true);
        } else {
            antarctica.setChecked(false);
            antarctica.setEnabled(false);
        }
        populateSpinner();
    }

    public void toggleEgyptAndLitany(View view) {
        CheckBox egypt = (CheckBox) findViewById(R.id.egyptBox);
        CheckBox litanyOfSecrets = (CheckBox) findViewById(R.id.litanyOfSecretsBox);
        if (((CheckBox) findViewById(R.id.underThePyramidsBox)).isChecked()) {
            egypt.setEnabled(true);
            litanyOfSecrets.setEnabled(true);
        } else {
            egypt.setChecked(false);
            egypt.setEnabled(false);
            litanyOfSecrets.setChecked(false);
            litanyOfSecrets.setEnabled(false);
        }
        populateSpinner();
    }

    public void toggleDreamlandsBoard(View view) {
        CheckBox dreamlandsBoard = (CheckBox) findViewById(R.id.dreamlandsBoardBox);
        if (((CheckBox) findViewById(R.id.theDreamlandsBox)).isChecked()) {
            dreamlandsBoard.setEnabled(true);
        } else {
            dreamlandsBoard.setChecked(false);
            dreamlandsBoard.setEnabled(false);
        }
        populateSpinner();
    }

    public void toggleCosmicAlignment(View view) {
        CheckBox cosmicAlignment = (CheckBox) findViewById(R.id.cosmicAlignmentBox);
        if (((CheckBox) findViewById(R.id.strangeRemnantsBox)).isChecked()) {
            cosmicAlignment.setEnabled(true);
        } else {
            cosmicAlignment.setChecked(false);
            cosmicAlignment.setEnabled(false);
        }
        populateSpinner();
    }

    public void togglePreludeCards(View view) {
        CheckBox cosmicAlignment = (CheckBox) findViewById(R.id.cosmicAlignmentBox);
        CheckBox litanyOfSecrets = (CheckBox) findViewById(R.id.litanyOfSecretsBox);
        switch (view.getId()) {
            case R.id.cosmicAlignmentBox /* 2130968594 */:
                if (((CheckBox) findViewById(R.id.underThePyramidsBox)).isChecked()) {
                    litanyOfSecrets.setChecked(false);
                    break;
                }
                break;
            case R.id.litanyOfSecretsBox /* 2130968614 */:
                if (((CheckBox) findViewById(R.id.strangeRemnantsBox)).isChecked()) {
                    cosmicAlignment.setChecked(false);
                    break;
                }
                break;
        }
    }

    public void continueGame(View view) throws ParserConfigurationException, SAXException, IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(getFilesDir(), "discard.xml"));
            doc.getDocumentElement().normalize();
            Config.ANCIENT_ONE = getNodeText(getSubNode(doc.getDocumentElement(), "ANCIENT_ONE"));
            Config.BASE = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "BASE")));
            Config.FORSAKEN_LORE = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "FORSAKEN_LORE")));
            Config.MOUNTAINS_OF_MADNESS = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "MOUNTAINS_OF_MADNESS")));
            Config.STRANGE_REMNANTS = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "STRANGE_REMNANTS")));
            Config.ANTARCTICA = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "ANTARCTICA")));
            Config.COSMIC_ALIGNMENT = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "COSMIC_ALIGNMENT")));
            Config.UNDER_THE_PYRAMIDS = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "UNDER_THE_PYRAMIDS")));
            Config.EGYPT = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "EGYPT")));
            Config.LITANY_OF_SECRETS = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "LITANY_OF_SECRETS")));
            Config.SIGNS_OF_CARCOSA = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "SIGNS_OF_CARCOSA")));
            Config.THE_DREAMLANDS = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "THE_DREAMLANDS")));
            Config.DREAMLANDS_BOARD = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "DREAMLANDS_BOARD")));
            Config.CITIES_IN_RUIN = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "CITIES_IN_RUIN")));
            Config.MASKS_OF_NYARLATHOTEP = Boolean.parseBoolean(getNodeText(getSubNode(doc.getDocumentElement(), "MASKS_OF_NYARLATHOTEP")));
            new Decks();
            for (Node node = doc.getElementsByTagName("DISCARD_PILE").item(0).getLastChild(); node != null; node = node.getPreviousSibling()) {
                if (!node.getNodeName().equals("#text")) {
                    String deck = getNodeText(node.getAttributes().getNamedItem("region"));
                    String id = getNodeText(node.getAttributes().getNamedItem("id"));
                    String encountered = getNodeText(node.getAttributes().getNamedItem("encountered"));
                    Decks.CARDS.discardCard(deck, id, encountered);
                }
            }
            Intent intent = new Intent(this, (Class<?>) EldritchCompanion.class);
            startActivity(intent);
        } catch (Exception ex) {
            System.out.println("Failed at Loading File!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
            for (StackTraceElement x : ex.getStackTrace()) {
                System.out.println(x.toString());
            }
        }
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

    public void startGame(View view) throws IOException {
        boolean base = ((CheckBox) findViewById(R.id.baseBox)).isChecked();
        boolean forsakenLore = ((CheckBox) findViewById(R.id.forsakenLoreBox)).isChecked();
        boolean mountainsOfMadness = ((CheckBox) findViewById(R.id.mountainsOfMadnessBox)).isChecked();
        boolean antarctica = ((CheckBox) findViewById(R.id.antarcticaBox)).isChecked();
        boolean strangeRemnants = ((CheckBox) findViewById(R.id.strangeRemnantsBox)).isChecked();
        boolean cosmicAlignment = ((CheckBox) findViewById(R.id.cosmicAlignmentBox)).isChecked();
        boolean underThePyramids = ((CheckBox) findViewById(R.id.underThePyramidsBox)).isChecked();
        boolean egypt = ((CheckBox) findViewById(R.id.egyptBox)).isChecked();
        boolean litanyOfSecrets = ((CheckBox) findViewById(R.id.litanyOfSecretsBox)).isChecked();
        boolean signsOfCarcosa = ((CheckBox) findViewById(R.id.signsOfCarcosaBox)).isChecked();
        boolean theDreamlands = ((CheckBox) findViewById(R.id.theDreamlandsBox)).isChecked();
        boolean dreamlandsBoard = ((CheckBox) findViewById(R.id.dreamlandsBoardBox)).isChecked();
        boolean citiesInRuin = ((CheckBox) findViewById(R.id.citiesInRuinBox)).isChecked();
        boolean masksOfNyarlathotep = ((CheckBox) findViewById(R.id.masksOfNyarlathotepBox)).isChecked();
        if (!base && !forsakenLore && !mountainsOfMadness && !strangeRemnants && !underThePyramids && !signsOfCarcosa && !theDreamlands && !citiesInRuin && !masksOfNyarlathotep) {
            Toast.makeText(getApplicationContext(), "Choose At Least One Expansion", Toast.LENGTH_LONG).show();
            return;
        }
        String ANCIENT_ONE = (String) ((Spinner) findViewById(R.id.spinner)).getSelectedItem();
        if (ANCIENT_ONE.equals("Random")) {
            List<String> ancientOnes = getAncientOnes();
            Collections.shuffle(ancientOnes);
            ANCIENT_ONE = ancientOnes.get(0);
        }
        Config.ANCIENT_ONE = ANCIENT_ONE.replace(" ", "_").replace("'", ".");
        Config.BASE = base;
        Config.FORSAKEN_LORE = forsakenLore;
        Config.MOUNTAINS_OF_MADNESS = mountainsOfMadness;
        Config.ANTARCTICA = antarctica;
        Config.STRANGE_REMNANTS = strangeRemnants;
        Config.COSMIC_ALIGNMENT = cosmicAlignment;
        Config.UNDER_THE_PYRAMIDS = underThePyramids;
        Config.EGYPT = egypt;
        Config.LITANY_OF_SECRETS = litanyOfSecrets;
        Config.SIGNS_OF_CARCOSA = signsOfCarcosa;
        Config.THE_DREAMLANDS = theDreamlands;
        Config.DREAMLANDS_BOARD = dreamlandsBoard;
        Config.CITIES_IN_RUIN = citiesInRuin;
        Config.MASKS_OF_NYARLATHOTEP = masksOfNyarlathotep;
        new Decks();
        try {
            File file = new File(getFilesDir(), "discard.xml");
            file.createNewFile();
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + " - Unable to create save file");
        }
        Intent intent = new Intent(this, (Class<?>) EldritchCompanion.class);
        startActivity(intent);
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        File file = new File(getFilesDir(), "discard.xml");
        if (!file.exists()) {
            findViewById(R.id.continueButton).setVisibility(View.GONE);
        } else {
            findViewById(R.id.continueButton).setVisibility(View.VISIBLE);
        }
    }
}
