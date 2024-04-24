package graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphicsAssetManager {
    private Map<String, char[][]> assets = new HashMap<>();
    private Map<String, int[][]> colorCodes = new HashMap<>(); // Store color codes separately

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
        // Construct file paths
        String charFilePath = basePath + assetName + "_chars.csv";
        String colorFilePath = basePath + assetName + "_colors.csv";

        // Initialize arrays to hold the asset data
        List<char[]> charList = new ArrayList<>();
        List<int[]> colorList = new ArrayList<>();

        // Read character data
        try (BufferedReader charReader = new BufferedReader(new FileReader(charFilePath))) {
            String line;
            while ((line = charReader.readLine()) != null) {
                charList.add(line.toCharArray());
            }
        }

        // Read color data
        try (BufferedReader colorReader = new BufferedReader(new FileReader(colorFilePath))) {
            String line;
            while ((line = colorReader.readLine()) != null) {
                int[] colorRow = parseColorRow(line);
                colorList.add(colorRow);
            }
        }

        // Convert lists to arrays
        char[][] chars = charList.toArray(new char[0][]);
        int[][] colors = colorList.toArray(new int[0][]);

        // Add to GraphicsAssetManager
        GraphicsAssetManager.getInstance().addAsset(assetName, chars, colors);
    }

    private static int[] parseColorRow(String line) {
        String[] parts = line.split(",");
        int[] colors = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            colors[i] = Integer.parseInt(parts[i].trim());
        }
        return colors;
    }
}
