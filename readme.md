# ⚡ EnerLyze – Smart Daily Energy Analyzer
### A Java Swing Desktop Application

---

## 📁 Project Structure

```
EnerLyze/
├── EnerLyzeApp.java      ← Main class + Swing GUI
├── EnergyRecord.java     ← Data model (OOP / Encapsulation)
├── EnergyEngine.java     ← Scoring logic & suggestions
├── FileManager.java      ← File I/O (save / load CSV)
└── README.md             ← This file
```

---

## 🚀 How to Compile & Run

Make sure you have **Java 8 or higher** installed.

### Step 1 — Open a terminal in the `EnerLyze/` folder

### Step 2 — Compile all Java files
```bash
javac *.java
```

### Step 3 — Run the application
```bash
java EnerLyzeApp
```

> On Windows you can also double-click `run.bat` if you create one (see below).

### Optional — run.bat (Windows)
Create a file `run.bat` in the same folder:
```bat
@echo off
javac *.java
java EnerLyzeApp
pause
```

---

## 🎮 How to Use

| Action | How |
|--------|-----|
| Analyze | Fill in Sleep, Work, Screen Time → click **⚡ Analyze** |
| Save | After analysis, click **💾 Save** to persist record to disk |
| Load History | Click **📂 Load History** to view all saved records |

---

## 📐 Java Concepts Covered

| Concept | Where |
|---------|-------|
| OOP — Classes & Objects | `EnergyRecord`, `EnergyEngine`, `FileManager` |
| Encapsulation | Private fields + getters/setters in `EnergyRecord` |
| Constructor | `EnergyRecord(...)` |
| Static methods | All methods in `EnergyEngine`, `FileManager` |
| if-else conditions | Score calculation, suggestions in `EnergyEngine` |
| Loops (for, enhanced for) | `calculateAverage()`, `loadRecords()`, `onLoadHistory()` |
| ArrayList (Collections) | `records` in `EnerLyzeApp` |
| String handling | `StringBuilder`, `String.format`, `.split()` |
| Exception handling | `try-catch` in `FileManager`, `onAnalyze`, `onSave` |
| File handling | `FileWriter`, `BufferedWriter`, `BufferedReader` in `FileManager` |
| Java Swing GUI | `JFrame`, `JTextField`, `JButton`, `JTextArea`, `JLabel`, `JSplitPane` |

---

## 🎨 UI Features

- **Dark theme** with indigo accent colours
- **Hover effects** on all buttons
- **Placeholder text** in input fields that clears on focus  
- **Dynamic score colour** — green (High), amber (Moderate), red (Low)  
- **Split-pane layout** — inputs on left, suggestions + history on right  
- **Scroll panes** on both history and suggestion panels

---

## 📊 Scoring Algorithm

| Factor | Max Points | Optimal Range |
|--------|-----------|---------------|
| Sleep  | 40 pts    | 7 – 9 hours   |
| Work   | 30 pts    | 6 – 8 hours   |
| Screen | 30 pts    | ≤ 2 hours     |
| **Total** | **100** | |

| Score Range | Status |
|-------------|--------|
| 70 – 100    | ⚡ High |
| 40 – 69     | 🔋 Moderate |
| 0 – 39      | 😴 Low |

---

## 💾 Data Storage

Records are saved to `energy_records.csv` in the same directory as the `.class` files.  
Format per line:
```
sleepHours,workHours,screenTime,energyScore,status,timestamp
7.5,8.0,3.0,72,High ⚡,2025-06-15 09:30
```
