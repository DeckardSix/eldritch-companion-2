package pqt.eldritch.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v4.widget.CompoundButtonCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import pqt.eldritch.Card;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class RemoveExpedition extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create layout programmatically
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(50, 50, 50, 50);
        
        // Current location label
        TextView currentLabel = new TextView(this);
        String currentLocation = (Decks.CARDS != null && Decks.CARDS.getExpeditionLocation() != null) ? 
            Decks.CARDS.getExpeditionLocation() : "EMPTY";
        currentLabel.setText("Current: " + currentLocation);
        currentLabel.setTextSize(20);
        currentLabel.setTextColor(android.graphics.Color.WHITE);
        currentLabel.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(currentLabel);
        
        // Radio group for locations
        RadioGroup group = new RadioGroup(this);
        group.setId(R.id.expeditionGroup);
        group.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = width / dens;
        double hi = height / dens;
        double x = Math.pow(wi, 2.0d);
        double y = Math.pow(hi, 2.0d);
        double screenInches = Math.sqrt(x + y);
        
        List<String> locations = new ArrayList<>();
        if (Decks.CARDS != null && Decks.CARDS.getDeck("EXPEDITION") != null) {
            locations = new ArrayList<>(getLocations(Decks.CARDS.getDeck("EXPEDITION")));
        }
        for (int i = 0; i < locations.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(locations.get(i));
            button.setTextColor(android.graphics.Color.WHITE);
            CompoundButtonCompat.setButtonTintList(button, android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
            if (screenInches > 4.5d) {
                button.setTextSize(35.0f);
            }
            button.setId(i);
            group.addView(button);
        }
        if (Decks.CARDS != null && Decks.CARDS.getExpeditionLocation() != null) {
            group.check(locations.indexOf(Decks.CARDS.getExpeditionLocation()));
        }
        mainLayout.addView(group);
        
        // Remove button
        Button removeButton = new Button(this);
        removeButton.setText("Remove Expeditions");
        removeButton.setTextSize(20);
        removeButton.setTextColor(android.graphics.Color.WHITE);
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeExpeditions(v);
            }
        });
        mainLayout.addView(removeButton);
        
        setContentView(mainLayout);
    }

    private Set<String> getLocations(List<Card> expeditionDeck) {
        Set<String> locations = new TreeSet<>();
        for (Card card : expeditionDeck) {
            locations.add(card.topHeader);
        }
        return locations;
    }

    public void removeExpeditions(View view) {
        RadioGroup group = (RadioGroup) findViewById(R.id.expeditionGroup);
        if (group != null && group.getCheckedRadioButtonId() != -1) {
            RadioButton button = (RadioButton) findViewById(group.getCheckedRadioButtonId());
            if (button != null && Decks.CARDS != null) {
                Decks.CARDS.removeExpeditions((String) button.getText());
            }
        }
        finish();
    }
}
