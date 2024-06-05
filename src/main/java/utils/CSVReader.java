package utils;

import items.Item;
import items.GearFactory;
import chests.Chest;
import chests.ChestFactory;
import quests.Quest;
import quests.QuestFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    public static List<Item> readItemsFromCsv(String filePath) {
        List<Item> items = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Item item = GearFactory.createGearFromCsv(values);
                items.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static Map<Integer, Integer> readExpFromCsv(String filePath) {
        Map<Integer,Integer> expValues = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int expLvl = Integer.parseInt(values[0]);
                int expValue = Integer.parseInt(values[1]);
                expValues.put(expLvl, expValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expValues;
    }

    public static List<Chest> readChestsFromCsv(String filePath) {
        List<Chest> chests = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Chest chest = ChestFactory.createChestFromCsv(values);
                chests.add(chest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chests;
    }

    public static List<Quest> readQuestsFromCsv(String filePath) {
        List<Quest> quests = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0; // To keep track of the line number for better debugging
            while ((line = br.readLine()) != null) {
                lineNumber++; // Increment line number with each new line read
                try {
                    String[] values = line.split(",");
                    if (values.length != 7) {
                        System.err.println("Invalid line format on line " + lineNumber + ": " + line);
                        continue; // Skip this iteration if the line format is invalid
                    }
                    Quest quest = QuestFactory.createQuestFromCsv(values);
                    quests.add(quest);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("ArrayIndexOutOfBoundsException on line " + lineNumber + ": " + line);
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    System.err.println("IllegalArgumentException on line " + lineNumber + ": " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read the file: " + filePath);
            e.printStackTrace();
        }

        return quests;
    }
}
