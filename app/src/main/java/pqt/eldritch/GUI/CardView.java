package pqt.eldritch.GUI;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    public Fragment newInstance(Card card) {
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
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create layout programmatically
        LinearLayout mainLayout = new LinearLayout(getActivity());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/se-caslon-ant.ttf");
        
        // ID box
        TextView idBoxView = new TextView(getActivity());
        idBoxView.setText(this.ID);
        idBoxView.setTextSize(16);
        idBoxView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(idBoxView);
        
        // Top section
        TextView topNameView = new TextView(getActivity());
        topNameView.setText(this.topHeader);
        topNameView.setTypeface(font);
        topNameView.setTextSize(18);
        topNameView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(topNameView);
        
        TextView topEncounterView = new TextView(getActivity());
        topEncounterView.setText(formatText(this.topEncounter));
        topEncounterView.setTextSize(14);
        topEncounterView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        topEncounterView.setOnClickListener(this);
        mainLayout.addView(topEncounterView);
        
        // Middle section
        TextView middleNameView = new TextView(getActivity());
        middleNameView.setText(this.middleHeader);
        middleNameView.setTypeface(font);
        middleNameView.setTextSize(18);
        middleNameView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(middleNameView);
        
        TextView middleEncounterView = new TextView(getActivity());
        middleEncounterView.setText(formatText(this.middleEncounter));
        middleEncounterView.setTextSize(14);
        middleEncounterView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        middleEncounterView.setOnClickListener(this);
        mainLayout.addView(middleEncounterView);
        
        // Bottom section
        TextView bottomNameView = new TextView(getActivity());
        bottomNameView.setText(this.bottomHeader);
        bottomNameView.setTypeface(font);
        bottomNameView.setTextSize(18);
        bottomNameView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(bottomNameView);
        
        TextView bottomEncounterView = new TextView(getActivity());
        bottomEncounterView.setText(formatText(this.bottomEncounter));
        bottomEncounterView.setTextSize(14);
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
                topNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                topEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
                middleNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                middleEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
                bottomNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                bottomEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
            } else if (this.encountered.equals("top")) {
                topNameView.setBackgroundColor(0);
                topEncounterView.setBackgroundColor(0);
                middleNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                middleEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
                bottomNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                bottomEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
            } else if (this.encountered.equals("middle")) {
                topNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                topEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
                middleNameView.setBackgroundColor(0);
                middleEncounterView.setBackgroundColor(0);
                bottomNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                bottomEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
            } else if (this.encountered.equals("bottom")) {
                topNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                topEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
                middleNameView.setBackgroundColor(getResources().getColor(R.color.shaded));
                middleEncounterView.setBackgroundColor(getResources().getColor(R.color.shaded));
                bottomNameView.setBackgroundColor(0);
                bottomEncounterView.setBackgroundColor(0);
            }
        }
        return mainLayout;
    }

    private Spanned formatText(String text) {
        return Html.fromHtml(text.replace("[", "<b><font color='black'>[").replace("]", "]</font></b>"));
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
