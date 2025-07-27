package pqt.eldritch.GUI;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View view = inflater.inflate(R.layout.activity_card, container, false);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/se-caslon-ant.ttf");
        TextView idBoxView = (TextView) view.findViewById(R.id.idBox);
        idBoxView.setText(this.ID);
        TextView topNameView = (TextView) view.findViewById(R.id.topName);
        topNameView.setText(this.topHeader);
        topNameView.setTypeface(font);
        TextView topEncounterView = (TextView) view.findViewById(R.id.topEncounter);
        topEncounterView.setText(formatText(this.topEncounter));
        topEncounterView.setOnClickListener(this);
        TextView middleNameView = (TextView) view.findViewById(R.id.middleName);
        middleNameView.setText(this.middleHeader);
        middleNameView.setTypeface(font);
        TextView middleEncounterView = (TextView) view.findViewById(R.id.middleEncounter);
        middleEncounterView.setText(formatText(this.middleEncounter));
        middleEncounterView.setOnClickListener(this);
        TextView bottomNameView = (TextView) view.findViewById(R.id.bottomName);
        bottomNameView.setText(this.bottomHeader);
        bottomNameView.setTypeface(font);
        TextView bottomEncounterView = (TextView) view.findViewById(R.id.bottomEncounter);
        bottomEncounterView.setText(formatText(this.bottomEncounter));
        bottomEncounterView.setOnClickListener(this);
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
        return view;
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
