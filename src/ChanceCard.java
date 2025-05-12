import java.util.function.Consumer;

public class ChanceCard {
    // Description of the Chance card (e.g., "Advance to GO", "Pay $50", etc.)
    private final String description;

    // Action to perform when the card is drawn, applied to the player
    private final Consumer<Player> action;

    // Constructor: Initializes the card with a description and an action
    public ChanceCard(String description, Consumer<Player> action) {
        this.description = description;
        this.action = action;
    }

    // Getter: Returns the description of the Chance card
    public String getDescription() {
        return description;
    }

    // Applies the action of the Chance card to the given player
    public void apply(Player player) {
        action.accept(player);
    }
}
