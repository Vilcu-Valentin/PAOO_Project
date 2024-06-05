package services;

import graphics.UIManager;
import items.Gear;
import items.Item;
import data.GameData;
import player.*;
import states.GameState;
import states.StateManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Math.abs;

public class InventoryService {
    private UIManager uiManager = UIManager.getInstance();
    private Player player = Player.getInstance();
    private Scanner scanner = new Scanner(System.in);
    private int cursorIndex; // helps with inventory navigation
    private int startIndex; // helps with scrolling

    public InventoryService() {
        this.cursorIndex = 0;
        this.startIndex = 0;
    }

    public void renderInventory() {
        uiManager.clearLoadedAssets();
        uiManager.loadAsset("inventory", 0, 0);
        List<Item> items = player.getInventory().getItems();

        uiManager.loadText(Integer.toString(items.size()), 7, 27, 0, 2, 1);
        uiManager.loadText(Integer.toString(player.getInventory().getCapacity()), 7, 30, 0, 2, 1);

        int maxVisibleItems = 7; // Based on the available space and item height (3 cells each)

        // Determine the items to display based on the current start index
        int endIndex = Math.min(startIndex + maxVisibleItems, items.size());

        // Render the up-arrow if there are items above the current start index
        if (startIndex > 0) {
            uiManager.loadAsset("up-arrow", 18, 1);
        }

        for (int i = startIndex; i < endIndex; i++) {
            renderInventoryItem(items.get(i), i - startIndex, i == cursorIndex);
        }

        // Render the down-arrow if there are items below the visible list
        if (endIndex < items.size()) {
            uiManager.loadAsset("down-arrow", 18, 24); // Assuming the inventory area is 7 cells tall
        }

        if (!items.isEmpty() && cursorIndex < items.size()) {
            renderDescription(items.get(cursorIndex));
        }

        renderPlayerStats();
        renderPlayerStatsModifiers();

        uiManager.drawScreen();

        handleInput();
    }

    private void renderInventoryItem(Item item, int position, boolean isSelected) {
        int x = 2;
        int y = 2 + position * 3;
        if (isSelected) {
            uiManager.loadAsset("select-item", x - 2, y); // Load the "selected-item" asset
        }
        if(item instanceof Gear)
        {
            uiManager.loadText(((Gear) item).getGearType().toString(), 8, x + 1, y, 10, 1);
        }
        int nameColor = 3;
        switch (item.getRarity())
        {
            case COMMON:
                nameColor = 100;
                break;
            case RARE:
                nameColor = 112;
                break;
            case EPIC:
                nameColor = 200;
                break;
            case LEGENDARY:
                nameColor = 208;
                break;
            case MYTHICAL:
                nameColor = 160;
        }
        uiManager.loadText(item.getName(), nameColor, x + 8, y + 1, 36, 1);
        if (item instanceof Gear) {
            Gear gear = (Gear) item;
            String assetId = gear.getAssetId();
            uiManager.loadAsset(assetId, x + 1, y + 1);
            // Display "Equipped" text if the gear is equipped
            if (player.getEquippedGear().get(gear.getGearType()) == gear) {
                uiManager.loadText("Equipped", 11 ,x + 29, y + 2,  10, 10); // Example position and color
            }
        }
    }

