# Complete Guide: Running PersonaFit on a Real Physical Android Phone

There are **two reliable methods** to connect your physical phone to your local XAMPP/Laravel backend:

---

## Method 1: USB Cable with `adb reverse` (Fastest, No Wi-Fi config needed)

When testing on a real phone via USB, the phone can route requests through the USB cable directly to your PC's `localhost:8000`.

### Step 1: Enable USB Debugging on Your Phone
1. Open **Settings** on your phone.
2. Go to **About Phone** (on Samsung: *Software Information*).
3. Tap **Build Number** 7 times until you see *"You are now a developer!"*.
4. Go back to **Settings** ➔ **Developer Options**.
5. Enable **USB Debugging** (and on Xiaomi/Redmi/POCO, also enable *"Install via USB"*).
6. Connect phone to PC with a USB cable, unlock your phone, and tap **"Allow / Always Allow"** on the USB debugging popup dialog.

### Step 2: Fix the ADB Path & Port Forwarding Command
If typing `adb` in PowerShell gives `"adb is not recognized"`, use the direct path to Android SDK's `adb.exe`:

1. Open PowerShell or Command Prompt on your PC and run:
   ```powershell
   & "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" devices
   ```
   *(You should see your phone listed with `device` next to it).*

2. Run the reverse port forwarding command targeting your physical device (`-d` flag specifies physical device):
   ```powershell
   & "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -d reverse tcp:8000 tcp:8000
   ```
   *(If successful, it returns `8000` or blank without any error).*

3. **Update `ApiClient.kt` for USB Reverse**:
   In `persona-fit-android/app/src/main/java/com/personafit/app/data/remote/ApiClient.kt`:
   ```kotlin
   // For Physical Phone with 'adb reverse' OR Emulator:
   private const val DEFAULT_BASE_URL = "http://127.0.0.1:8000/api/"
   ```
   *(Note: `10.0.2.2` only works inside the emulator. On a real phone with `adb reverse`, use `127.0.0.1:8000` or `localhost:8000`).*

---

## Method 2: Wi-Fi Local Network IP (No ADB / No Cable needed after install)

If you don't want to use `adb reverse` or want to test wirelessly on your local Wi-Fi:

### Step 1: Find your PC's Local IPv4 Address
1. Open PowerShell and run:
   ```powershell
   ipconfig
   ```
2. Look for **IPv4 Address** under your Wi-Fi or Ethernet adapter (e.g. `192.168.1.15` or `192.168.0.102`).

### Step 2: Start Laravel Binding to `0.0.0.0`
Make sure Laravel listens to incoming requests from your local network (not just localhost):
```powershell
cd c:\xampp\htdocs\persona-fit\persona-fit-backend
php artisan serve --host=0.0.0.0 --port=8000
```

### Step 3: Update `ApiClient.kt` with Your PC's IP
In `persona-fit-android/app/src/main/java/com/personafit/app/data/remote/ApiClient.kt`:
```kotlin
private const val DEFAULT_BASE_URL = "http://YOUR_PC_IP:8000/api/" 
// Example: "http://192.168.1.15:8000/api/"
```
*(Make sure your phone and PC are connected to the same Wi-Fi network).*

---

## Step 4: Run the App from Android Studio

1. In the top toolbar of **Android Studio**, open the device dropdown.
2. Select your physical phone (e.g. *Samsung SM-G991B*, *Redmi Note 12*, *Pixel 7*, etc.).
3. Click the green **Run (▶)** button (or press `Shift + F10`).
4. Android Studio will install and launch **PersonaFit** directly on your physical phone!

---

## Troubleshooting Common Errors:

| Error Message in Terminal | Cause | Quick Fix |
|---|---|---|
| `'adb' is not recognized...` | ADB is not in Windows system PATH | Run `& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -d reverse tcp:8000 tcp:8000` |
| `error: more than one device/emulator` | Both emulator and real phone are open | Use the `-d` flag: `adb -d reverse tcp:8000 tcp:8000` |
| `error: device unauthorized` | Phone screen was locked or popup was dismissed | Unlock phone, unplug and re-plug USB cable, and tap **"Allow USB Debugging"** |
| `error: no devices/emulators found` | Phone is in "Charge Only" mode or USB debugging is disabled | Re-enable USB debugging in Developer Options, change USB mode to "File Transfer / MTP" |