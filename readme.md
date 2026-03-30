<div align="center">

# ⚡ EnerLyze
### Smart Daily Energy Analyzer
*A Java Swing Desktop Application*

> **Analyze your daily lifestyle habits and get an instant energy score — helping you understand how sleep, work, and screen time shape your productivity.**

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [How to Use](#-how-to-use)
- [Scoring Algorithm](#-scoring-algorithm)
- [UI Highlights](#-ui-highlights)
- [Java Concepts Covered](#-java-concepts-covered)
- [Class Architecture](#-class-architecture)
- [Data Storage](#-data-storage)
- [Troubleshooting](#-troubleshooting)
- [Future Scope](#-future-scope)

---

## 🌟 Overview

EnerLyze is a desktop productivity tool built entirely with **Java SE and Java Swing**. It takes three simple daily inputs — sleep hours, work hours, and screen time — and computes a personalized **Energy Score (0–100)**. Based on the score, it classifies your energy level, offers tailored suggestions, and lets you track your history over time.

It was built as a BYOP (Build Your Own Project) submission for the *Programming in Java* course, demonstrating all core Java concepts in a real-world, usable application.

---

## ✨ Features

| Feature | Description |
|---|---|
| ⚡ **Energy Score** | Instant 0–100 score based on your daily inputs |
| 🏷️ **Status Label** | High / Moderate / Low classification with colour coding |
| 💡 **Suggestions** | Personalized, parameter-specific improvement tips |
| 💾 **Save Records** | Persist sessions to a local CSV file |
| 📂 **Load History** | View all past records with timestamps |
| 📊 **Average Score** | Running average computed across all saved records |
| 📈 **Comparison** | See how today's score compares to your last session |
| 🎨 **Modern UI** | Dark-themed Swing GUI with hover effects and colour feedback |

---

## 📁 Project Structure

```
EnerLyze/
│
├── EnerLyzeApp.java        ← Main class — JFrame GUI & all event handlers
├── EnergyRecord.java       ← Data model — encapsulated fields, CSV serialisation
├── EnergyEngine.java       ← Pure logic — scoring, status, suggestions, analytics
├── FileManager.java        ← File I/O — save/load using FileWriter + BufferedReader
│
├── energy_records.csv      ← Auto-created on first save (do not edit manually)
└── README.md               ← This file
```

> **Note:** `energy_records.csv` is generated automatically when you first click **Save**. You do not need to create it manually.

---

## 🚀 Getting Started

### Prerequisites

- **Java 8 or higher** must be installed
- Verify your installation:
  ```bash
  java -version
  ```

---

### Option A — Command Line (All Platforms)

**Step 1 — Navigate to the project folder**
```bash
cd path/to/EnerLyze
```

**Step 2 — Compile all Java files**
```bash
javac *.java
```

**Step 3 — Run the application**
```bash
java EnerLyzeApp
```

---

### Option B — Windows Batch File

Create a `run.bat` file in the `EnerLyze/` folder with the following content:

```bat
@echo off
echo Compiling EnerLyze...
javac *.java
echo Launching EnerLyze...
java EnerLyzeApp
pause
```

Then simply **double-click `run.bat`** to compile and launch.

---

## 🎮 How to Use

```
┌─────────────────────────────────────────────────────────┐
│                      WORKFLOW                           │
│                                                         │
│  1. Enter Sleep Hours  (e.g. 7.5)                       │
│  2. Enter Work Hours   (e.g. 8.0)                       │
│  3. Enter Screen Time  (e.g. 3.0)                       │
│                                                         │
│  ⚡ Click ANALYZE  →  See your score + suggestions      │
│  💾 Click SAVE     →  Save this session to file         │
│  📂 Click LOAD     →  View all historical records       │
└─────────────────────────────────────────────────────────┘
```

### Button Reference

| Button | Action |
|--------|--------|
| ⚡ **Analyze** | Validates inputs, calculates score, displays suggestions |
| 💾 **Save** | Saves the current session as a record to `energy_records.csv` |
| 📂 **Load History** | Reads and displays all saved records + average score |

### Input Rules

| Field | Valid Range | Notes |
|-------|-------------|-------|
| Sleep Hours | `0.0 – 24.0` | Decimal values accepted (e.g. `7.5`) |
| Work Hours | `0.0 – 24.0` | Include all focused work/study time |
| Screen Time | `0.0 – 24.0` | All recreational screen usage outside of work |

> ⚠️ All three fields must be filled before clicking Analyze. Invalid fields are highlighted in red with a specific error message.

---

## 📊 Scoring Algorithm

EnerLyze uses a **100-point weighted scoring system** across three dimensions:

### Point Allocation

| Factor | Max Points | Optimal Range | Notes |
|--------|:----------:|:-------------:|-------|
| 🛌 Sleep Hours | **40 pts** | 7 – 9 hrs | Highest weight — most critical for recovery |
| 💼 Work Hours | **30 pts** | 6 – 8 hrs | Penalises overwork heavily beyond 12 hrs |
| 📱 Screen Time | **30 pts** | ≤ 2 hrs | Full points for minimal non-work screen use |
| **Total** | **100 pts** | | |

### Sleep Scoring Breakdown

| Sleep Duration | Points Awarded |
|---------------|:--------------:|
| 7 – 9 hours *(optimal)* | 40 |
| 6 – 7 hours | 30 |
| 9 – 10 hours | 30 |
| 5 – 6 hours | 20 |
| 4 – 5 hours | 10 |
| < 4 hours | 0 |

### Energy Status Classification

| Score Range | Status | Meaning |
|:-----------:|--------|---------|
| 70 – 100 | ⚡ **High** | Excellent habits — keep it up! |
| 40 – 69 | 🔋 **Moderate** | Decent, but room to improve |
| 0 – 39 | 😴 **Low** | Rest and rebalance needed |

---

## 🎨 UI Highlights

```
┌──────────────────────────────────────────────────────────────────┐
│  ⚡ EnerLyze       Smart Daily Energy Analyzer     📦 3 records  │
├────────────────────────────┬─────────────────────────────────────┤
│                            │                                     │
│  📋 Daily Inputs           │  💡 Personalised Suggestions        │
│  ┌──────────────────────┐  │  ┌─────────────────────────────┐    │
│  │ 🛌 Sleep Hours       │  │  │ ✅ Great sleep duration!    │    │
│  │ [  7.5             ] │  │  │ ⚠️  Slight overwork...      │    │
│  │ 💼 Work Hours        │  │  │ 📵 High screen time...      │    │
│  │ [  9.0             ] │  │  │ 🟡 Moderate energy today    │    │
│  │ 📱 Screen Time       │  │  └─────────────────────────────┘    │
│  │ [  5.0             ] │  │                                     │
│  └──────────────────────┘  │  📜 Session History                 │
│                            │  ┌─────────────────────────────┐    │
│  [⚡ Analyze][💾][📂]       │  │ #01 [2025-06-15] Score: 62  │    │
│                            │  │ #02 [2025-06-16] Score: 78  │    │
│  🎯 Energy Score           │  └─────────────────────────────┘    │
│         62                 │                                     │
│    🔋 Moderate             │  📊 Insights                        │
│                            │  📈 Average Score: 70.0 (2 recs)   │
│                            │  📉 Dropped by 8 pts vs last       │
└────────────────────────────┴─────────────────────────────────────┘
```

## 📐 Java Concepts Covered

| Concept | Where in Code |
|---------|--------------|
| **OOP — Classes & Objects** | `EnergyRecord`, `EnergyEngine`, `FileManager`, `EnerLyzeApp` |
| **Encapsulation** | Private fields + public getters/setters in `EnergyRecord` |
| **Constructor** | `EnergyRecord(sleepHours, workHours, screenTime, score, status, timestamp)` |
| **Static Methods** | All methods in `EnergyEngine` and `FileManager` |
| **if-else Conditions** | `calculateScore()` and `getSuggestions()` in `EnergyEngine` |
| **Enhanced for Loop** | `calculateAverage()` — iterates over `ArrayList<EnergyRecord>` |
| **Classic for Loop** | `onLoadHistory()` — index-based display with `#01`, `#02`... |
| **ArrayList (Collections)** | `records` field in `EnerLyzeApp` |
| **String Handling** | `StringBuilder`, `String.format()`, `.split(",", 6)`, `.trim()` |
| **Exception Handling** | `try-catch-finally` in `FileManager`, `onAnalyze()`, `onSave()` |
| **File Handling** | `FileWriter` + `BufferedWriter` (save), `FileReader` + `BufferedReader` (load) |
| **Java Swing GUI** | `JFrame`, `JTextField`, `JButton`, `JTextArea`, `JLabel`, `JSplitPane`, `JScrollPane` |
| **Event Listeners** | `ActionListener` on buttons, `FocusAdapter` on input fields |
| **Custom Painting** | `paintComponent()` override for hover states on buttons |

---

## 🏛️ Class Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                         EnerLyzeApp                              │
│            Main GUI — JFrame + all event handlers                │
│                                                                  │
│   onAnalyze() ──────────────────► EnergyEngine                  │
│                                   .calculateScore()              │
│                                   .getStatus()                   │
│                                   .getSuggestions()              │
│                                   .calculateAverage()            │
│                                   .compareWithLast()             │
│                                                                  │
│   onSave() / onLoad() ──────────► FileManager                   │
│                                   .saveRecords(ArrayList)        │
│                                   .loadRecords() → ArrayList     │
│                                                                  │
│   ArrayList<EnergyRecord> ──────► in-memory session store       │
└──────────────────────────────────────────────────────────────────┘

EnergyRecord  (Data Model)
├── sleepHours   : double     (private)
├── workHours    : double     (private)
├── screenTime   : double     (private)
├── energyScore  : int        (private)
├── status       : String     (private)
├── timestamp    : String     (private)
├── toCsvLine()  →  "7.5,8.0,3.0,72,High ⚡,2025-06-15 09:30"
└── fromCsvLine() ←  parses the above format back to an object
```

### Responsibility Summary

| Class | Single Responsibility |
|-------|----------------------|
| `EnerLyzeApp` | GUI construction, layout, event handling, user interaction |
| `EnergyRecord` | Stores one session's data; handles CSV serialisation |
| `EnergyEngine` | All calculation and suggestion logic — no GUI, no I/O |
| `FileManager` | All disk read/write operations — no GUI, no logic |

---

## 💾 Data Storage

Records are saved to **`energy_records.csv`** in the same directory as your compiled `.class` files.

### File Format

```
sleepHours,workHours,screenTime,energyScore,status,timestamp
```

### Example File Contents

```csv
7.5,8.0,3.0,72,High ⚡,2025-06-15 09:30
6.0,10.5,6.0,47,Moderate 🔋,2025-06-16 08:15
5.0,12.0,8.0,22,Low 😴,2025-06-17 07:45
8.0,7.0,2.0,92,High ⚡,2025-06-18 09:00
```

### Storage Rules

- Each record occupies exactly **one line**
- Fields are separated by **commas**; timestamp is always the last field
- The file is **fully overwritten** on every Save (not appended)
- Deleting the file is safe — the app creates a fresh one on next Save
- Manually editing the file may cause records to be skipped on load

---

## 🔧 Troubleshooting

| Problem | Likely Cause | Fix |
|---------|-------------|-----|
| `javac: command not found` | Java not installed or not in PATH | Install JDK 8+ and add to system PATH |
| `Error: Could not find or load main class` | Not compiled, or wrong directory | Run `javac *.java` first in the correct folder |
| Input field shows red border | Invalid or empty value entered | Enter a number between `0` and `24` |
| History shows "No records found" | No file saved yet | Click **Save** after an analysis to create the file |
| Emoji appears as `?` or blank | Older JDK or limited font support | Update to JDK 11+ or use a font that supports Unicode |
| Window is too small / clipped | Low display resolution | Resize window — minimum enforced at `820 × 640` |
| Score not updating after new input | Old analysis still shown | Click **⚡ Analyze** again after changing inputs |

---

## 🔭 Future Scope

- [ ] **Database support** — Replace CSV with SQLite via JDBC for richer querying and filtering
- [ ] **Charts & graphs** — Weekly/monthly energy trend visualisation using JFreeChart
- [ ] **Mobile version** — Android port using Kotlin with Material Design
- [ ] **Cloud sync** — Cross-device record synchronisation via REST API
- [ ] **Wearable integration** — Auto-populate sleep data from fitness tracker APIs
- [ ] **User profiles** — Multi-user support with secure login credentials
- [ ] **Dark / Light theme toggle** — User-selectable colour scheme at runtime
- [ ] **Export to PDF** — One-click printable daily or weekly energy report

---

## 📄 License

This project is released under the **MIT License** — free to use, modify, and distribute for personal or academic purposes.

---

<div align="center">

*Stay powered. Stay balanced. ⚡*

</div>

</div>

