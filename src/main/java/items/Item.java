package items;

public class Item {
    protected String name;
    protected String assetId; // Used to search the graphical asset in the database
    protected String description; // Small description
    protected Rarity rarity;

    public Item() {}
    public Item(String name, String assetId, String description, Rarity rarity) {
        this.name = name;
        this.assetId = assetId;
        this.description = description;
        this.rarity = rarity;
    }
    public Item(Item item)
    {
        this.name = item.name;
        this.assetId = item.assetId;
        this.description = item.description;
        this.rarity = item.rarity;
    }
    public Item copy()
    {
        return new Item(this);
    }


    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAssetId() {
        return assetId;
    }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
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
}
