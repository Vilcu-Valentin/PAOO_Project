package player;

import data.GameData;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private int health;
    private int mana;
    private int exp;
    private int lvl;
    private int strength;
    private int agility;
    private int intelligence;
    private int defense;
    private int dexterity;

    public Stats(int health, int mana, int lvl, int exp, int strength, int agility, int intelligence, int defense, int dexterity) {
        this.health = health;
        this.mana = mana;
        this.lvl = lvl;
        this.exp = exp;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.defense = defense;
        this.dexterity = dexterity;
    }

    // Getters and Setters
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public int getExp() {
        return exp;
    }
    public void setExp(int exp) {
        this.exp = exp;
    }
    public void addExp(int amount)
    {
        int totalAmount = this.exp + amount;
        if(totalAmount >= GameData.getExpDataByLvl(lvl))
        {
            if(lvl + 1 > GameData.getLvlCap())
            {
                setExp(GameData.getExpDataByLvl(lvl));
            }else {

                while(totalAmount > GameData.getExpDataByLvl(lvl))
                {
                    totalAmount -= GameData.getExpDataByLvl(lvl);
                    setLvl(lvl += 1);
                    health += 20;
                    mana += 20;
                    strength += 1;
                    defense += 1;
                    intelligence += 1;
                    dexterity += 1;
                    agility += 1;
                }
                setExp(totalAmount);
            }
        }else{
            setExp(totalAmount);
        }
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

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getStatValue(String statName) {
        switch (statName.toLowerCase()) {
            case "health":
                return getHealth();
            case "mana":
                return getMana();
            case "strength":
                return getStrength();
            case "agility":
                return getAgility();
            case "intelligence":
                return getIntelligence();
            case "defense":
                return getDefense();
            case "dexterity":
                return getDexterity();
            case "exp":
                return getExp();
            case "level":
            case "lvl":
                return getLvl();
            default:
                throw new IllegalArgumentException("Stat " + statName + " does not exist");
        }
    }
    public Map<String, Integer> toMap() {
        Map<String, Integer> statsMap = new HashMap<>();
        statsMap.put("health", getHealth());
        statsMap.put("mana", getMana());
        statsMap.put("strength", getStrength());
        statsMap.put("agility", getAgility());
        statsMap.put("intelligence", getIntelligence());
        statsMap.put("defense", getDefense());
        statsMap.put("dexterity", getDexterity());
        return statsMap;
    }

}
