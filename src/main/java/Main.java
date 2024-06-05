import data.GameData;
import graphics.*;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import items.*;
import player.Player;
import player.Stats;
import services.ChestOpeningService;
import services.QuestService;
import states.GameState;
import states.StateManager;
import utils.CSVReader;
import services.InventoryService;

public class Main {
    public static void main(String[] args) {
        // Load all assets in the assets folder (these are graphical assets only)
        LoadAllAssets();

        // Load item and exp data from CSV
        GameData.loadItemData(CSVReader.readItemsFromCsv("D:\\IntelliJ Projects\\PAOO_Project_v2\\src\\main\\java\\items.csv"));
        GameData.loadExpData(CSVReader.readExpFromCsv("D:\\IntelliJ Projects\\PAOO_Project_v2\\src\\main\\java\\exp.csv"));
        GameData.loadChestData(CSVReader.readChestsFromCsv("D:\\IntelliJ Projects\\PAOO_Project_v2\\src\\main\\java\\chests.csv"));
        GameData.loadQuestData(CSVReader.readQuestsFromCsv("D:\\IntelliJ Projects\\PAOO_Project_v2\\src\\main\\java\\quests.csv"));

        // Create singleton instances of the Player and UIManager, other singleton instances will be here as well
        var uiManager = UIManager.getInstance();
        var player = Player.getInstance();
        var stateManager = StateManager.getInstance(new InventoryService(), new ChestOpeningService(), new QuestService());

        // Add player starting item
        player.getInventory().addItem(GameData.getItemByName("stick"));

        uiManager.clearScreen();

        stateManager.setState(GameState.QUEST_VIEW);  // Start in the inventory view
    }

    static void LoadAllAssets() {
        String basePath = "D:\\IntelliJ Projects\\PAOO_Project_v2\\src\\main\\java\\assets\\";

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
