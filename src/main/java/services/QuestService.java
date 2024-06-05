package services;

import data.GameData;
import graphics.UIManager;
import player.Player;
import quests.Quest;
import states.GameState;
import states.StateManager;
import utils.CSVReader;

import java.util.*;

public class QuestService {
    private UIManager uiManager = UIManager.getInstance();
    private Player player = Player.getInstance();
    private Random random = new Random();
    private Scanner scanner = new Scanner(System.in);
    private List<Quest> availableQuests;
    private List<Quest> quests;
    private boolean generateNewQuests = true;
    private int cursorIndex = 0;  // Cursor to navigate through quests

    public QuestService() {
        this.availableQuests = GameData.getQuestData(); // Assume this retrieves all available quests
    }

    public void renderQuests() {
        if (generateNewQuests) {
            quests = generateQuests();
            generateNewQuests = false;
        }

        displayQuests(quests);

        char input;
        while (true) {
            input = readInput();
            if (input == '\0') {
                // If input is empty (Enter key pressed), re-display the quests
                displayQuests(quests);
            } else {
                // Handle valid input
                handleInput(input, quests);
            }
        }
    }

    private List<Quest> generateQuests() {
        List<Quest> quests = new LinkedList<>();
        int playerLevel = player.getStats().getLvl();

        while (quests.size() < 3) {
            Quest quest = availableQuests.get(random.nextInt(availableQuests.size()));
            if (quest.getLevel() >= playerLevel - 5 && quest.getLevel() <= playerLevel && !quests.contains(quest)) {
                quests.add(quest);
            }
        }
        return quests;
    }

    private void displayQuests(List<Quest> quests) {
        uiManager.clearLoadedAssets();

        if(player.getInventory().getItems().size() == player.getInventory().getCapacity())
            uiManager.loadText("WARNING: Your inventory is full, you will not be receiving any chests after finishing a quest!", 1, 1, 23, 50,2);

        for (int i = 0; i < quests.size(); i++) {
            Quest quest = quests.get(i);
            int xPosition = 1 + i * 22;
            uiManager.loadAsset(quest.getAssetId(), xPosition, 2);
            uiManager.loadText(quest.getName(), 7, xPosition + 3, 3, 14,2);
            uiManager.loadText(quest.getDescription(), 7, xPosition + 3, 6, 14, 3);
            StringBuilder diffStr = new StringBuilder();
            int diffColor = 0;
            for(int j=0; j < quest.getDifficulty(); j++)
            {
                diffStr.append("*");
            }

            if(quest.getDifficulty() <= 3)
                diffColor = 118;
            if(quest.getDifficulty() >3 && quest.getDifficulty() <= 5)
                diffColor = 190;
            if (quest.getDifficulty() > 5 && quest.getDifficulty() <= 7)
                diffColor = 226;
            if (quest.getDifficulty() > 7 && quest.getDifficulty() <= 9)
                diffColor = 214;
            if (quest.getDifficulty() > 9)
                diffColor = 196;

            uiManager.loadText(diffStr.toString(), diffColor, xPosition + 4,13, 10, 1);
            uiManager.loadText(Integer.toString(quest.getLevel()), 10, xPosition + 10, 10, 7, 1);
            uiManager.loadText(Integer.toString(quest.getExpReward()), 10, xPosition + 10, 15, 7, 1);

            int finalSRate = calculateSuccessProbability(quest);
            int color = 0;
            if(finalSRate < 10)
                color = 160;
            if(finalSRate >= 10 && finalSRate < 40)
                color = 202;
            if(finalSRate >= 40 && finalSRate < 60)
                color = 220;
            if(finalSRate >= 60 && finalSRate < 90)
                color = 154;
            if (finalSRate >= 90)
                color = 82;
            uiManager.loadText(Integer.toString(finalSRate), color, xPosition + 10, 16, 10, 1);
        }

        // Highlight the selected quest
        int highlightX = 1 + cursorIndex * 22;
        uiManager.loadText("==\\/==", 10, highlightX + 7, 1, 7,1 );
        uiManager.loadText("==/\\==", 10, highlightX + 7, 18, 7,1 );

        uiManager.drawScreen();
    }

