To open and run the entire **PersonaFit** system again, follow these quick steps:

---

### Step 1: Start XAMPP (MySQL Database & Apache)
1. Open the **XAMPP Control Panel** on your computer.
2. Click **Start** next to **Apache**.
3. Click **Start** next to **MySQL**.

---

### Step 2: Start the Laravel Backend Server
1. Open **PowerShell** or **Command Prompt (cmd)**.
2. Navigate to the backend folder:
   ```powershell
   cd C:\xampp\htdocs\persona-fit\persona-fit-backend
   ```
3. Start the server:
   ```powershell
   php artisan serve
   ```
   > 💡 Keep this terminal window open. You should see: `Server running on [http://127.0.0.1:8000]`.

---

### Step 3: Run the Android App in Android Studio
1. Open **Android Studio**.
2. Open the project at:
   `C:\xampp\htdocs\persona-fit\persona-fit-android`
3. Wait a moment for Gradle to finish indexing/syncing.
4. Select your device from the top toolbar:
   * **Android Emulator** (e.g. `Pixel 7 API 34`), **or**
   * **Your Physical Android Phone** (connected via USB).
5. Click the green **Run (▶)** button (or press `Shift + F10`).

---

### *(Optional)* Access the Web Admin Portal
If you want to view or manage exercises and injury rules on your computer browser:
* Open your browser and go to:
  [http://127.0.0.1:8000/admin](http://127.0.0.1:8000/admin) (or your configured admin route).