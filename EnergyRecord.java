/**
 * EnergyRecord.java
 * Represents a single energy analysis record.
 * Demonstrates: OOP, Encapsulation, Constructors, Getters/Setters
 */
public class EnergyRecord {

    // --- Fields (private for encapsulation) ---
    private double sleepHours;
    private double workHours;
    private double screenTime;
    private int energyScore;
    private String status;
    private String timestamp;

    // --- Constructor ---
    public EnergyRecord(double sleepHours, double workHours, double screenTime,
                        int energyScore, String status, String timestamp) {
        this.sleepHours  = sleepHours;
        this.workHours   = workHours;
        this.screenTime  = screenTime;
        this.energyScore = energyScore;
        this.status      = status;
        this.timestamp   = timestamp;
    }

    // --- Getters ---
    public double getSleepHours()  { return sleepHours;  }
    public double getWorkHours()   { return workHours;   }
    public double getScreenTime()  { return screenTime;  }
    public int    getEnergyScore() { return energyScore; }
    public String getStatus()      { return status;      }
    public String getTimestamp()   { return timestamp;   }

    // --- Setters ---
    public void setSleepHours(double sleepHours)   { this.sleepHours  = sleepHours;  }
    public void setWorkHours(double workHours)     { this.workHours   = workHours;   }
    public void setScreenTime(double screenTime)   { this.screenTime  = screenTime;  }
    public void setEnergyScore(int energyScore)    { this.energyScore = energyScore; }
    public void setStatus(String status)           { this.status      = status;      }
    public void setTimestamp(String timestamp)     { this.timestamp   = timestamp;   }

    /**
     * Converts the record to a CSV line for file storage.
     * Format: sleepHours,workHours,screenTime,energyScore,status,timestamp
     */
    public String toCsvLine() {
        return sleepHours + "," + workHours + "," + screenTime + ","
             + energyScore + "," + status + "," + timestamp;
    }

    /**
     * Creates an EnergyRecord from a CSV line (used when loading from file).
     */
    public static EnergyRecord fromCsvLine(String line) {
        // Split the line by comma
        String[] parts = line.split(",", 6);
        if (parts.length < 6) return null; // Guard against malformed lines

        double sleepHours  = Double.parseDouble(parts[0].trim());
        double workHours   = Double.parseDouble(parts[1].trim());
        double screenTime  = Double.parseDouble(parts[2].trim());
        int    energyScore = Integer.parseInt(parts[3].trim());
        String status      = parts[4].trim();
        String timestamp   = parts[5].trim();

        return new EnergyRecord(sleepHours, workHours, screenTime,
                                energyScore, status, timestamp);
    }

    /**
     * Readable summary for display in the history panel.
     */
    @Override
    public String toString() {
        return String.format(
            "[%s]  Sleep: %.1fh | Work: %.1fh | Screen: %.1fh  →  Score: %d  (%s)",
            timestamp, sleepHours, workHours, screenTime, energyScore, status
        );
    }
}

