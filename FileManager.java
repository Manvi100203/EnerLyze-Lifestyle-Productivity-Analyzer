import java.io.*;
import java.util.ArrayList;

/**
 * FileManager.java
 * Handles saving and loading EnergyRecords to/from a CSV file.
 * Demonstrates: File handling, Exception handling (try-catch), BufferedReader/FileWriter
 */
public class FileManager {

    // File where records are stored
    private static final String FILE_PATH = "energy_records.csv";

    /**
     * Saves all records in the ArrayList to a CSV file.
     * Overwrites the file completely on every save.
     *
     * @param records ArrayList of EnergyRecord objects
     * @throws IOException if file writing fails
     */
    public static void saveRecords(ArrayList<EnergyRecord> records) throws IOException {
        // FileWriter in overwrite mode (false = no append)
        FileWriter fw = new FileWriter(FILE_PATH, false);
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            for (EnergyRecord record : records) {  // loop through all records
                bw.write(record.toCsvLine());
                bw.newLine();
            }
        } finally {
            // Always close the writer to avoid resource leaks
            bw.close();
        }
    }

    /**
     * Loads records from the CSV file into an ArrayList.
     *
     * @return ArrayList of EnergyRecord objects (empty if file not found)
     */
    public static ArrayList<EnergyRecord> loadRecords() {
        ArrayList<EnergyRecord> records = new ArrayList<>();

        // Check if file exists first
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return records; // Return empty list silently
        }

        // Try to read the file line by line
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        EnergyRecord record = EnergyRecord.fromCsvLine(line);
                        if (record != null) {
                            records.add(record);
                        }
                    } catch (NumberFormatException e) {
                        // Skip malformed lines silently
                        System.out.println("Skipping malformed line: " + line);
                    }
                }
            }
            br.close();

        } catch (IOException e) {
            // File read error — return whatever we have so far
            System.out.println("Error reading file: " + e.getMessage());
        }

        return records;
    }

    /**
     * Returns the file path being used (for display purposes).
     */
    public static String getFilePath() {
        return FILE_PATH;
    }
}

