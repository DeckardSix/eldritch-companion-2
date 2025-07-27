package pqt.eldritch.GUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
public class DeckGallery extends FragmentActivity implements ViewPager.OnPageChangeListener {
    public ViewPager gallery;

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create layout programmatically
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.MATCH_PARENT));
        
        // Create ViewPager programmatically
        ViewPager viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewpager);
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.MATCH_PARENT));
        mainLayout.addView(viewPager);
        
        setContentView(mainLayout);
        initialisePaging();
    }

    private void initialisePaging() {
        List<Fragment> fragments = new ArrayList<>();
        List<Card> deck = Decks.CARDS.getDeck(getIntent().getStringExtra("DECK"));
        if (deck == null || deck.isEmpty()) {
            Toast.makeText(getApplicationContext(), getIntent().getStringExtra("DECK") + " Deck is Empty.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        for (Card card : deck) {
            fragments.add(new CardView().newInstance(card));
        }
        this.gallery = (ViewPager) findViewById(R.id.viewpager);
        this.gallery.setAdapter(new CardPagerAdapter(super.getSupportFragmentManager(), fragments));
        this.gallery.setPageTransformer(true, new DepthPageTransformer());
        this.gallery.setOnPageChangeListener(this);
        onPageSelected(0);
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        setTitle(Decks.CARDS.getRegion(getIntent().getStringExtra("DECK"), i));
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    private class CardPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public CardPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.fragments.size();
        }
    }

    private class DepthPageTransformer implements ViewPager.PageTransformer {
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

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_actions, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discard_card /* 2130968576 */:
                FragmentPagerAdapter myAdapter = (FragmentPagerAdapter) this.gallery.getAdapter();
                CardView card = (CardView) myAdapter.getItem(this.gallery.getCurrentItem());
                Decks.CARDS.discardCard(card.getRegion(), card.getID(), null);
                super.finish();
                return true;
            case R.id.action_end_game /* 2130968577 */:
            case R.id.action_remove_card /* 2130968578 */:
            default:
                return super.onOptionsItemSelected(item);
            case R.id.action_shuffle_deck /* 2130968579 */:
                Decks.CARDS.shuffleFullDeck(getIntent().getStringExtra("DECK"));
                super.finish();
                return true;
        }
    }
}