    private void renderDescription(Item item) {
        String description = item.getDescription();
        uiManager.loadText(description, 2, 47, 2, 24, 4); // Example position and color

        if(item instanceof Gear)
        {
            int x=47, y=6, i=0;
            Map<String, Integer> gearStats = ((Gear) item).getStatModifiers();
            for (Map.Entry<String, Integer> entry : gearStats.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if(value != 0)
                {
                    int vColor = 2;
                    int color = 1;
                    if(value < 0)
                        vColor = 1;
                    StringBuilder modTypeStr = new StringBuilder();
                    switch (key.toLowerCase())
                    {
                        case "health":
                            modTypeStr.append("HP:  ");
                            color = 9;
                            break;
                        case "mana":
                            modTypeStr.append("MP:  ");
                            color = 6;
                            break;
                        case "strength":
                            modTypeStr.append("STR: ");
                            color = 1;
                            break;
                        case "defense":
                            modTypeStr.append("DEF: ");
                            color = 4;
                            break;
                        case "agility":
                            modTypeStr.append("AGI: ");
                            color = 15;
                            break;
                        case "intelligence":
                            modTypeStr.append("INT: ");
                            color = 10;
                            break;
                        case "dexterity":
                            modTypeStr.append("DEX: ");
                            color = 3;
                            break;
                    }
                    uiManager.loadText(modTypeStr.toString(), color, x, y, 5, 1);
                    uiManager.loadText(value.toString(), vColor, x + 5, y, 4, 1);

                    y+=1;
                    if(i >= 3)
                    {
                        x=59;
                        y=6 + 7%i - 1;
                    }
                    i++;
                }
            }
        }
    }

    private void renderPlayerStats() {
        Stats stats = player.getStats();

        uiManager.loadText(Integer.toString(stats.getLvl()), 10, 52, 13, 19,1);
        uiManager.loadText(Integer.toString(GameData.getExpDataByLvl(player.getStats().getLvl())), 10, 64, 13, 6, 1);
        uiManager.loadText(expPercentage(stats.getExp()), 10, 47, 14, 24, 1);
        uiManager.loadText(Integer.toString(stats.getHealth()), 10, 52, 16, 19, 1);
        uiManager.loadText(Integer.toString(stats.getMana()), 10, 52, 17, 19, 1);
        uiManager.loadText(Integer.toString(stats.getStrength()), 10, 52, 19, 19, 1);
        uiManager.loadText(Integer.toString(stats.getDefense()), 10, 52, 20, 19, 1);
        uiManager.loadText(Integer.toString(stats.getIntelligence()), 10, 52, 21, 19, 1);
        uiManager.loadText(Integer.toString(stats.getDexterity()), 10, 52, 22, 19, 1);
        uiManager.loadText(Integer.toString(stats.getAgility()), 10, 52, 23, 19, 1);
    }

    private void renderPlayerStatsModifiers()
    {
        Stats stats = player.getStats();
        Item selectedItem = getSelectedItem();

        Map<String, Integer> currentValues = new HashMap<>();
        currentValues.put("health", stats.getHealth());
        currentValues.put("mana", stats.getMana());
        currentValues.put("strength", stats.getStrength());
        currentValues.put("defense", stats.getDefense());
        currentValues.put("agility", stats.getAgility());
        currentValues.put("intelligence", stats.getIntelligence());
        currentValues.put("dexterity", stats.getDexterity());

        if(selectedItem instanceof Gear)
        {
            Map<String, Integer> itemModifiers = ((Gear) selectedItem).getStatModifiers();
            itemModifiers.forEach((key, value) -> {

                if(player.getEquippedGear().get((((Gear) selectedItem).getGearType())) != null)
                    if(selectedItem == player.getEquippedGear().get(((Gear) selectedItem).getGearType()))
                        value = -value;
                    else
                        value = value - player.getEquippedGear().get(((Gear) selectedItem).getGearType()).getStatModifiers().get(key);
                if(value != 0)
                {
                    StringBuilder diffStr = new StringBuilder("(");
                    StringBuilder totalStr = new StringBuilder("-> ");
                    int color = 1;
                    if(value > 0)
                        {
                            diffStr.append("+" + value + ")");
                            color = 2;
                        }
                    else
                        {
                            diffStr.append("-" + abs(value) + ")");
                            color = 1;
                        }
                    int total = value + currentValues.get(key);
                    totalStr.append(total);

                    int x=0,y=0;
                    switch (key.toLowerCase())
                    {
                        case "health":
                            x=56;
                            y=16;
                            break;
                        case "mana":
                            x=56;
                            y=17;
                            break;
                        case "strength":
                            x=56;
                            y=19;
                            break;
                        case "defense":
                            x=56;
                            y=20;
                            break;
                        case "intelligence":
                            x=56;
                            y=21;
                            break;
                        case "dexterity":
                            x=56;
                            y=22;
                            break;
                        case "agility":
                            x=56;
                            y=23;
                            break;
                    }

                    uiManager.loadText(diffStr.toString(), color, x, y, 7, 1);
                    uiManager.loadText(totalStr.toString(), color, x+8, y, 7, 1);
                }
            });
        }
    }

