package pqt.eldritch.GUI;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import pqt.eldritch.Card;
import pqt.eldritch.Decks;
import pqt.eldritch.R;

/* loaded from: classes.dex */
public class DeckGallery extends AppCompatActivity {
    public ViewPager2 gallery;
    private ViewPager2.OnPageChangeCallback pageChangeCallback;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String deckName = getIntent().getStringExtra("DECK");
        List<Card> cards = Decks.CARDS.getDeck(deckName);
        List<Fragment> fragments = new ArrayList<>();
        for (Card card : cards) {
            fragments.add(new CardView().newInstance(card, deckName));
        }
        this.gallery = (ViewPager2) findViewById(R.id.viewpager);
        this.gallery.setAdapter(new CardPagerAdapter(this, fragments));
        // ViewPager2 doesn't support setPageTransformer with boolean parameter
        this.gallery.setPageTransformer(new DepthPageTransformer());
        
        // Create the page change callback
        pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Empty implementation
            }

            @Override
            public void onPageSelected(int position) {
                String deckName = getIntent().getStringExtra("DECK");
                setTitle(Decks.CARDS.getRegion(deckName, position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Empty implementation
            }
        };
        
        this.gallery.registerOnPageChangeCallback(pageChangeCallback);
        // Trigger initial page selection
        if (pageChangeCallback != null) {
            pageChangeCallback.onPageSelected(0);
        }
    }

    private class CardPagerAdapter extends FragmentStateAdapter {
        private List<Fragment> fragments;

        CardPagerAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments) {
            super(fragmentActivity);
            this.fragments = fragments;
        }

        @Override
        public int getItemCount() {
            return this.fragments.size();
        }

        @Override
        public Fragment createFragment(int position) {
            return this.fragments.get(position);
        }

        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }
    }

    private class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        private DepthPageTransformer() {
        }

        @Override // android.support.v4.view.ViewPager.PageTransformer
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1.0f) {
                view.setAlpha(0.0f);
                return;
            }
            if (position <= 0.0f) {
                view.setAlpha(1.0f);
                view.setTranslationX(0.0f);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                return;
            }
            if (position <= 1.0f) {
                view.setAlpha(1.0f - position);
                view.setTranslationX(pageWidth * (-position));
                float scaleFactor = MIN_SCALE + (0.25f * (1.0f - Math.abs(position)));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                return;
            }
            view.setAlpha(0.0f);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        android.util.Log.d("DeckGallery", "Creating ActionBar menu...");
        
        // Add shuffle item with density-specific icon
        MenuItem shuffleItem = menu.add(Menu.NONE, R.id.action_shuffle_deck, Menu.NONE, "Shuffle");
        shuffleItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        shuffleItem.setIcon(R.drawable.ic_shuffle_actionbar);
        android.util.Log.d("DeckGallery", "Added shuffle item");

        // Add discard item with density-specific icon
        MenuItem discardItem = menu.add(Menu.NONE, R.id.action_discard_card, Menu.NONE, "Discard");
        discardItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        discardItem.setIcon(R.drawable.ic_discard_actionbar);
        android.util.Log.d("DeckGallery", "Added discard item");

        android.util.Log.d("DeckGallery", "Menu created successfully");
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        String deckName = getIntent().getStringExtra("DECK");
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the ActionBar back button
                finish();
                return true;
            case R.id.action_shuffle_deck:
                if (Decks.CARDS != null && deckName != null) {
                    Decks.CARDS.shuffleFullDeck(deckName);
                    super.finish();
                }
                return true;
            case R.id.action_discard_card:
                CardPagerAdapter myAdapter = (CardPagerAdapter) this.gallery.getAdapter();
                if (myAdapter != null) {
                    CardView card = (CardView) myAdapter.getItem(this.gallery.getCurrentItem());
                    if (Decks.CARDS != null && card != null) {
                        Decks.CARDS.discardCard(card.getRegion(), card.getID(), null);
                    }
                }
                super.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
