import graphics.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Create singleton instances of the GraphicsAssetManager and UIManager, used for rendering
        GraphicsAssetManager assetManager = GraphicsAssetManager.getInstance();
        UIManager uiManager = UIManager.getInstance();

        // Load all assets in the assets file
        LoadAllAssets();

        // Load and draw the green tree asset
        uiManager.loadAsset("greenTree", 0, 0); // Adjust position as needed
        uiManager.clearScreen();
        uiManager.drawScreen();
    }

    static void LoadAllAssets()
    {
        String basePath = "C:\\Users\\Vally\\Laborator\\PAOO-Project\\assets\\";

        File dir = new File(basePath);
        File[] files = dir.listFiles((d, name) -> name.endsWith("_chars.csv"));

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String assetName = fileName.substring(0, fileName.length() - "_chars.csv".length());
                System.out.println(assetName);
                try {
                    GraphicsAssetManager.loadAsset(assetName, basePath);
                } catch (IOException e) {
                    System.err.println("Failed to load asset " + assetName + ": " + e.getMessage());
                }
            }
        }
    }
}