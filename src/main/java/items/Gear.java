package items;

import java.util.Map;
import java.util.HashMap;
import graphics.GraphicsAssetManager;

public class Gear extends Item {
    private GearType gearType;
    private Map<String, Integer> statModifiers;

    public Gear(Item item, GearType gearType, Map<String, Integer> statModifiers) {
        super(item.name, item.assetId, item.description, item.rarity);
        this.gearType = gearType;
        this.statModifiers = statModifiers;
    }
    public Gear(String name, String assetId, String description, Rarity rarity, GearType gearType, Map<String, Integer> statModifiers) {
        super(name, assetId, description, rarity);
        this.gearType = gearType;
        this.statModifiers = statModifiers;
    }
    public Gear(Gear gear)
    {
        this.gearType = gear.gearType;
        this.statModifiers = gear.statModifiers;
        this.name = gear.name;
        this.assetId = gear.assetId;
        this.description = gear.description;
        this.rarity = gear.rarity;
    }
    @Override
    public Gear copy()
    {
        return new Gear(this);
    }

    @Override
    public String getAssetId() {
        GraphicsAssetManager manager = GraphicsAssetManager.getInstance();
        if (manager.getAsset(assetId) == null) {  // Check if asset exists using the manager
            return getDefaultAssetIdForType(gearType);
        }
        return assetId;
    }

    private String getDefaultAssetIdForType(GearType gearType) {
        // Return a default asset ID based on the gear type
        switch (gearType) {
            case HELMET: return "default-helmet";
            case CHESTPLATE: return "default-chestplate";
            case LEGGINGS: return "default-leggings";
            case BOOTS: return "default-boots";
            case WEAPON: return "default-weapon";
            case RING: return "default-ring";
            case BRACELET: return "default-bracelet";
            case NECKLACE: return "default-necklace";
            default: return "default-item";
        }
    }


    // Getters and Setters
    public GearType getGearType() {
        return gearType;
    }

    public void setGearType(GearType gearType) {
        this.gearType = gearType;
    }

    public Map<String, Integer> getStatModifiers() {
        return statModifiers;
    }

    public void setStatModifiers(Map<String, Integer> statModifiers) {
        this.statModifiers = statModifiers;
    }
}
