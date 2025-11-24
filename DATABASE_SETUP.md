# Database Setup Instructions

This project uses a pre-populated SQLite database to store all card data. The database can be included in the project assets for faster app startup.

## How It Works

1. **Pre-populated Database**: The database file `eldritch_cards.db` can be stored in `app/src/main/assets/databases/`
2. **Automatic Copy**: On first app launch, if the database exists in assets, it will be copied to the app's database directory
3. **Fallback to Migration**: If no pre-populated database is found, the app will automatically migrate from `cards.xml`

## Setting Up the Pre-populated Database

### Option 1: Using the App (Recommended)

1. Build and run the app on a device/emulator
2. Go to the Setup screen
3. Click "DB Setup" to ensure the database is populated
4. Click "Export DB to Project" button
5. The database will be exported to external storage
6. Copy the exported file from the device to:
   ```
   app/src/main/assets/databases/eldritch_cards.db
   ```

### Option 2: Manual Generation

1. Run the app and ensure the database is populated (via "DB Setup" button)
2. Use Android Studio's Device File Explorer or ADB to copy the database:
   ```bash
   adb pull /data/data/com.poquets.eldritch/databases/eldritch_cards.db app/src/main/assets/databases/eldritch_cards.db
   ```

### Option 3: Create Assets Directory Structure

1. Create the directories if they don't exist:
   ```
   app/src/main/assets/databases/
   ```
2. Place the `eldritch_cards.db` file in that directory

## Database Structure

The database contains a single table `cards` with the following columns:
- `_id`: Primary key (auto-increment)
- `card_id`: Unique card identifier (e.g., "A1", "G1")
- `region`: Card region/deck name (e.g., "AMERICAS", "GATES")
- `expansion`: Expansion name (e.g., "BASE", "FORSAKEN_LORE")
- `top_header`, `top_encounter`: Top section of card
- `middle_header`, `middle_encounter`: Middle section of card
- `bottom_header`, `bottom_encounter`: Bottom section of card
- `encountered`: Track which encounter was drawn ("NONE", "TOP", "MIDDLE", "BOTTOM")
- `card_name`: Optional card name for named cards

## Benefits of Pre-populated Database

- **Faster Startup**: No XML parsing on first launch
- **Version Control**: Database can be versioned with the project
- **Consistent Data**: All users get the same database structure
- **Offline Ready**: No need for migration step

## Notes

- The database file is typically 1-2 MB in size
- If the database in assets is outdated, users can use "DB Setup" to re-migrate
- The database version is tracked (currently version 2)
- Database upgrades are handled automatically via `onUpgrade()`


