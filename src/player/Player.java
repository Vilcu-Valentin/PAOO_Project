package player;

import items.*;

public class Player {
    private Inventory inventory;
    private Stats stats;

    // player.Player Singleton pattern
    private static Player instance = new Player();

    public Player(){
        this.inventory = new Inventory(20);
        this.stats = new Stats(20, 1, 1,1,1,1);
    }
    public static Player getInstance() {
        return instance;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Stats getStats() {
        return stats;
    }
}