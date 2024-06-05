package graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsAssetManager {
    private Map<String, char[][]> assets = new HashMap<>();
    private Map<String, int[][]> colorCodes = new HashMap<>();

    // Singleton Pattern
    private static GraphicsAssetManager instance = new GraphicsAssetManager();

    private GraphicsAssetManager() {}

    public static GraphicsAssetManager getInstance() {
        return instance;
    }

    public void addAsset(String name, char[][] asset, int[][] colorCode) {
        assets.put(name, asset);
        colorCodes.put(name, colorCode);
    }

    public char[][] getAsset(String name) {
        return assets.getOrDefault(name, null);
    }

    public int[][] getColorCode(String name) {
        return colorCodes.getOrDefault(name, null);
    }

    public static void loadAsset(String assetName, String basePath) throws IOException {
        String charFilePath = basePath + assetName + "_chars.csv";
        String colorFilePath = basePath + assetName + "_colors.csv";

        List<char[]> charList = new ArrayList<>();
        List<int[]> colorList = new ArrayList<>();

        // Read character data
        try (BufferedReader charReader = new BufferedReader(new FileReader(charFilePath))) {
            String line;
            while ((line = charReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    charList.add(line.toCharArray());
                }
            }
        }

        // Read color data
        try (BufferedReader colorReader = new BufferedReader(new FileReader(colorFilePath))) {
            String line;
            while ((line = colorReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    int[] colorRow = parseColorRow(line);
                    if (colorRow != null) {
                        colorList.add(colorRow);
                    }
                }
            }
        }

        int charRowCount = charList.size();
        int colorRowCount = colorList.size();

        if (charRowCount != colorRowCount) {
            throw new IOException("Character and color asset row counts do not match.");
        }

        for (int i = 0; i < charRowCount; i++) {
            if (charList.get(i).length != colorList.get(i).length) {
                throw new IOException("Character and color asset column counts do not match at row " + i);
            }
        }

        char[][] chars = charList.toArray(new char[0][]);
        int[][] colors = colorList.toArray(new int[0][]);

        GraphicsAssetManager.getInstance().addAsset(assetName, chars, colors);
    }

    private static int[] parseColorRow(String line) {
        String[] parts = line.split(",");
        int[] colors = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                colors[i] = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid color value at index " + i + ": " + parts[i]);
                return null; // Return null if there's an invalid value
            }
        }
        return colors;
    }
}