    private String expPercentage(int exp) {
        int maxExp = GameData.getExpDataByLvl(player.getStats().getLvl());
        int percentage = (exp * 100) / maxExp;  // Calculate the percentage of current exp relative to maxExp

        // Calculate how many '#' should be shown in the progress bar
        int numberOfHashes = (percentage * 22) / 100;  // As 10 is the total length of the progress bar

        // Build the progress bar string
        StringBuilder progressBar = new StringBuilder("[");

        // Append '#' for each point in numberOfHashes
        for (int i = 0; i < numberOfHashes; i++) {
            progressBar.append("#");
        }

        // Append '-' for the remaining places in the progress bar
        for (int i = numberOfHashes; i < 22; i++) {
            progressBar.append("-");
        }

        progressBar.append("]");  // Close the progress bar

        return progressBar.toString();
    }

    public void handleInput() {
        while(true){
            char key = readInput();
            switch (key) {
                case 'w':
                case 'W':
                    moveCursor(true);
                    renderInventory();
                    break;
                case 's':
                case 'S':
                    moveCursor(false);
                    renderInventory();
                    break;
                case 'e':
                case 'E':
                    equipSelectedItem();
                    renderInventory();
                    break;
                case 'x':
                case 'X':
                    deleteSelectedItem();
                    renderInventory();
                    break;
                case 'p':
                case 'P':
                    addStick(); //Test for adding new items at runtime
                    renderInventory();
                    break;
                case 'm':
                case 'M':
                    player.getStats().addExp(1000);
                    renderInventory();
                    break;
                case 'q':
                case 'Q':
                    StateManager.getInstance().setState(GameState.QUEST_VIEW);
                    break;
                case 'l':
                case 'L':
                    GameData.savePlayerData();
                default:
                    break;
            }
        }
    }

    private char readInput() {
        System.out.print("Input: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return ' '; // Return a default character or handle it as needed
        }
        return input.charAt(0);
    }

    private void moveCursor(boolean moveUp) {
        int itemsCount = player.getInventory().getItems().size();
        if (moveUp && cursorIndex > 0) {
            cursorIndex -= 1;
            if (cursorIndex < startIndex) {
                startIndex = cursorIndex;
            }
        } else if (!moveUp && cursorIndex < itemsCount - 1) {
            cursorIndex += 1;
            if (cursorIndex >= startIndex + 7) {
                startIndex = cursorIndex - 6;
            }
        }
    }

    private void equipSelectedItem() {
        Item item = getSelectedItem();
            if (item instanceof Gear) {
                Gear gear = (Gear) item;
                boolean result = false;
                if(gear == player.getEquippedGear().get(gear.getGearType()))
                    result = player.getInventory().unequipItem(gear);
                else
                    result = player.getInventory().equipItem(gear);
                //if(!result)
                 //   uiManager.loadText("You cannot equip/unequip this gear", 1, 0, 25, 30, 1);
            }
    }

    private void deleteSelectedItem()
    {
        Item item = getSelectedItem();
        boolean result = true;
        if(item instanceof Gear){
            Gear gear = (Gear) item;
            if(gear == player.getEquippedGear().get(gear.getGearType()))
                result = player.getInventory().unequipItem(gear);
        }
        if(result)
        {
            player.getInventory().removeItem(item);
            moveCursor(true);
            moveCursor(false);
        }
    }

    private void addStick()
    {
        player.getInventory().addItem(GameData.getItemByName("stick"));
    }

    private Item getSelectedItem()
    {
        List<Item> items = player.getInventory().getItems();
        if (!items.isEmpty() && cursorIndex < items.size())
            return items.get(cursorIndex);
        return new Item();
    }
}
