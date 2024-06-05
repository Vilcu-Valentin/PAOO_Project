package graphics;

public class Colors {
    // ANSI Reset
    public static final String ANSI_RESET = "\u001B[0m";

    // Method to get the ANSI color code by an integer identifier
    public static String getColorCode(int color) {
        if (color >= 0 && color <= 255) {
            return "\u001B[38;5;" + color + "m";
        }
        return ANSI_RESET;
    }

    // Method to get the ANSI background color code by an integer identifier
    public static String getBackgroundColorCode(int color) {
        if (color >= 0 && color <= 255) {
            return "\u001B[48;5;" + color + "m";
        }
        return ANSI_RESET;
    }
}
