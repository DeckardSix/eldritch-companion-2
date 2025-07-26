package pqt.eldritch.GUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class DiscardGallery extends DeckGallery {
    @Override // pqt.eldritch.GUI.DeckGallery, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // pqt.eldritch.GUI.DeckGallery, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discard_pile, menu);
        return true;
    }

    @Override // pqt.eldritch.GUI.DeckGallery, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_card /* 2130968578 */:
                Decks.CARDS.removeCardFromDiscard(this.gallery.getCurrentItem());
                super.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
