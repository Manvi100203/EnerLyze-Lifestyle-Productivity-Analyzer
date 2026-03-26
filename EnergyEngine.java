import java.util.ArrayList;

/**
 * EnergyEngine.java
 * Contains all backend logic for calculating energy scores and suggestions.
 * Demonstrates: Static methods, if-else, String handling
 */
public class EnergyEngine {

    // --- Energy Score Calculation ---
    /**
     * Calculates an energy score (0-100) based on sleep, work, and screen time.
     *
     * Scoring logic:
     *   Sleep  (0-40 pts): Optimal = 7-9 hrs. Penalise over/under sleep.
     *   Work   (0-30 pts): Optimal = 6-8 hrs. Heavy overwork penalised more.
     *   Screen (0-30 pts): Lower screen time = better score.
     */
    public static int calculateScore(double sleepHours, double workHours, double screenTime) {
        int score = 0;

        // --- Sleep score (0-40) ---
        if (sleepHours >= 7 && sleepHours <= 9) {
            score += 40;
        } else if (sleepHours >= 6 && sleepHours < 7) {
            score += 30;
        } else if (sleepHours > 9 && sleepHours <= 10) {
            score += 30;
        } else if (sleepHours >= 5 && sleepHours < 6) {
            score += 20;
        } else if (sleepHours > 10) {
            score += 20;
        } else if (sleepHours >= 4 && sleepHours < 5) {
            score += 10;
        } else {
            score += 0; // less than 4 hours
        }

        // --- Work score (0-30) ---
        if (workHours >= 6 && workHours <= 8) {
            score += 30;
        } else if (workHours > 8 && workHours <= 10) {
            score += 20;
        } else if (workHours > 10 && workHours <= 12) {
            score += 10;
        } else if (workHours < 6 && workHours >= 4) {
            score += 25;
        } else if (workHours < 4) {
            score += 30; // rest day
        } else {
            score += 0; // more than 12 hours
        }

        // --- Screen time score (0-30) ---
        if (screenTime <= 2) {
            score += 30;
        } else if (screenTime <= 4) {
            score += 22;
        } else if (screenTime <= 6) {
            score += 14;
        } else if (screenTime <= 8) {
            score += 7;
        } else {
            score += 0; // 8+ hours
        }

        // Clamp between 0-100
        return Math.max(0, Math.min(100, score));
    }

    // --- Status Label ---
    /**
     * Returns a text status based on the energy score.
     */
    public static String getStatus(int score) {
        if (score >= 70) {
            return "High ⚡";
        } else if (score >= 40) {
            return "Moderate 🔋";
        } else {
            return "Low 😴";
        }
    }

    // --- Personalised Suggestions ---
    /**
     * Returns a multi-line suggestion string based on the inputs.
     * Demonstrates: if-else conditions, String handling
     */
    public static String getSuggestions(double sleepHours, double workHours, double screenTime, int score) {
        StringBuilder sb = new StringBuilder();
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("  💡 Personalised Suggestions\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        // Sleep feedback
        if (sleepHours < 6) {
            sb.append("🛌 Sleep more! You only got ").append(sleepHours).append("h.\n");
            sb.append("   Aim for 7-9 hours for peak energy.\n");
        } else if (sleepHours > 10) {
            sb.append("😴 You slept ").append(sleepHours).append("h — a bit too much.\n");
            sb.append("   Oversleeping can cause grogginess.\n");
        } else {
            sb.append("✅ Great sleep duration (").append(sleepHours).append("h)!\n");
        }

        // Work feedback
        if (workHours > 10) {
            sb.append("💼 You worked ").append(workHours).append("h today — that's a lot.\n");
            sb.append("   Try to take short breaks every 90 minutes.\n");
        } else if (workHours > 8) {
            sb.append("⚠️ Slight overwork (").append(workHours).append("h). Consider wrapping up earlier.\n");
        } else {
            sb.append("✅ Work-time looks balanced (").append(workHours).append("h).\n");
        }

        // Screen time feedback
        if (screenTime > 8) {
            sb.append("📵 Screen time is very high (").append(screenTime).append("h).\n");
            sb.append("   Use a blue-light filter and take eye breaks.\n");
        } else if (screenTime > 5) {
            sb.append("📱 Moderate screen time (").append(screenTime).append("h).\n");
            sb.append("   Try the 20-20-20 rule to reduce eye strain.\n");
        } else {
            sb.append("✅ Low screen time (").append(screenTime).append("h) — eyes are happy!\n");
        }

        // General tip based on score
        sb.append("\n");
        if (score < 40) {
            sb.append("🔴 Overall: Your energy is LOW today.\n");
            sb.append("   Rest, hydrate, and avoid stressors.\n");
        } else if (score < 70) {
            sb.append("🟡 Overall: MODERATE energy.\n");
            sb.append("   A short walk or nap can recharge you.\n");
        } else {
            sb.append("🟢 Overall: Great energy day!\n");
            sb.append("   Keep up these healthy habits. 🎉\n");
        }

        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        return sb.toString();
    }

    // --- Average Score ---
    /**
     * Calculates the average energy score from a list of records.
     * Demonstrates: ArrayList, loops
     */
    public static double calculateAverage(ArrayList<EnergyRecord> records) {
        if (records.isEmpty()) return 0;
        int total = 0;
        for (EnergyRecord r : records) {  // enhanced for-loop
            total += r.getEnergyScore();
        }
        return (double) total / records.size();
    }

    // --- Compare with Last Record ---
    /**
     * Compares current score with the last saved record and returns a summary.
     */
    public static String compareWithLast(ArrayList<EnergyRecord> records, int currentScore) {
        if (records.isEmpty()) {
            return "ℹ️ No previous record to compare with.";
        }
        EnergyRecord last = records.get(records.size() - 1);
        int diff = currentScore - last.getEnergyScore();

        if (diff > 0) {
            return "📈 Your energy improved by " + diff + " points compared to last record!";
        } else if (diff < 0) {
            return "📉 Your energy dropped by " + Math.abs(diff) + " points compared to last record.";
        } else {
            return "➡️ Same energy score as the last record.";
        }
    }
}
