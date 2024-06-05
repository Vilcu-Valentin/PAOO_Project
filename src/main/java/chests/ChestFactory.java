package chests;

import data.GameData;
import items.Item;
import items.GearFactory;
import items.Rarity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChestFactory {
    public static Chest createChestFromCsv(String[] csvData) {
        String name = csvData[0];
        String description = csvData[1];
        Rarity rarity = Rarity.valueOf(csvData[2].toUpperCase());

        Map<Item, Integer> rewardItems = new LinkedHashMap<>();
        for (int i = 3; i < csvData.length; i += 2) {
            Item item = GameData.getItemByName(csvData[i]).copy();
            int ticketValue = Integer.parseInt(csvData[i + 1]);
            rewardItems.put(item, ticketValue);
        }

        return new Chest(name, description, rarity, rewardItems);
    }
}
