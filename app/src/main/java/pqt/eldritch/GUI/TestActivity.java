package pqt.eldritch.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a simple TextView instead of using a layout file
        TextView textView = new TextView(this);
        textView.setText("Eldritch Companion Test - App is working!");
        textView.setTextSize(20);
        textView.setPadding(50, 50, 50, 50);
        
        setContentView(textView);
    }
} 