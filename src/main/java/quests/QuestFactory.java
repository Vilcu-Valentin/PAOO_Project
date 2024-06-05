package quests;

public class QuestFactory {
    public static Quest createQuestFromCsv(String[] csvData) {
        String name = csvData[0];
        String description = csvData[1];
        String assetId = csvData[2];
        int level = Integer.parseInt(csvData[3]);
        int difficulty = Integer.parseInt(csvData[4]);
        int expReward = Integer.parseInt(csvData[5]);
        QuestType questType = QuestType.valueOf(csvData[6].toUpperCase());

        return new Quest(name, description, assetId, level, difficulty, expReward, questType);
    }
}
