# Drawable Organization

This document describes the reorganization of drawable resources using Android naming conventions.

## Organization Structure

### Background Files (bg_*)
XML files that define background drawables and gradients:
- `bg_alexandria.xml` - Alexandria region background
- `bg_asia.xml` - Asia region background
- `bg_celephais.xml` - Celephais region background
- `bg_city.xml` - City region background
- `bg_city_wilderness.xml` - City/Wilderness region background
- `bg_dreams_quest.xml` - Dreams Quest region background
- `bg_europe.xml` - Europe region background
- `bg_expedition.xml` - Expedition region background (renamed from expdedition)
- `bg_frozen_waste.xml` - Frozen Waste region background
- `bg_gate.xml` - Gate region background
- `bg_mystic_ruins.xml` - Mystic Ruins region background
- `bg_sahara.xml` - Sahara region background
- `bg_splash.xml` - Splash screen background
- `bg_test_*` - Test background files

### Action Icons (ic_*)
PNG files used for user interface actions:

#### Shuffle Icons:
- `ic_shuffle.png` - Main shuffle icon (145KB)
- `ic_shuffle_alt.png` - Alternative shuffle icon (851KB)
- `ic_shuffle_small.png` - Small shuffle icon (806B)
- `ic_shuffle_36x36.png` - 36x36 shuffle icon for hdpi
- `ic_shuffle_48x48.png` - 48x48 shuffle icon for xhdpi
- `ic_shuffle_50x50.png` - 50x50 shuffle icon
- `ic_shuffle_72x72.png` - 72x72 shuffle icon for xxhdpi

#### Discard Icons:
- `ic_discard.png` - Main discard icon (121KB)
- `ic_discard_alt.png` - Alternative discard icon (674KB)
- `ic_discard_36x36.png` - 36x36 discard icon for hdpi
- `ic_discard_48x48.png` - 48x48 discard icon for xhdpi
- `ic_discard_50x50.png` - 50x50 discard icon
- `ic_discard_72x72.png` - 72x72 discard icon for xxhdpi
- `ic_discard_75x75.png` - 75x75 discard icon
- `ic_discard_no_bg.png` - Discard icon without background
- `ic_discard_no_bg_white.png` - White discard icon without background

#### Other Icons:
- `ic_arrow_right.png` - Right arrow icon (renamed from end_right)

### Large Images (img_*)
PNG files for logos, backgrounds, and large graphics:
- `img_eldritch_horror.png` - Main Eldritch Horror logo (123KB)
- `img_cthulhu_background.png` - Cthulhu background image (244KB)
- `img_splash_bg.png` - Splash screen background image (244KB)
- `img_neighbourhood.png` - Neighbourhood image (12KB)
- `img_encounter_front.png` - Encounter card front (in drawable-hdpi)

### Button Styles (btn_*)
XML files that define button appearances:
- `btn_round.xml` - Round button style (renamed from round_button)

## Density-Specific ActionBar Icons

ActionBar icons are properly organized in density-specific folders:

### drawable-mdpi (baseline):
- `ic_shuffle_actionbar.png`
- `ic_discard_actionbar.png`

### drawable-hdpi (1.5x):
- `ic_shuffle_actionbar.png` (36x36)
- `ic_discard_actionbar.png` (36x36)

### drawable-xhdpi (2x):
- `ic_shuffle_actionbar.png` (48x48)
- `ic_discard_actionbar.png` (48x48)

### drawable-xxhdpi (3x):
- `ic_shuffle_actionbar.png` (72x72)
- `ic_discard_actionbar.png` (72x72)

## Benefits of This Organization

1. **Consistency**: Follows Android naming conventions
2. **Maintainability**: Easy to find files by purpose
3. **Scalability**: Clear pattern for adding new resources
4. **Professional**: Standard approach used in Android development
5. **Alphabetical Grouping**: Files are naturally grouped when sorted

## Code References Updated

All references in the following files have been updated:
- `app/src/main/res/layout/activity_setup.xml`
- `app/src/main/res/layout-large/activity_setup.xml`
- `app/src/main/java/pqt/eldritch/GUI/CardView.java`
- `app/src/main/java/pqt/eldritch/GUI/DeckGallery.java`
- All XML layout files (via batch replacement of round_button references) 