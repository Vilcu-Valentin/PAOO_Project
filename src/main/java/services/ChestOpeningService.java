package services;

import chests.Chest;
import data.GameData;
import items.Item;
import graphics.UIManager;
import player.Player;
import states.GameState;
import states.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class ChestOpeningService {
    private UIManager uiManager = UIManager.getInstance();
    private static final int VISIBLE_ITEMS = 7; // Number of items visible in the window
    private static final int ITEM_SPACING = 9; // Characters width per item
    private static final int CENTER_POSITION = 30; // Center position on screen

    public void openChest() {
        List<Chest> chests = GameData.getChestData();
        int playerLevel = Player.getInstance().getStats().getLvl();

        // Calculate the weight for each range
        List<Integer> weights = calculateWeightsForLevel(playerLevel);

        // Get the weighted random chest index within the correct range
        int randomChestIndex = getWeightedRandomChestIndex(weights);
        Chest chest = chests.get(randomChestIndex);

        // Get the item that will be won from the chest
        Item wonItem = chest.Open();

        // Display the animation
        displayAnimation(chest, wonItem);
    }

    private List<Integer> calculateWeightsForLevel(int level) {
        List<Integer> weights = new ArrayList<>();
        int level1 = (int)((100 / Math.pow(level, 0.5f)) - 20);
        int level10 = (int)((-Math.pow(level - 15, 2))*0.5+50);
        int level20 = (int)(-Math.pow(level - 25, 2)+30);
        weights.add(level1);
        weights.add(level10);
        weights.add(level20);
        return weights;
    }

    private int getWeightedRandomChestIndex(List<Integer> weights) {
        // Calculate total tickets
        int totalTickets = 0;
        for (int weight : weights) {
            totalTickets += weight;
        }

        // Determine which ticket number is the winning one
        int randomTicket = new Random().nextInt(totalTickets) + 1;

        // Determine which range wins based on the ticket
        int cumulativeTickets = 0;
        for (int i = 0; i < weights.size(); i++) {
            cumulativeTickets += weights.get(i);
            if (randomTicket <= cumulativeTickets) {
                int rangeStartIndex = i * 4; // Each range has 4 chests
                int offset = new Random().nextInt(4); // Choose randomly within the selected range
                return rangeStartIndex + offset;
            }
        }

        return 0; // Default return
    }

    private Item generateSingleAnimationItem(Chest chest, Random rand) {
        List<Item> pool = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : chest.getRewardItems().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                pool.add(entry.getKey());
            }
        }

        int randomIndex = rand.nextInt(pool.size());
        return pool.get(randomIndex);
    }


    private void displayAnimation(Chest chest, Item wonItem) {
        int animationDuration = 3000;
        int totalSteps = animationDuration / 100;
        int slowDownPoint = (int) (totalSteps * 0.7); // Start slowing down later
        int introductionPoint = totalSteps - VISIBLE_ITEMS / 2 - 2; // Introduce won item to appear in middle

        Random rand = new Random();
        List<Item> visibleItems = new ArrayList<>(VISIBLE_ITEMS);
        for (int i = 0; i < VISIBLE_ITEMS; i++) {
            visibleItems.add(generateSingleAnimationItem(chest, rand));
        }

        for (int i = 0; i < totalSteps; i++) {
            uiManager.clearScreen();
            uiManager.clearLoadedAssets();
            uiManager.loadText("=\\-/=", 7, 30, 7, 5, 1);
            uiManager.loadText("=/-\\=", 7, 30,13,5,1);

            for (int j = 0; j < VISIBLE_ITEMS; j++) {
                int x = 2 + (j * ITEM_SPACING);
                int color = 2;
                switch (visibleItems.get(j).getRarity()){
                    case COMMON:
                        color = 100;
                        break;
                    case RARE:
                        color = 112;
                        break;
                    case EPIC:
                        color = 200;
                        break;
                    case LEGENDARY:
                        color = 208;
                        break;
                    case MYTHICAL:
                        color = 160;
                }
                uiManager.loadText("+=====+", color, x, 8, 7,1);
                uiManager.loadText("+=====+", color, x, 12, 7, 1);
                uiManager.loadText("|     |", color, x, 9, 7, 1);
                uiManager.loadText("|     |", color, x, 10, 7, 1);
                uiManager.loadText("|     |", color, x, 11, 7, 1);
                uiManager.loadAsset(visibleItems.get(j).getAssetId(), x+1, 10);
            }

            uiManager.drawScreen();

            if (i >= introductionPoint) {
                // Insert the won item so it ends up in the middle when the animation stops
                visibleItems.remove(0);
                if (i < totalSteps - 1) {
                    visibleItems.add((i == introductionPoint) ? wonItem : generateSingleAnimationItem(chest, rand));
                }
            } else {
                // Continue normal item replacement
                visibleItems.remove(0);
                visibleItems.add(generateSingleAnimationItem(chest, rand));
            }

            int sleepTime = 100;
            if (i >= slowDownPoint) {
                int remainingSteps = totalSteps - i;
                sleepTime = 100 + (300 * (slowDownPoint + VISIBLE_ITEMS - remainingSteps) / slowDownPoint);
            }
            sleep(sleepTime);
        }

        // Final display of the won item
        drawWinScreen(wonItem);

    }

    private void drawWinScreen(Item wonItem) {
        uiManager.clearScreen();
        uiManager.clearLoadedAssets();

        int color = 2;
        switch (wonItem.getRarity()){
            case COMMON:
                color = 100;
                break;
            case RARE:
                color = 112;
                break;
            case EPIC:
                color = 200;
                break;
            case LEGENDARY:
                color = 208;
                break;
            case MYTHICAL:
                color = 160;
        }
        uiManager.loadText("+=====+", color, CENTER_POSITION-1, 8, 7,1);
        uiManager.loadText("+=====+", color, CENTER_POSITION-1, 12, 7, 1);
        uiManager.loadText("|     |", color, CENTER_POSITION-1, 9, 7, 1);
        uiManager.loadText("|     |", color, CENTER_POSITION-1, 10, 7, 1);
        uiManager.loadText("|     |", color, CENTER_POSITION-1, 11, 7, 1);

        uiManager.loadAsset(wonItem.getAssetId(), CENTER_POSITION, 10);  // Ensure the item is centered
        uiManager.loadText(wonItem.getName(), color, CENTER_POSITION,  13, 25, 1);
        uiManager.drawScreen();
        sleep(1000);

        // Add the item to the inventory
        Player.getInstance().getInventory().addItem(wonItem);
        StateManager.getInstance().setState(GameState.INVENTORY_VIEW);
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
