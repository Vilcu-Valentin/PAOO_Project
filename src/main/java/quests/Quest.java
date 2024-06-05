package quests;

import chests.Chest;
import player.Player;
import player.Stats;

import java.util.Random;

public class Quest {
    private Player player = Player.getInstance();
    private String name;
    private String description;
    private String assetId;
    private int level;
    private int difficulty;
    private int expReward;
    private Stats minimumStats;
    private QuestType questType;

    public Quest(String name, String description, String assetId, int level, int difficulty, int expReward, QuestType questType) {
        this.name = name;
        this.description = description;
        this.assetId = assetId;
        this.level = level;
        this.difficulty = difficulty;
        this.expReward = expReward;
        this.questType = questType;
        this.minimumStats = generateQuestStats(questType, level, difficulty);
    }

    private Stats generateQuestStats(QuestType type, int level, int difficulty)
    {
        Random random = new Random();
        double levelMultiplier = 1.0 + (level - 1) * 0.5 - 0.5;  // 50% increment per level
        double difficultyMultiplier = Math.pow(1.25, difficulty - 1) - 0.5;  // 25% exponential growth per difficulty level

        int health = (int)(20 * levelMultiplier * difficultyMultiplier);
        int mana = (int)(20 * levelMultiplier * difficultyMultiplier);
        int strength = (int)(levelMultiplier * difficultyMultiplier);
        int intelligence = (int)(levelMultiplier * difficultyMultiplier);
        int agility = (int)(levelMultiplier * difficultyMultiplier);
        int defense = (int)(levelMultiplier * difficultyMultiplier);
        int dexterity = (int)(levelMultiplier * difficultyMultiplier);

        switch (type) {
            case COMBAT:
                health *= 5;
                mana *= 0.5;
                strength *= 10;
                defense *= 8;
                intelligence *= 3;
                agility *= 3;
                dexterity *= 0.5;
                break;
            case MAGIC:
                health *= 2;
                mana *= 10;
                strength *= 1;
                defense *= 4;
                intelligence *= 8;
                agility *= 0.5;
                dexterity *= 3;
                break;
            case SCOUT:
                health *= 1;
                mana *= 4;
                strength *= 0.5;
                defense *= 0.5;
                intelligence *= 5;
                agility *= 10;
                dexterity *= 9;
                break;
            case SURVIVAL:
                health *= 10;
                mana *= 5;
                strength *= 3;
                defense *= 10;
                intelligence *= 2;
                agility *= 4;
                dexterity *= 1;
                break;
            case DIPLOMACY:
                health *= 0.5;
                mana *= 1;
                strength *= 0.5;
                defense *= 0.5;
                intelligence *= 10;
                agility *= 0.5;
                dexterity *= 10;
                break;
            case EXPLORATION:
                health *= 2;
                mana *= 1;
                strength *= 2;
                defense *= 1;
                intelligence *= 6;
                agility *= 8;
                dexterity *= 2;
                break;
            case HUNTING:
                health *= 3;
                mana *= 5;
                strength *= 8;
                defense *= 3;
                intelligence *= 4;
                agility *= 9;
                dexterity *= 2;
                break;
            case GATHERING:
                health *= 0.5;
                mana *= 2;
                strength *= 0.5;
                defense *= 1;
                intelligence *= 6;
                agility *= 0.5;
                dexterity *= 9;
                break;
            case DEFENSE:
                health *= 10;
                mana *= 5;
                strength *= 5;
                defense *= 10;
                intelligence *= 2;
                agility *= 3;
                dexterity *= 0.5;
                break;
            case HEIST:
                health *= 1;
                mana *= 2;
                strength *= 0.5;
                defense *= 0.5;
                intelligence *= 5;
                agility *= 8;
                dexterity *= 10;
                break;
        }

        // Applying random variation from 85% to 115% (0.85 to 1.15)
        double variationRange = 0.15;
        health = (int) Math.max(0, health * (1 - variationRange + 2 * variationRange * random.nextDouble()));
        mana = (int) Math.max(0, mana * (1 - variationRange + 2 * variationRange * random.nextDouble()));
        strength = (int) Math.max(0, strength * (1 - variationRange + 2 * variationRange * random.nextDouble()));
        intelligence = (int) Math.max(0, intelligence * (1 - variationRange + 2 * variationRange * random.nextDouble()));
        agility = (int) Math.max(0, agility * (1 - variationRange + 2 * variationRange * random.nextDouble()));
        defense = (int) Math.max(0, defense * (1 - variationRange + 2 * variationRange * random.nextDouble()));
        dexterity = (int) Math.max(0, dexterity * (1 - variationRange + 2 * variationRange * random.nextDouble()));

        return new Stats(health, mana, 1,0, strength,agility,intelligence,defense,dexterity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public Stats getMinimumStats() {
        return minimumStats;
    }
}
