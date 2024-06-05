package items;

import player.Player;
import player.Stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class Inventory {
    private List<Item> items;
    private int capacity;
    private Player player; // Reference to player to update stats when equipping/unequipping gear

    public Inventory(int capacity, Player player) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
        this.player = player;
    }

    public void setCapacity(int capacity) {this.capacity = capacity;}
    public int getCapacity() {return  capacity;}
    public boolean addItem(Item item) {
        if (items.size() < capacity) {
            items.add(item.copy());
            return true;
        }
        return false;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean equipItem(Gear gear) {
        if (gear == null || checkForNegativeModifier(gear, true)) {
            return false;
        }

        GearType type = gear.getGearType();
        Gear currentGear = player.getEquippedGear().get(type);

        if (currentGear != null) {
            unequipItem(currentGear); // Unequip the current item in that slot
        }

        if(gear != currentGear)
        {
            player.getEquippedGear().put(type, gear);
            updatePlayerStats(gear, true);
        }
        return true;
    }

    public boolean unequipItem(Gear gear) {
        if (gear == null || !player.getEquippedGear().containsValue(gear) || checkForNegativeModifier(gear, false)) {
            return false;
        }

        player.getEquippedGear().remove(gear.getGearType());
        updatePlayerStats(gear, false);

        return true;
    }

    private void updatePlayerStats(Gear gear, boolean equip) {
        Stats stats = player.getStats();
        gear.getStatModifiers().forEach((key, value) -> {
            switch (key.toLowerCase()) {
                case "health":
                    stats.setHealth(stats.getHealth() + (equip ? value : -value));
                    break;
                case "mana":
                    stats.setMana(stats.getMana() + (equip ? value: -value));
                    break;
                case "strength":
                    stats.setStrength(stats.getStrength() + (equip ? value : -value));
                    break;
                case "agility":
                    stats.setAgility(stats.getAgility() + (equip ? value : -value));
                    break;
                case "intelligence":
                    stats.setIntelligence(stats.getIntelligence() + (equip ? value : -value));
                    break;
                case "defense":
                    stats.setDefense(stats.getDefense() + (equip ? value : -value));
                    break;
                case "dexterity":
                    stats.setDexterity(stats.getDexterity() + (equip ? value: -value));
            }
        });
    }

    private boolean checkForNegativeModifier(Gear gear, boolean isEquipping) {
        Stats stats = player.getStats();

        Map<String, Integer> currentValues = new HashMap<>();
        currentValues.put("health", stats.getHealth());
        currentValues.put("mana", stats.getMana());
        currentValues.put("strength", stats.getStrength());
        currentValues.put("defense", stats.getDefense());
        currentValues.put("intelligence", stats.getIntelligence());
        currentValues.put("dexterity", stats.getDexterity());
        currentValues.put("agility", stats.getAgility());

        Map<String, Integer> itemModifiers = gear.getStatModifiers();
        for (Map.Entry<String, Integer> entry : itemModifiers.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            // Adjust value if there is already gear equipped of the same type
            if (player.getEquippedGear().get(gear.getGearType()) != null) {
                Integer currentModifier = player.getEquippedGear().get(gear.getGearType()).getStatModifiers().get(key);
                if (currentModifier != null) {
                    if(!isEquipping)
                        value = -currentModifier;
                    else
                        value -= currentModifier;
                }
            }

            // Check if the resulting modifier is negative
            if (value < 0) {
                int total = value + currentValues.get(key);
                if (total < 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
