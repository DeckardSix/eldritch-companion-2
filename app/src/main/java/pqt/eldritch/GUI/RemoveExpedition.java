package pqt.eldritch.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
        setContentView(R.layout.activity_remove_expedition);
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
        List<String> locations = new ArrayList<>(getLocations(Decks.CARDS.getDeck("EXPEDITION")));
        ((TextView) findViewById(R.id.currentLabel)).setText("Current: " + Decks.CARDS.getExpeditionLocation());
        RadioGroup group = (RadioGroup) findViewById(R.id.expeditionGroup);
        for (int i = 0; i < locations.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(locations.get(i));
            if (screenInches > 4.5d) {
                button.setTextSize(35.0f);
            }
            button.setId(i);
            group.addView(button);
        }
        group.check(locations.indexOf(Decks.CARDS.getExpeditionLocation()));
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
        RadioButton button = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        Decks.CARDS.removeExpeditions((String) button.getText());
        finish();
    }
}
