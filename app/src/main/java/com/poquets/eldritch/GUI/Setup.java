package com.poquets.eldritch.GUI;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.CompoundButtonCompat;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;

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
import com.poquets.eldritch.Config;
import com.poquets.eldritch.CardDatabaseHelper;
import com.poquets.eldritch.DatabaseInitializer;
import com.poquets.eldritch.Decks;
import com.poquets.eldritch.XMLToSQLiteMigration;
import com.poquets.eldritch.R;

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
        
        // Show disclaimer dialog on first launch
        showDisclaimerIfNeeded();
    }
    
    private void showDisclaimerIfNeeded() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean disclaimerShown = prefs.getBoolean("disclaimer_shown", false);
        
        if (!disclaimerShown) {
            // Create a scrollable TextView for the message to handle tablets better
            ScrollView scrollView = new ScrollView(this);
            // Semi-transparent dark background to match app theme while keeping readability
            scrollView.setBackgroundColor(0xE0000000); // 87.5% opacity black overlay
            
            // Create a LinearLayout to hold both message and checkbox
            LinearLayout contentLayout = new LinearLayout(this);
            contentLayout.setOrientation(LinearLayout.VERTICAL);
            
            TextView messageView = new TextView(this);
            messageView.setText(
                "⚠️ NOT AFFILIATED WITH FANTASY FLIGHT GAMES\n" +
                "This app is NOT affiliated with Fantasy Flight Games in any way.\n" +
                "Always refer to official Fantasy Flight Games materials.\n\n" +
                "⚠️ AGE LIMITATION - 14+\n" +
                "This app is recommended for ages 14+ as per the game publisher.\n" +
                "Content is based on H.P. Lovecraft Mythos (user discretion advised).\n\n" +
                "APP PURPOSE:\n" +
                "This app is designed to REPLACE physical cards to randomize cards and save table space.\n\n" +
                "⚠️ CRITICAL: In case of ANY doubt, the PHYSICAL CARDS are ALWAYS the truth.\n" +
                "The app may contain bugs, errors, or inaccuracies.\n\n" +
                "KEY INFORMATION:\n" +
                "• This app is FREE and in PERPETUAL BETA STATUS\n" +
                "• Cards are stored in a LOCAL DATABASE on your device only\n" +
                "• The database cannot be changed and requires NO user information\n" +
                "• The app does NOT save any user information\n" +
                "• The app does NOT offer any type of communication\n" +
                "• Works completely OFFLINE - no WiFi or network signal needed\n" +
                "• Language: English ONLY (regardless of your country/location)\n" +
                "• The app does NOT use your location\n\n" +
                "IMPORTANT:\n" +
                "⚠️ You must ALWAYS refer to original physical game materials if there is any doubt.\n" +
                "⚠️ Any issues with the app do NOT prevent you from using the real physical cards.\n" +
                "⚠️ The developer assumes NO LIABILITY for any issues, errors, or consequences.\n" +
                "⚠️ You use this app at your own risk.\n\n" +
                "By continuing, you acknowledge that you have read and agree to the full Privacy Policy & Terms of Service."
            );
            messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            messageView.setTextColor(0xFFFFFFFF); // White text to match app theme
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
            messageView.setPadding(padding, padding, padding, padding);
            
            // Create checkbox to save preference
            CheckBox dontShowAgainCheckbox = new CheckBox(this);
            dontShowAgainCheckbox.setText("Don't show this again");
            dontShowAgainCheckbox.setTextColor(0xFFFFFFFF); // White text
            dontShowAgainCheckbox.setPadding(padding, padding / 2, padding, padding);
            setCheckboxWhite(dontShowAgainCheckbox);
            
            contentLayout.addView(messageView);
            contentLayout.addView(dontShowAgainCheckbox);
            scrollView.addView(contentLayout);
            
            // Set maximum height for tablets (60% of screen height)
            int maxHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
            scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                maxHeight
            ));
            
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("⚠️ CRITICAL INFORMATION - READ FIRST");
            builder.setView(scrollView);
            builder.setPositiveButton("I Understand", (dialog, which) -> {
                // Only save preference if checkbox is checked
                if (dontShowAgainCheckbox.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("disclaimer_shown", true);
                    editor.apply();
                }
                dialog.dismiss();
            });
            builder.setCancelable(false);
            
            AlertDialog dialog = builder.create();
            dialog.show();
            
            // Set dialog background to match app theme (dark/transparent)
            if (dialog.getWindow() != null) {
                // Make dialog background transparent to show app background
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                
                // Ensure dialog doesn't take up entire screen on tablets
                int maxWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
                dialog.getWindow().setLayout(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                
                // Set dialog title text color to white to match app theme
                int titleId = getResources().getIdentifier("alertTitle", "id", "android");
                if (titleId != 0) {
                    TextView titleView = dialog.getWindow().getDecorView().findViewById(titleId);
                    if (titleView != null) {
                        titleView.setTextColor(0xFFFFFFFF); // White text
                    }
                }
            }
        }
    }

    private void initializeData() {
        this.base = new ArrayList<>();
        this.base.add("Azathoth");
        this.base.add("Yog-Sothoth");
        this.base.add("Shub-Niggurath");
        this.base.add("Cthulhu");
        this.forsakenLore = new ArrayList<>();
        this.forsakenLore.add("Yig");
        this.mountainsOfMadness = new ArrayList<>();
        this.mountainsOfMadness.add("Rise of the Elder Things");
        this.mountainsOfMadness.add("Ithaqua");
        this.strangeRemnants = new ArrayList<>();
        this.strangeRemnants.add("Syzygy");
        this.underThePyramids = new ArrayList<>();
        this.underThePyramids.add("Abhoth");
        this.underThePyramids.add("Nephren-Ka");
        this.signsOfCarcosa = new ArrayList<>();
        this.signsOfCarcosa.add("Hastur");
        this.theDreamlands = new ArrayList<>();
        this.theDreamlands.add("Hypnos");
        this.theDreamlands.add("Atlach-Nacha");
        this.citiesInRuin = new ArrayList<>();
        this.citiesInRuin.add("Shudde M'ell");
        this.masksOfNyarlathotep = new ArrayList<>();
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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, ancientOnes);
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

    public void startGame(View view) {
        try {
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
                Toast.makeText(this, "Choose At Least One Expansion", Toast.LENGTH_LONG).show();
                return;
            }
            
            String ANCIENT_ONE = (String) ((Spinner) findViewById(R.id.spinner)).getSelectedItem();
            if (ANCIENT_ONE == null || ANCIENT_ONE.isEmpty()) {
                Toast.makeText(this, "Please select an Ancient One", Toast.LENGTH_LONG).show();
                return;
            }
            
            if (ANCIENT_ONE.equals("Random")) {
                List<String> ancientOnes = getAncientOnes();
                if (ancientOnes.isEmpty()) {
                    Toast.makeText(this, "No Ancient Ones available for selected expansions", Toast.LENGTH_LONG).show();
                    return;
                }
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
            
            // Check if we need to migrate from XML to SQLite
            try {
                CardDatabaseHelper dbHelper = CardDatabaseHelper.getInstance(this);
                Log.d("Setup", "Database helper created, checking if cards exist...");
                
                if (!dbHelper.hasCards()) {
                    Log.d("Setup", "No cards found in database, starting migration...");
                    Toast.makeText(this, "Initializing card database...", Toast.LENGTH_SHORT).show();
                    // Perform migration from XML to SQLite
                    XMLToSQLiteMigration migration = new XMLToSQLiteMigration(this);
                    if (migration.performMigration()) {
                        Log.d("Setup", "Successfully migrated cards from XML to SQLite");
                        Log.d("Setup", migration.getMigrationStats());
                    } else {
                        Log.e("Setup", "Failed to migrate cards from XML to SQLite, falling back to XML");
                        Toast.makeText(this, "Warning: Database migration failed, using XML fallback", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("Setup", "Database already contains cards, skipping migration");
                }
            } catch (Exception e) {
                Log.e("Setup", "Error during database initialization/migration: " + e.getMessage(), e);
                e.printStackTrace();
                Toast.makeText(this, "Warning: Database error, using XML fallback", Toast.LENGTH_LONG).show();
                // Continue with XML fallback
            }
            
            // Use context-aware Decks constructor to load from database
            Log.d("Setup", "Loading decks...");
            new Decks(this);
            
            if (Decks.CARDS == null) {
                Toast.makeText(this, "Error: Failed to load card decks. Please try again.", Toast.LENGTH_LONG).show();
                Log.e("Setup", "Decks.CARDS is null after initialization");
                return;
            }
            
            try {
                File file = new File(getFilesDir(), "discard.xml");
                file.createNewFile();
            } catch (Exception ex) {
                Log.e("Setup", "Unable to create save file: " + ex.getMessage(), ex);
                System.out.println(ex.getMessage() + " - Unable to create save file");
            }
            
            Log.d("Setup", "Starting EldritchCompanion activity...");
            Intent intent = new Intent(this, (Class<?>) EldritchCompanion.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("Setup", "Error in startGame: " + e.getMessage(), e);
            e.printStackTrace();
            Toast.makeText(this, "Error starting game: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
