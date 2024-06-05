package items;

import java.util.LinkedHashMap;
import java.util.Map;

public class GearFactory {
    public static Item createGearFromCsv(String[] csvData) {
        String name = csvData[0];
        String assetId = csvData[1];
        String description = csvData[2];
        GearType gearType = GearType.valueOf(csvData[3].toUpperCase());
        Rarity rarity = Rarity.valueOf(csvData[4].toUpperCase());

        Map<String, Integer> statModifiers = new LinkedHashMap<>();
        statModifiers.put("health", Integer.parseInt(csvData[5]));
        statModifiers.put("mana", Integer.parseInt(csvData[6]));
        statModifiers.put("strength", Integer.parseInt(csvData[7]));
        statModifiers.put("defense", Integer.parseInt(csvData[8]));
        statModifiers.put("intelligence", Integer.parseInt(csvData[9]));
        statModifiers.put("dexterity", Integer.parseInt(csvData[10]));
        statModifiers.put("agility", Integer.parseInt(csvData[11]));

        Gear gear = new Gear(new Item(name, assetId, description, rarity), gearType, statModifiers);
        gear.setName(name);
        gear.setAssetId(assetId);
        gear.setDescription(description);
        gear.setRarity(rarity);

        return gear;
    }
}