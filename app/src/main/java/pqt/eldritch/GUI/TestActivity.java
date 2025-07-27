package pqt.eldritch.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import pqt.eldritch.DatabaseInitializer;

public class TestActivity extends Activity {
    private TextView statusText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create layout programmatically
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("Eldritch Companion - Database Tools");
        titleText.setTextSize(20);
        titleText.setPadding(0, 0, 0, 30);
        layout.addView(titleText);
        
        // Check Status Button
        Button checkStatusBtn = new Button(this);
        checkStatusBtn.setText("Check Database Status");
        checkStatusBtn.setOnClickListener(v -> checkDatabaseStatus());
        layout.addView(checkStatusBtn);
        
        // Initialize Database Button
        Button initDbBtn = new Button(this);
        initDbBtn.setText("Initialize Database");
        initDbBtn.setOnClickListener(v -> initializeDatabase());
        layout.addView(initDbBtn);
        
        // Force Re-initialize Button
        Button reinitDbBtn = new Button(this);
        reinitDbBtn.setText("Force Re-initialize Database");
        reinitDbBtn.setOnClickListener(v -> forceReinitializeDatabase());
        layout.addView(reinitDbBtn);
        
        // Test Database Button
        Button testDbBtn = new Button(this);
        testDbBtn.setText("Test Database");
        testDbBtn.setOnClickListener(v -> testDatabase());
        layout.addView(testDbBtn);
        
        // Status text area
        statusText = new TextView(this);
        statusText.setText("Ready to test database operations...");
        statusText.setTextSize(12);
        statusText.setPadding(0, 30, 0, 0);
        statusText.setBackground(getResources().getDrawable(android.R.drawable.edit_text));
        statusText.setPadding(20, 20, 20, 20);
        
        // Make it scrollable
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(statusText);
        layout.addView(scrollView);
        
        setContentView(layout);
        
        // Show initial status
        checkDatabaseStatus();
    }
    
    private void checkDatabaseStatus() {
        try {
            String status = DatabaseInitializer.getDatabaseStatus(this);
            statusText.setText("Database Status:\n\n" + status);
            Toast.makeText(this, "Status updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            statusText.setText("Error checking status: " + e.getMessage());
            Toast.makeText(this, "Error checking status", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void initializeDatabase() {
        try {
            statusText.setText("Initializing database...\n");
            boolean success = DatabaseInitializer.initializeDatabase(this, false);
            
            if (success) {
                String status = DatabaseInitializer.getDatabaseStatus(this);
                statusText.setText("Database initialized successfully!\n\n" + status);
                Toast.makeText(this, "Database initialized!", Toast.LENGTH_SHORT).show();
            } else {
                statusText.setText("Database initialization failed!");
                Toast.makeText(this, "Initialization failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            statusText.setText("Error during initialization: " + e.getMessage());
            Toast.makeText(this, "Error during initialization", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void forceReinitializeDatabase() {
        try {
            statusText.setText("Force re-initializing database...\n");
            boolean success = DatabaseInitializer.initializeDatabase(this, true);
            
            if (success) {
                String status = DatabaseInitializer.getDatabaseStatus(this);
                statusText.setText("Database re-initialized successfully!\n\n" + status);
                Toast.makeText(this, "Database re-initialized!", Toast.LENGTH_SHORT).show();
            } else {
                statusText.setText("Database re-initialization failed!");
                Toast.makeText(this, "Re-initialization failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            statusText.setText("Error during re-initialization: " + e.getMessage());
            Toast.makeText(this, "Error during re-initialization", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void testDatabase() {
        try {
            statusText.setText("Testing database...\n");
            String result = DatabaseInitializer.testDatabase(this);
            statusText.setText("Database Test Results:\n\n" + result);
            Toast.makeText(this, "Database test completed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            statusText.setText("Error during database test: " + e.getMessage());
            Toast.makeText(this, "Error during test", Toast.LENGTH_SHORT).show();
        }
    }
} 