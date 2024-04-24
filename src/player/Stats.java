package player;
public class Stats {
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }
    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getIntelligence() {
        return intelligence;
    }
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getDefense() {
        return defense;
    }
    public void setDefense(int defense) {
        this.defense = defense;
    }

    private int health;
    private int level;
    private int strength;
    private int agility;
    private int intelligence;
    private int defense;

    public Stats(int health, int level, int strength, int agility, int intelligence, int defense){
        this.health = health;
        this.level  = level;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.defense = defense;
    }

    public void applyItemStats(Item item) {
        this.strength += item.getStrength();
        this.agility += item.getAgility();
        this.defense += item.getDefense();
        this.intelligence += item.getIntelligence();
        this.health += item.getHealth();
    }

    public void removeItemStats(Item item) {
        this.strength -= item.getStrength();
        this.agility -= item.getAgility();
        this.defense -= item.getDefense();
        this.intelligence -= item.getIntelligence();
        this.health -= item.getHealth();
    }
}
