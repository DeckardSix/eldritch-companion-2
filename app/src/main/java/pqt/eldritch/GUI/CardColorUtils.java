package pqt.eldritch.GUI;

import android.graphics.Color;

public class CardColorUtils {
    
    // Color mapping for different deck types (matching the button colors from EldritchCompanion)
    private static final String AMERICAS_COLOR = "#ff00741e";
    private static final String EUROPE_COLOR = "#ffc96900";
    private static final String ASIA_COLOR = "#ff5a0e8a";
    private static final String ANT_WEST_COLOR = "#ffb897bb";
    private static final String ANT_EAST_COLOR = "#a1746100";
    private static final String AFRICA_COLOR = "#7b5000";
    private static final String EGYPT_COLOR = "#b60101";
    private static final String DREAMLANDS_COLOR = "#8caa2f";
    private static final String GENERAL_COLOR = "#5e000000";
    private static final String GATE_COLOR = "#fe00cd5b";
    private static final String RESEARCH_COLOR = "#ff000000";
    private static final String ANT_RESEARCH_COLOR = "#d98b4512";
    private static final String SPECIAL1_COLOR = "#d9578b84";
    private static final String SPECIAL2_COLOR = "#d92c8b5e";
    private static final String SPECIAL3_COLOR = "#d9075e35";
    private static final String DISASTER_COLOR = "#ff0000ff";
    private static final String DEVASTATION_COLOR = "#ffff0000";
    private static final String DISCARD_COLOR = "#ff3d4876";
    private static final String EXPEDITION_COLOR = "#ff1a0077";
    private static final String MYSTIC_RUINS_COLOR = "#a21d00f2";
    private static final String DREAM_QUEST_COLOR = "#5e30ad";
    
    /**
     * Get the background color for a specific deck
     */
    public static int getDeckBackgroundColor(String deckName) {
        if (deckName == null) {
            return Color.WHITE; // Default color
        }
        
        switch (deckName.toUpperCase()) {
            case "AMERICAS":
                return Color.parseColor(AMERICAS_COLOR);
            case "EUROPE":
                return Color.parseColor(EUROPE_COLOR);
            case "ASIA":
                return Color.parseColor(ASIA_COLOR);
            case "ANTARCTICA-WEST":
                return Color.parseColor(ANT_WEST_COLOR);
            case "ANTARCTICA-EAST":
                return Color.parseColor(ANT_EAST_COLOR);
            case "AFRICA":
                return Color.parseColor(AFRICA_COLOR);
            case "EGYPT":
                return Color.parseColor(EGYPT_COLOR);
            case "DREAMLANDS":
                return Color.parseColor(DREAMLANDS_COLOR);
            case "GENERAL":
                return Color.parseColor(GENERAL_COLOR);
            case "GATE":
                return Color.parseColor(GATE_COLOR);
            case "RESEARCH":
                return Color.parseColor(RESEARCH_COLOR);
            case "ANTARCTICA-RESEARCH":
                return Color.parseColor(ANT_RESEARCH_COLOR);
            case "SPECIAL-1":
                return Color.parseColor(SPECIAL1_COLOR);
            case "SPECIAL-2":
                return Color.parseColor(SPECIAL2_COLOR);
            case "SPECIAL-3":
                return Color.parseColor(SPECIAL3_COLOR);
            case "DISASTER":
                return Color.parseColor(DISASTER_COLOR);
            case "DEVASTATION":
                return Color.parseColor(DEVASTATION_COLOR);
            case "DISCARD":
                return Color.parseColor(DISCARD_COLOR);
            case "EXPEDITION":
                return Color.parseColor(EXPEDITION_COLOR);
            case "MYSTIC_RUINS":
                return Color.parseColor(MYSTIC_RUINS_COLOR);
            case "DREAM-QUEST":
                return Color.parseColor(DREAM_QUEST_COLOR);
            default:
                return Color.WHITE; // Default color
        }
    }
    
    /**
     * Calculate if text should be black or white based on background color brightness
     * Uses the luminance formula: 0.299*R + 0.587*G + 0.114*B
     */
    public static int getTextColorForBackground(int backgroundColor) {
        int red = Color.red(backgroundColor);
        int green = Color.green(backgroundColor);
        int blue = Color.blue(backgroundColor);
        
        // Calculate luminance
        double luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255.0;
        
        // Use white text for dark backgrounds, black text for light backgrounds
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }
    
    /**
     * Get text color for a specific deck
     */
    public static int getDeckTextColor(String deckName) {
        int backgroundColor = getDeckBackgroundColor(deckName);
        return getTextColorForBackground(backgroundColor);
    }
} 