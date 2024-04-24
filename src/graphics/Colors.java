package graphics;

public class Colors {
    // ANSI Reset
    public static final String ANSI_RESET = "\u001B[0m";

    // ANSI Foreground Colors
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_B_BLACK = "\u001B[90m";
    public static final String ANSI_B_RED = "\u001B[91m";
    public static final String ANSI_B_GREEN = "\u001B[92m";
    public static final String ANSI_B_YELLOW = "\u001B[93m";
    public static final String ANSI_B_BLUE = "\u001B[94m";
    public static final String ANSI_B_PURPLE = "\u001B[95m";
    public static final String ANSI_B_CYAN = "\u001B[96m";
    public static final String ANSI_B_WHITE = "\u001B[97m";

    // Method to get the ANSI color code by an integer identifier
    public static String getColorCode(int color) {
        switch (color) {
            case 1: return ANSI_RED;
            case 2: return ANSI_GREEN;
            case 3: return ANSI_YELLOW;
            case 4: return ANSI_BLUE;
            case 5: return ANSI_PURPLE;
            case 6: return ANSI_CYAN;
            case 7: return ANSI_WHITE;
            case 8: return ANSI_BLACK;
            case 9: return ANSI_B_RED;
            case 10: return ANSI_B_GREEN;
            case 11: return ANSI_B_YELLOW;
            case 12: return ANSI_B_BLUE;
            case 13: return ANSI_B_PURPLE;
            case 14: return ANSI_B_CYAN;
            case 15: return ANSI_B_WHITE;
            case 16: return ANSI_B_BLACK;
            default: return ANSI_RESET;
        }
    }
}
