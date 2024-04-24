package graphics;

import java.io.IOException;

public class UIManager {
    private static UIManager instance = new UIManager();
    private char[][] screenBuffer;
    private int[][] colorBuffer;
    private final int width;
    private final int height;

    // ANSI reset code to reset color formatting
    private static final String ANSI_RESET = "\u001B[0m";

    private UIManager() {
        this.width = 64;
        this.height = 24;
        this.screenBuffer = new char[this.height][this.width];
        this.colorBuffer = new int[this.height][this.width];
        clearLoadedAssets();
    }

    public static UIManager getInstance() {
        return instance;
    }

    public void loadAsset(String assetName, int x, int y) {
        char[][] asset = GraphicsAssetManager.getInstance().getAsset(assetName);
        int[][] colors = GraphicsAssetManager.getInstance().getColorCode(assetName);
        if (asset != null && colors != null) {
            for (int i = 0; i < asset.length; i++) {
                for (int j = 0; j < asset[i].length; j++) {
                    if (x + j < this.width && y + i < this.height) {
                        this.screenBuffer[y + i][x + j] = asset[i][j];
                        this.colorBuffer[y + i][x + j] = colors[i][j];
                    }
                }
            }
        }
    }

    public void drawScreen() {
        clearScreen();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char ch = screenBuffer[i][j];
                int colorCode = colorBuffer[i][j];
                String color = Colors.getColorCode(colorCode);
                System.out.print(color + ch + ANSI_RESET);
            }
            System.out.println();
        }
    }

    public void clearLoadedAssets() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.screenBuffer[i][j] = ' ';
                this.colorBuffer[i][j] = 0; // Reset to default color code
            }
        }
    }

    public void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred while trying to clear the screen.");
        }
    }
}
