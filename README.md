# PowerBubble

A lightweight Android floating bubble app that provides quick access to essential power controls and system actions right from your screen.

## Overview

PowerBubble creates a small, draggable floating bubble that stays on top of all your apps, giving you instant access to power controls without interrupting your workflow. Perfect for one-handed operation and quick system actions.

## Features

### 🔮 Floating Bubble Interface
- **Always Accessible**: Draggable floating bubble that stays on top of all apps
- **Auto-fade**: Bubble becomes semi-transparent after 2.5 seconds of inactivity
- **Smart Positioning**: Automatically positions popup menu based on bubble location

### 🔋 Power Controls
- **🔒 Screen Lock**: Instantly lock your device screen
- **⏻ Power Dialog**: Access Android's native power menu (shutdown, restart options)
- **🔊 Volume Control**: Quick access to volume adjustment

### ⚡ Quick Actions
- Tap the bubble to open the action menu
- Drag to reposition anywhere on screen
- Background dismissal by tapping outside the menu

## Requirements

- **Android 7.0** (API level 24) or higher
- **Android 14** (API level 36) recommended for best experience

## Permissions Required

PowerBubble requires several permissions to function properly:

### 🖥️ Overlay Permission
- **Purpose**: Display the floating bubble on top of other apps
- **Required**: Yes
- **How to Grant**: App will guide you to Android settings

### ♿ Accessibility Service
- **Purpose**: Perform system actions like screen lock and power dialog
- **Required**: Yes (for lock and power features)
- **How to Grant**: App will direct you to Accessibility settings

### 🛡️ Device Administrator
- **Purpose**: Enable screen lock functionality
- **Required**: Optional (only for screen lock feature)
- **How to Grant**: App will request device admin activation

## Installation

### From Source
1. Clone this repository:
   ```bash
   git clone https://github.com/Mahmadabid/PowerBubble.git
   ```

2. Open the project in Android Studio

3. Build and install:
   ```bash
   ./gradlew installDebug
   ```

### APK Installation
1. Download the latest APK from the [Releases](https://github.com/Mahmadabid/PowerBubble/releases) section
2. Enable "Install from unknown sources" in your device settings
3. Install the APK file

## Usage

### Initial Setup
1. **Launch PowerBubble** from your app drawer
2. **Grant Permissions**: Follow the in-app prompts to grant required permissions:
   - Tap "Request Overlay Permission" and allow the permission
   - Tap "Request Accessibility Permission" and enable PowerBubble in Accessibility settings
   - Optionally tap "Request Device Admin Access" for screen lock functionality
3. **Enable Bubble**: Toggle the "Enable Floating Bubble" switch

### Using the Bubble
1. **Access Menu**: Tap the floating bubble to open the action menu
2. **Available Actions**:
   - **🔒 Turn Off Screen**: Lock your device immediately
   - **🔊 Change Volume**: Open volume controls
   - **⏻ Power Off Features**: Access power dialog (shutdown, restart)
3. **Reposition**: Drag the bubble to any position on your screen
4. **Dismiss Menu**: Tap anywhere outside the menu to close it

### Managing Permissions
- **View Status**: The main app shows current permission status
- **Revoke All**: Use "Revoke All Permissions" button to quickly remove all granted permissions

## Technical Details

### Architecture
- **Language**: Java
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14)
- **Version**: 1.0

### Key Components
- **MainActivity**: Permission management and settings interface
- **BubbleService**: Foreground service managing the floating bubble
- **BubbleView**: Custom view rendering the draggable bubble
- **PopupMenuView**: Action menu displayed when bubble is tapped
- **BubbleAccessibilityService**: Accessibility service for system actions
- **MyDeviceAdminReceiver**: Device admin receiver for lock functionality

### Privacy & Security
- **No Network Access**: App works entirely offline
- **No Data Collection**: No user data is collected or transmitted
- **Minimal Permissions**: Only requests permissions necessary for functionality
- **Open Source**: Full source code available for review

## Troubleshooting

### Bubble Not Appearing
1. Ensure overlay permission is granted
2. Check if the bubble service is running
3. Try toggling the bubble switch off and on

### Screen Lock Not Working
1. Verify accessibility service is enabled
2. Grant device administrator permission
3. Check Android version compatibility (Android 9+ recommended for lock screen)

### Power Dialog Not Showing
1. Ensure accessibility service is enabled
2. Some device manufacturers may restrict this functionality
3. Try using the volume control feature instead

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is open source. See the repository for license details.

## Support

If you encounter any issues or have suggestions:
1. Check the [Issues](https://github.com/Mahmadabid/PowerBubble/issues) section
2. Create a new issue with detailed information about your problem
3. Include your Android version and device model

## Acknowledgments

- Built with Android's Accessibility Services API
- Uses Android's Window Manager for overlay functionality
- Inspired by the need for quick, accessible power controls

---

**Made with ❤️ for Android users who value efficiency and accessibility**