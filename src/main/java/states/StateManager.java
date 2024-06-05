package states;

import data.GameData;
import services.ChestOpeningService;
import services.InventoryService;
import services.QuestService;

public class StateManager {
    private static StateManager instance;
    private GameState currentState;
    private InventoryService inventoryService;
    private ChestOpeningService chestOpeningService;
    private QuestService questService;

    private StateManager(InventoryService inventory, ChestOpeningService chest, QuestService quest) {
        this.inventoryService = inventory;
        this.chestOpeningService = chest;
        this.questService = quest;
        this.currentState = GameState.INVENTORY_VIEW;  // Default state
    }

    public static StateManager getInstance(InventoryService inventory, ChestOpeningService chest, QuestService quest) {
        if (instance == null) {
            instance = new StateManager(inventory, chest, quest);
        }
        return instance;
    }

    public static StateManager getInstance()
    {
        return instance;
    }

    public void setState(GameState newState) {
        this.currentState = newState;
        switch (newState) {
            case INVENTORY_VIEW:
                inventoryService.renderInventory();
                break;
            case CHEST_OPENING:
                chestOpeningService.openChest();

                break;
            case QUEST_VIEW:
                questService.renderQuests();
                break;
        }
    }
}
