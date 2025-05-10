public abstract class CommunityChestCard {
    private final String description;

    public CommunityChestCard(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Each card must define its own action
    public abstract void apply(Player player);
}