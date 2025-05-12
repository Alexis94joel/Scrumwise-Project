// Abstract base class for all Community Chest cards
public abstract class CommunityChestCard {
    // Description of the card's effect (e.g., "Bank error in your favor")
    private final String description;

    // Constructor sets the description when the card is created
    public CommunityChestCard(String description) {
        this.description = description;
    }

    // Returns the description of the card
    public String getDescription() {
        return description;
    }

    // Abstract method that must be implemented by each specific card
    // Defines what happens when a player draws this card
    public abstract void apply(Player player);
}
