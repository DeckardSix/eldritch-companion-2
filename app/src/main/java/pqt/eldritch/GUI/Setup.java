package pqt.eldritch.GUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.graphics.PorterDuff;
import androidx.core.widget.CompoundButtonCompat;
import android.widget.SpinnerAdapter;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import pqt.eldritch.CardDatabaseHelper;
import pqt.eldritch.DatabaseInitializer;
import pqt.eldritch.Decks;
import pqt.eldritch.XMLToSQLiteMigration;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class Setup extends AppCompatActivity {
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
        
        // Use the XML layout instead of programmatic creation
        setContentView(R.layout.activity_setup);
        
        // Ensure ActionBar is properly displayed
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        
        // Set the font and styles for UI elements
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/se-caslon-ant.ttf");
        
        // Apply styles to existing checkboxes from XML
        setCheckboxWhite((CheckBox) findViewById(R.id.baseBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.forsakenLoreBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.mountainsOfMadnessBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.antarcticaBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.strangeRemnantsBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.cosmicAlignmentBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.underThePyramidsBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.egyptBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.litanyOfSecretsBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.signsOfCarcosaBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.theDreamlandsBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.dreamlandsBoardBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.citiesInRuinBox));
        setCheckboxWhite((CheckBox) findViewById(R.id.masksOfNyarlathotepBox));
        
        // Set font for headers
        TextView expanHeader = findViewById(R.id.expanHeader);
        TextView ancientHeader = findViewById(R.id.ancientHeader);
        expanHeader.setTypeface(font);
        ancientHeader.setTypeface(font);
        
        // Initialize data and populate spinner
        initializeData();
        populateSpinner();
    }

    private void initializeData() {
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
        // Use the custom spinner layout
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, ancientOnes);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
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
            
            // Check if we need to migrate from XML to SQLite
            try {
                CardDatabaseHelper dbHelper = CardDatabaseHelper.getInstance(this);
                Log.d("Setup", "Database helper created, checking if cards exist...");
                
                if (!dbHelper.hasCards()) {
                    Log.d("Setup", "No cards found in database, starting migration...");
                    // Perform migration from XML to SQLite
                    XMLToSQLiteMigration migration = new XMLToSQLiteMigration(this);
                    if (migration.performMigration()) {
                        Log.d("Setup", "Successfully migrated cards from XML to SQLite");
                        Log.d("Setup", migration.getMigrationStats());
                    } else {
                        Log.e("Setup", "Failed to migrate cards from XML to SQLite, falling back to XML");
                    }
                } else {
                    Log.d("Setup", "Database already contains cards, skipping migration");
                }
            } catch (Exception e) {
                Log.e("Setup", "Error during database initialization/migration: " + e.getMessage(), e);
                e.printStackTrace();
                // Continue with XML fallback
            }
            
            new Decks(this);
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
        Button continueButton = findViewById(R.id.continueButton);
        
        // Show/hide Continue button based on save file existence
        if (continueButton != null) {
            if (!file.exists()) {
                continueButton.setVisibility(View.GONE);
            } else {
                continueButton.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void setCheckboxWhite(CheckBox checkbox) {
        checkbox.setTextColor(android.graphics.Color.WHITE);
        CompoundButtonCompat.setButtonTintList(checkbox, android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
    }
    
    public void setupDatabase(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Database Setup");
        builder.setMessage("This will force re-initialize the card database from XML. This may take a few moments. Continue?");
        
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Show progress message
            Toast.makeText(this, "Initializing database...", Toast.LENGTH_SHORT).show();
            
            // Run database initialization in background thread
            new Thread(() -> {
                try {
                    Log.d("Setup", "Manual database setup requested");
                    boolean success = DatabaseInitializer.initializeDatabase(this, true);
                    
                    // Show result on UI thread
                    runOnUiThread(() -> {
                        if (success) {
                            String stats = DatabaseInitializer.getDatabaseStatus(this);
                            Log.d("Setup", "Manual database setup completed successfully");
                            Log.d("Setup", stats);
                            
                            AlertDialog.Builder resultBuilder = new AlertDialog.Builder(this);
                            resultBuilder.setTitle("Database Setup Complete");
                            resultBuilder.setMessage("Database successfully initialized!\n\n" + stats);
                            resultBuilder.setPositiveButton("OK", null);
                            resultBuilder.show();
                            
                            Toast.makeText(this, "Database setup completed!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("Setup", "Manual database setup failed");
                            AlertDialog.Builder errorBuilder = new AlertDialog.Builder(this);
                            errorBuilder.setTitle("Database Setup Failed");
                            errorBuilder.setMessage("Failed to initialize database. Please check logs for details.");
                            errorBuilder.setPositiveButton("OK", null);
                            errorBuilder.show();
                            
                            Toast.makeText(this, "Database setup failed!", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("Setup", "Error during manual database setup", e);
                    runOnUiThread(() -> {
                        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(this);
                        errorBuilder.setTitle("Database Setup Error");
                        errorBuilder.setMessage("Error during database setup: " + e.getMessage());
                        errorBuilder.setPositiveButton("OK", null);
                        errorBuilder.show();
                        
                        Toast.makeText(this, "Database setup error!", Toast.LENGTH_LONG).show();
                    });
                }
            }).start();
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        
        builder.show();
    }
}
