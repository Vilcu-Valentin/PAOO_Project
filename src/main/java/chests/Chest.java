package chests;

import player.Player;
import items.*;

import java.util.*;

public class Chest {
    private Player player = Player.getInstance();

    private String name;
    // private String assetId; // we'll see if we actually add an asset graphics or not
    private String description;
    private Rarity rarity;
    private Map<Item, Integer> rewardItems = new LinkedHashMap<>();

    // Constructors
    public Chest(Chest chest)
    {
        this.name = chest.name;
        this.description = chest.description;
        this.rarity = chest.rarity;
        this.rewardItems = chest.rewardItems;
    }
    public Chest(String name, String description, Rarity rarity, Map<Item, Integer> rewardItems) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.rewardItems = rewardItems;
    }
    public Chest copy(Chest chest)
    {
        return new Chest(this);
    }


    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Rarity getRarity() {
        return rarity;
    }
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Map<Item, Integer> getRewardItems() {
        return rewardItems;
    }
    public void setRewardItems(Map<Item, Integer> rewardItems) {
        this.rewardItems = rewardItems;
    }

    public Item Open()
    {
        // Calculate the total number of tickets
        int totalTickets = rewardItems.values().stream().mapToInt(Integer::intValue).sum();

        // Generate a random number between 1 and totalTickets (inclusive)
        Random rand = new Random();
        int randomTicket = rand.nextInt(totalTickets) + 1;

        // Iterate through the items and find the one that corresponds to the random ticket
        int currentTicketSum = 0;
        for (Map.Entry<Item, Integer> entry : rewardItems.entrySet()) {
            currentTicketSum += entry.getValue();
            if (randomTicket <= currentTicketSum) {
                return entry.getKey();
            }
        }

        // Should never reach here if the input map is valid
        throw new IllegalStateException("The input map is empty or invalid.");
    }
}
