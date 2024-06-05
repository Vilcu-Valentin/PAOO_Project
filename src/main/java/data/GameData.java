package data;

import items.Gear;
import items.GearFactory;
import items.GearType;
import items.Item;
import chests.Chest;
import player.Player;
import player.Stats;
import quests.Quest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {
    private static Map<String, Item> itemData = new HashMap<>();
    private static Map<Integer, Integer> expData;
    private static List<Chest> chestData;
    private static List<Quest> questData;
    private static int lvlCap = 25;

    private GameData() {
        // Private constructor to prevent instantiation
    }

    public static void savePlayerData() {
        Player player = Player.getInstance();
        Stats stats = player.getStats();

        // Generate the filename
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateStr = sdf.format(new Date());
        String filename = "D:\\IntelliJ Projects\\PAOO_Project_v2\\src\\main\\java\\saves\\Level" + stats.getLvl() + "-" + dateStr + ".csv";

        try (FileWriter writer = new FileWriter(filename)) {
            // Write stats
            writer.write("Stats\n");
            writer.write("Health," + stats.getHealth() + "\n");
            writer.write("Mana," + stats.getMana() + "\n");
            writer.write("Level," + stats.getLvl() + "\n");
            writer.write("Exp," + stats.getExp() + "\n");
            writer.write("Strength," + stats.getStrength() + "\n");
            writer.write("Agility," + stats.getAgility() + "\n");
            writer.write("Intelligence," + stats.getIntelligence() + "\n");
            writer.write("Defense," + stats.getDefense() + "\n");
            writer.write("Dexterity," + stats.getDexterity() + "\n");

            // Write equipped gear
            writer.write("\nEquipped Gear\n");
            for (Map.Entry<GearType, Gear> entry : player.getEquippedGear().entrySet()) {
                Gear gear = entry.getValue();
                if (gear != null) {
                    writer.write(entry.getKey().name() + "," + gear.getName() + "\n");
                }
            }

            // Write inventory
            writer.write("\nInventory\n");
            for (Item item : player.getInventory().getItems()) {
                writer.write(item.getName() + "\n");
            }

            System.out.println("Game saved to " + filename);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public static void loadPlayerData(String filename) {
        Player player = Player.getInstance();
        Stats stats = player.getStats();
        player.getInventory().getItems().clear(); // Clear current inventory
        player.getEquippedGear().clear(); // Clear currently equipped gear

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\IntelliJ Projects\\PAOO_Project_v2\\src\\main\\java\\saves\\" + filename))) {
            String line;
            String section = "";

            while ((line = br.readLine()) != null) {
                if (line.equals("Stats")) {
                    section = "Stats";
                    continue;
                } else if (line.equals("Equipped Gear")) {
                    section = "Equipped Gear";
                    continue;
                } else if (line.equals("Inventory")) {
                    section = "Inventory";
                    continue;
                }

                String[] values = line.split(",");
                switch (section) {
                    case "Stats":
                        switch (values[0].trim()) {
                            case "Health":
                                stats.setHealth(Integer.parseInt(values[1].trim()));
                                break;
                            case "Mana":
                                stats.setMana(Integer.parseInt(values[1].trim()));
                                break;
                            case "Level":
                                stats.setLvl(Integer.parseInt(values[1].trim()));
                                break;
                            case "Exp":
                                stats.setExp(Integer.parseInt(values[1].trim()));
                                break;
                            case "Strength":
                                stats.setStrength(Integer.parseInt(values[1].trim()));
                                break;
                            case "Agility":
                                stats.setAgility(Integer.parseInt(values[1].trim()));
                                break;
                            case "Intelligence":
                                stats.setIntelligence(Integer.parseInt(values[1].trim()));
                                break;
                            case "Defense":
                                stats.setDefense(Integer.parseInt(values[1].trim()));
                                break;
                            case "Dexterity":
                                stats.setDexterity(Integer.parseInt(values[1].trim()));
                                break;
                        }
                        break;
                    case "Inventory":
                        Item item = itemData.get(values[0].trim().toLowerCase());
                        if (item != null) {
                            player.getInventory().addItem(item);
                        }
                        break;
                }
            }

            System.out.println("Game loaded from " + filename);
        } catch (IOException e) {
            System.err.println("Failed to load game: " + e.getMessage());
        }
    }


    public static void loadItemData(List<Item> items) {
        for (Item item : items) {
            itemData.put(item.getName().toLowerCase(), item);
        }
    }

    public static void loadQuestData(List<Quest> quests)
    {
        questData = quests;
    }
    public static void loadExpData(Map<Integer, Integer> _expData)
    {
        expData = _expData;
    }

    public static void loadChestData(List<Chest> chests)
    {
        chestData = chests;
    }

    public static int getLvlCap() {return lvlCap;}
    public static List<Quest> getQuestData() {
        return questData;
    }

    public static List<Chest> getChestData() {
        return chestData;
    }

    public static Map<Integer,Integer> getExpData()
    {
        return expData;
    }
    public static Integer getExpDataByLvl(Integer key)
    {
        return expData.get(key);
    }

    public static Item getItemByName(String name) {
        return itemData.get(name);
    }
    public static Map<String, Item> getAllItemData() {
        return itemData;
    }
}