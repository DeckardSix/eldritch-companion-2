package pqt.eldritch.GUI;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.widget.CompoundButtonCompat;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import pqt.eldritch.Card;
import pqt.eldritch.Config;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class RemoveExpedition extends AppCompatActivity {
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Use the XML layout instead of programmatic creation
        setContentView(R.layout.activity_remove_expedition);
        
        // Ensure ActionBar is properly displayed
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Current location label
        TextView currentLabel = (TextView) findViewById(R.id.currentLocationLabel);
        String currentLocation = (Decks.CARDS != null && Decks.CARDS.getExpeditionLocation() != null) ? 
            Decks.CARDS.getExpeditionLocation() : "EMPTY";
        currentLabel.setText("Current: " + currentLocation);
        currentLabel.setTextSize(20);
        currentLabel.setTextColor(android.graphics.Color.WHITE);
        
        // Radio group for locations
        RadioGroup group = (RadioGroup) findViewById(R.id.expeditionGroup);
        
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            dm = getResources().getDisplayMetrics();
        } else {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
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
        
        // Remove button
        Button removeButton = (Button) findViewById(R.id.removeExpeditionsButton);
        removeButton.setText("Remove Expeditions");
        removeButton.setTextSize(20);
        removeButton.setTextColor(android.graphics.Color.WHITE);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeExpeditions(v);
            }
        });
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