    private void handleInput(char input, List<Quest> quests) {
        switch (input) {
            case 'a':
            case 'A':
                moveCursor(-1, quests.size());
                break;
            case 'd':
            case 'D':
                moveCursor(1, quests.size());
                break;
            case 'e':
            case 'E':
                resolveQuest(quests.get(cursorIndex));
                break;
            case 'i':
            case 'I':
                StateManager.getInstance().setState(GameState.INVENTORY_VIEW);
                break;
            case 'l':
            case 'L':
                GameData.savePlayerData();
            default:
                break;
        }
    }

    private void moveCursor(int direction, int totalQuests) {
        cursorIndex += direction;
        if (cursorIndex < 0) {
            cursorIndex = totalQuests - 1;
        } else if (cursorIndex >= totalQuests) {
            cursorIndex = 0;
        }
        renderQuests();
    }

    private boolean resolveQuest(Quest quest) {
        int successProbability = calculateSuccessProbability(quest);
        int roll = random.nextInt(100); // Random roll from 0 to 99
        boolean isSuccessful = roll < successProbability;
        generateNewQuests = true;

        if (isSuccessful) {
            displaySuccessScreen(quest);
        } else {
            displayFailureScreen();
        }
        return isSuccessful;
    }

    private int calculateSuccessProbability(Quest quest) {
        int baseSuccessRate = 60; // Base success rate
        Map<String, Integer> recommendedStats = quest.getMinimumStats().toMap();

        for (Map.Entry<String, Integer> entry : recommendedStats.entrySet()) {
            String stat = entry.getKey();
            int recommendedValue = entry.getValue();
            int playerValue = player.getStats().getStatValue(stat);

            int diff = playerValue - recommendedValue;
            if(stat == "health" || stat == "mana")
                diff /= 20;
            if (playerValue >= recommendedValue) {
                baseSuccessRate += diff * 1; // Gain 1% for each point meeting/exceeding the requirement
            } else {
                baseSuccessRate += diff * 2; // Lose 2% for each point they are under
            }
        }

        System.out.println(baseSuccessRate);
        // Ensure probability is within 1% to 99% to maintain some chance of success/failure
        return Math.min(Math.max(baseSuccessRate, 1), 99);
    }

    private void displaySuccessScreen(Quest quest) {
        player.getStats().addExp(quest.getExpReward());
        boolean isInvFull = player.getInventory().getItems().size() == player.getInventory().getCapacity();

        uiManager.clearLoadedAssets();
        uiManager.loadAsset("success-screen", 0, 0);
        uiManager.loadText("Quest Completed Successfully!", 10, 2, 2, 60, 2);
        uiManager.loadText("Experience Gained: " + quest.getExpReward(), 10, 2, 4, 60, 2);
        uiManager.loadText("Current Level: " + player.getStats().getLvl(), 10, 2, 5, 60, 2);
        if(!isInvFull)
        {
            uiManager.loadText("You have won a chest!", 10, 2, 7, 60, 2);
            uiManager.loadText("Press 'O' to open the reward chest", 10, 2, 8, 60, 2);
        }

        uiManager.drawScreen();
        if(!isInvFull)
        {
            waitForInput('o');

            //TODO: make sure it returns better chest instead of random chests
            StateManager.getInstance().setState(GameState.CHEST_OPENING);
        }else {
            sleep(2500);
            renderQuests();
        }
    }

    private void displayFailureScreen() {
        uiManager.clearLoadedAssets();
        uiManager.loadAsset("failure-screen", 0, 0);
        uiManager.loadText("Quest Failed!", 1, 2, 2, 60, 2);
        uiManager.loadText("You failed the quest. Please try again.", 1, 2, 4, 60, 2);

        uiManager.drawScreen();
        sleep(2000);
        renderQuests();
    }

    private char readInput() {
        System.out.print("Input: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return ' '; // Return a default character or handle it as needed
        }
        return input.charAt(0);
    }

    private void waitForInput(char expectedInput) {
        char input;
        do {
            input = readInput();
        } while (input != expectedInput);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
