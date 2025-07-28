package pqt.eldritch.GUI;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import pqt.eldritch.Card;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class CardView extends Fragment implements View.OnClickListener {
    public String ID;
    public String bottomEncounter;
    public String bottomHeader;
    public String encountered;
    public String middleEncounter;
    public String middleHeader;
    public String region;
    public String topEncounter;
    public String topHeader;
    public String deckName;

    public Fragment newInstance(Card card, String deckName) {
        CardView cardFragment = new CardView();
        Bundle args = new Bundle();
        args.putString("REGION", card.region);
        args.putString("ID", card.ID);
        args.putString("TOP_HEADER", card.topHeader);
        args.putString("TOP_ENCOUNTER", card.topEncounter);
        args.putString("MIDDLE_HEADER", card.middleHeader);
        args.putString("MIDDLE_ENCOUNTER", card.middleEncounter);
        args.putString("BOTTOM_HEADER", card.bottomHeader);
        args.putString("BOTTOM_ENCOUNTER", card.bottomEncounter);
        args.putString("ENCOUNTERED", card.encountered);
        args.putString("DECK_NAME", deckName);
        cardFragment.setArguments(args);
        return cardFragment;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.region = getArguments().getString("REGION");
        this.ID = getArguments().getString("ID");
        this.topHeader = getArguments().getString("TOP_HEADER");
        this.topEncounter = getArguments().getString("TOP_ENCOUNTER");
        this.middleHeader = getArguments().getString("MIDDLE_HEADER");
        this.middleEncounter = getArguments().getString("MIDDLE_ENCOUNTER");
        this.bottomHeader = getArguments().getString("BOTTOM_HEADER");
        this.bottomEncounter = getArguments().getString("BOTTOM_ENCOUNTER");
        this.encountered = getArguments().getString("ENCOUNTERED");
        this.deckName = getArguments().getString("DECK_NAME");
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create a FrameLayout to layer the background image over the color
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        
        // Add ActionBar spacing to prevent overlap
        int actionBarHeight = getActionBarHeight();
        frameLayout.setPadding(0, actionBarHeight, 0, 0); // Exact ActionBar height, no extra spacing
        
        // Set background color based on deck
        int backgroundColor = CardColorUtils.getDeckBackgroundColor(this.deckName);
        int textColor = CardColorUtils.getDeckTextColor(this.deckName);
        frameLayout.setBackgroundColor(backgroundColor);
        
        // Create background image view with 20% alpha for more transparency
        ImageView backgroundImage = new ImageView(getActivity());
        try {
            backgroundImage.setImageResource(R.drawable.img_encounter_front);
            backgroundImage.setScaleType(ImageView.ScaleType.FIT_XY); // Scale to fill
            backgroundImage.setAlpha(0.20f); // 20% alpha transparency (more transparent)
            android.util.Log.d("CardView", "Background image loaded successfully");
        } catch (Exception e) {
            android.util.Log.e("CardView", "Failed to load background image: " + e.getMessage());
            // Fallback to a simple colored background
            backgroundImage.setBackgroundColor(android.graphics.Color.parseColor("#33000000")); // 33% alpha black
        }
        backgroundImage.setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, 
            FrameLayout.LayoutParams.MATCH_PARENT));
        frameLayout.addView(backgroundImage);
        
        // Create main content layout
        LinearLayout mainLayout = new LinearLayout(getActivity());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(100, 100, 100, 20); // 100px left/right/top margins
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, 
            FrameLayout.LayoutParams.MATCH_PARENT));
        
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/se-caslon-ant.ttf");
        
        // ID box
        TextView idBoxView = new TextView(getActivity());
        idBoxView.setText(this.ID);
        idBoxView.setTextSize(16);
        idBoxView.setTextColor(textColor);
        idBoxView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(idBoxView);
        
        // Spacing after ID box
        View spacing1 = new View(getActivity());
        spacing1.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            20)); // 20dp spacing
        mainLayout.addView(spacing1);
        
        // Vertical divider line after ID box
        View divider1 = new View(getActivity());
        divider1.setBackgroundColor(textColor);
        divider1.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            2)); // 2dp thick line
        mainLayout.addView(divider1);
        
        // Add spacing after divider1
        TextView divider1Spacing = new TextView(getActivity());
        divider1Spacing.setText(" ");
        divider1Spacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            10)); // 10dp spacing
        mainLayout.addView(divider1Spacing);
        
        // Top section with horizontal layout for header and shuffle image
        LinearLayout topHeaderLayout = new LinearLayout(getActivity());
        topHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
        topHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        
        TextView topNameView = new TextView(getActivity());
        topNameView.setText(this.topHeader);
        topNameView.setTypeface(font);
        topNameView.setTextSize(36); // Doubled from 18
        topNameView.setTextColor(textColor);
        topNameView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT)); // Size based on text content
        topHeaderLayout.addView(topNameView);
        
        // Add 3 spaces before the shuffle image
        TextView topSpacing = new TextView(getActivity());
        topSpacing.setText(" "); // 3 spaces
        topSpacing.setTextSize(36); // Same size as header text
        topSpacing.setTextColor(textColor);
        topSpacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        topHeaderLayout.addView(topSpacing);
        
        // Shuffle image for top section
        ImageView topShuffleImage = new ImageView(getActivity());
        try {
            topShuffleImage.setImageResource(R.drawable.cardsshuffle);
            android.util.Log.d("CardView", "Top shuffle image loaded successfully");
        } catch (Exception e) {
            android.util.Log.e("CardView", "Failed to load top shuffle image: " + e.getMessage());
            // Fallback to a simple text
            topShuffleImage.setBackgroundColor(android.graphics.Color.GRAY);
        }
        // Set image height 33% bigger than text size (36sp * 1.33)
        int textSizeInPixels = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 
            36 * 1.33f, 
            getResources().getDisplayMetrics()
        );
        topShuffleImage.setLayoutParams(new LinearLayout.LayoutParams(
            textSizeInPixels, 
            textSizeInPixels));
        topShuffleImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        topShuffleImage.setClickable(true);
        topShuffleImage.setFocusable(true);
        topShuffleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Debug logging
                android.util.Log.d("CardView", "Top shuffle image clicked. DeckName: " + deckName);
                if (Decks.CARDS != null && deckName != null) {
                    Decks.CARDS.shuffleFullDeck(deckName);
                    android.util.Log.d("CardView", "Shuffle completed for deck: " + deckName);
                    // Close the activity like the top menu does
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else {
                    android.util.Log.e("CardView", "Cannot shuffle: Decks.CARDS=" + (Decks.CARDS != null) + ", deckName=" + deckName);
                }
            }
        });
        topHeaderLayout.addView(topShuffleImage);
        
        mainLayout.addView(topHeaderLayout);
        
        // Small spacing between header and encounter text
        View topHeaderSpacing = new View(getActivity());
        topHeaderSpacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            8)); // 8dp spacing
        mainLayout.addView(topHeaderSpacing);
        
        TextView topEncounterView = new TextView(getActivity());
        topEncounterView.setText(formatText(this.topEncounter));
        topEncounterView.setTextSize(14);
        topEncounterView.setTextColor(textColor);
        topEncounterView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        topEncounterView.setOnClickListener(this);
        mainLayout.addView(topEncounterView);
        
        // Spacing after top section (3x more space)
        View spacing2 = new View(getActivity());
        spacing2.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            60)); // 60dp spacing (3x more)
        mainLayout.addView(spacing2);
        
        // Vertical divider line after top section
        View divider2 = new View(getActivity());
        divider2.setBackgroundColor(textColor);
        divider2.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            2)); // 2dp thick line
        mainLayout.addView(divider2);
        
        // Add spacing after divider2
        TextView divider2Spacing = new TextView(getActivity());
        divider2Spacing.setText(" ");
        divider2Spacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            10)); // 10dp spacing
        mainLayout.addView(divider2Spacing);
        
        // Middle section with horizontal layout for header and shuffle image
        LinearLayout middleHeaderLayout = new LinearLayout(getActivity());
        middleHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
        middleHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        
        TextView middleNameView = new TextView(getActivity());
        middleNameView.setText(this.middleHeader);
        middleNameView.setTypeface(font);
        middleNameView.setTextSize(36); // Doubled from 18
        middleNameView.setTextColor(textColor);
        middleNameView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT)); // Size based on text content
        middleHeaderLayout.addView(middleNameView);
        
        // Add 3 spaces before the shuffle image
        TextView middleSpacing = new TextView(getActivity());
        middleSpacing.setText(" "); // 3 spaces
        middleSpacing.setTextSize(36); // Same size as header text
        middleSpacing.setTextColor(textColor);
        middleSpacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        middleHeaderLayout.addView(middleSpacing);
        
        // Shuffle image for middle section
        ImageView middleShuffleImage = new ImageView(getActivity());
        try {
            middleShuffleImage.setImageResource(R.drawable.cardsshuffle);
            android.util.Log.d("CardView", "Middle shuffle image loaded successfully");
        } catch (Exception e) {
            android.util.Log.e("CardView", "Failed to load middle shuffle image: " + e.getMessage());
            // Fallback to a simple text
            middleShuffleImage.setBackgroundColor(android.graphics.Color.GRAY);
        }
        // Use the same size as top shuffle image (already calculated as 33% bigger)
        middleShuffleImage.setLayoutParams(new LinearLayout.LayoutParams(
            textSizeInPixels, 
            textSizeInPixels));
        middleShuffleImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        middleShuffleImage.setClickable(true);
        middleShuffleImage.setFocusable(true);
        middleShuffleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Debug logging
                android.util.Log.d("CardView", "Middle shuffle image clicked. DeckName: " + deckName);
                if (Decks.CARDS != null && deckName != null) {
                    Decks.CARDS.shuffleFullDeck(deckName);
                    android.util.Log.d("CardView", "Shuffle completed for deck: " + deckName);
                    // Close the activity like the top menu does
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else {
                    android.util.Log.e("CardView", "Cannot shuffle: Decks.CARDS=" + (Decks.CARDS != null) + ", deckName=" + deckName);
                }
            }
        });
        middleHeaderLayout.addView(middleShuffleImage);
        
        mainLayout.addView(middleHeaderLayout);
        
        // Small spacing between header and encounter text
        View middleHeaderSpacing = new View(getActivity());
        middleHeaderSpacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            8)); // 8dp spacing
        mainLayout.addView(middleHeaderSpacing);
        
        TextView middleEncounterView = new TextView(getActivity());
        middleEncounterView.setText(formatText(this.middleEncounter));
        middleEncounterView.setTextSize(14);
        middleEncounterView.setTextColor(textColor);
        middleEncounterView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        middleEncounterView.setOnClickListener(this);
        mainLayout.addView(middleEncounterView);
        
        // Spacing after middle section (3x more space)
        View spacing3 = new View(getActivity());
        spacing3.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            60)); // 60dp spacing (3x more)
        mainLayout.addView(spacing3);
        
        // Vertical divider line after middle section
        View divider3 = new View(getActivity());
        divider3.setBackgroundColor(textColor);
        divider3.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            2)); // 2dp thick line
        mainLayout.addView(divider3);
        
        // Add spacing after divider3
        TextView divider3Spacing = new TextView(getActivity());
        divider3Spacing.setText(" ");
        divider3Spacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            10)); // 10dp spacing
        mainLayout.addView(divider3Spacing);
        
        // Bottom section with horizontal layout for header and shuffle image
        LinearLayout bottomHeaderLayout = new LinearLayout(getActivity());
        bottomHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        
        TextView bottomNameView = new TextView(getActivity());
        bottomNameView.setText(this.bottomHeader);
        bottomNameView.setTypeface(font);
        bottomNameView.setTextSize(36); // Doubled from 18
        bottomNameView.setTextColor(textColor);
        bottomNameView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT)); // Size based on text content
        bottomHeaderLayout.addView(bottomNameView);
        
        // Add 3 spaces before the shuffle image
        TextView bottomSpacing = new TextView(getActivity());
        bottomSpacing.setText(" "); // 3 spaces
        bottomSpacing.setTextSize(36); // Same size as header text
        bottomSpacing.setTextColor(textColor);
        bottomSpacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        bottomHeaderLayout.addView(bottomSpacing);
        
        // Shuffle image for bottom section
        ImageView bottomShuffleImage = new ImageView(getActivity());
        try {
            bottomShuffleImage.setImageResource(R.drawable.cardsshuffle);
            android.util.Log.d("CardView", "Bottom shuffle image loaded successfully");
        } catch (Exception e) {
            android.util.Log.e("CardView", "Failed to load bottom shuffle image: " + e.getMessage());
            // Fallback to a simple text
            bottomShuffleImage.setBackgroundColor(android.graphics.Color.GRAY);
        }
        // Use the same size as top shuffle image (already calculated as 33% bigger)
        bottomShuffleImage.setLayoutParams(new LinearLayout.LayoutParams(
            textSizeInPixels, 
            textSizeInPixels));
        bottomShuffleImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bottomShuffleImage.setClickable(true);
        bottomShuffleImage.setFocusable(true);
        bottomShuffleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Debug logging
                android.util.Log.d("CardView", "Bottom shuffle image clicked. DeckName: " + deckName);
                if (Decks.CARDS != null && deckName != null) {
                    Decks.CARDS.shuffleFullDeck(deckName);
                    android.util.Log.d("CardView", "Shuffle completed for deck: " + deckName);
                    // Close the activity like the top menu does
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else {
                    android.util.Log.e("CardView", "Cannot shuffle: Decks.CARDS=" + (Decks.CARDS != null) + ", deckName=" + deckName);
                }
            }
        });
        bottomHeaderLayout.addView(bottomShuffleImage);
        
        mainLayout.addView(bottomHeaderLayout);
        
        // Small spacing between header and encounter text
        View bottomHeaderSpacing = new View(getActivity());
        bottomHeaderSpacing.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            8)); // 8dp spacing
        mainLayout.addView(bottomHeaderSpacing);
        
        TextView bottomEncounterView = new TextView(getActivity());
        bottomEncounterView.setText(formatText(this.bottomEncounter));
        bottomEncounterView.setTextSize(14);
        bottomEncounterView.setTextColor(textColor);
        bottomEncounterView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        bottomEncounterView.setOnClickListener(this);
        mainLayout.addView(bottomEncounterView);
        
        // Handle encounter states
        if (this.encountered == null || !this.encountered.equals("NONE")) {
            topEncounterView.setSoundEffectsEnabled(false);
            middleEncounterView.setSoundEffectsEnabled(false);
            bottomEncounterView.setSoundEffectsEnabled(false);
        }
        if (middleNameView.getText().equals("PASS")) {
            topEncounterView.setSoundEffectsEnabled(false);
        }
        if (this.encountered != null) {
            if (this.encountered.equals("removed")) {
                topNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                topEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                middleNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                middleEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                bottomNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                bottomEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
            } else if (this.encountered.equals("top")) {
                topNameView.setBackgroundColor(0);
                topEncounterView.setBackgroundColor(0);
                middleNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                middleEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                bottomNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                bottomEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
            } else if (this.encountered.equals("middle")) {
                topNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                topEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                middleNameView.setBackgroundColor(0);
                middleEncounterView.setBackgroundColor(0);
                bottomNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                bottomEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
            } else if (this.encountered.equals("bottom")) {
                topNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                topEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                middleNameView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                middleEncounterView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shaded));
                bottomNameView.setBackgroundColor(0);
                bottomEncounterView.setBackgroundColor(0);
            }
        }
        
        // Add the main layout to the frame layout
        frameLayout.addView(mainLayout);
        
        return frameLayout;
    }

    private Spanned formatText(String text) {
        // Use the same text color as the rest of the card for consistency
        String textColorHex = String.format("#%06X", (0xFFFFFF & CardColorUtils.getDeckTextColor(this.deckName)));
        return HtmlCompat.fromHtml(text.replace("[", "<b><font color='" + textColorHex + "'>[").replace("]", "]</font></b>"), HtmlCompat.FROM_HTML_MODE_LEGACY);
    }

    /**
     * Helper method to get ActionBar height for proper spacing
     */
    private int getActionBarHeight() {
        int actionBarHeight = 0;
        try {
            TypedValue tv = new TypedValue();
            if (getActivity() != null && getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            // Fallback to standard ActionBar height if unable to get from theme
            if (actionBarHeight == 0) {
                actionBarHeight = (int) (56 * getResources().getDisplayMetrics().density); // 56dp in pixels
            }
        } catch (Exception e) {
            // Fallback height
            actionBarHeight = (int) (56 * getResources().getDisplayMetrics().density);
        }
        return actionBarHeight;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id;
        if (this.encountered != null && this.encountered.equals("NONE") && (id = v.getId()) != R.id.topName && id != R.id.middleName && id != R.id.bottomName && id != R.id.idBox) {
            String encounter = "NONE";
            switch (id) {
                case R.id.bottomEncounter /* 2130968589 */:
                    encounter = "bottom";
                    break;
                case R.id.middleEncounter /* 2130968617 */:
                    encounter = "middle";
                    break;
                case R.id.topEncounter /* 2130968633 */:
                    if (!((String) ((TextView) getActivity().findViewById(R.id.middleName)).getText()).equals("PASS")) {
                        encounter = "top";
                        break;
                    } else {
                        return;
                    }
            }
            Decks.CARDS.discardCard(this.region, this.ID, encounter);
            getActivity().finish();
        }
    }

    public String getRegion() {
        return this.region;
    }

    public String getID() {
        return this.ID;
    }
}
