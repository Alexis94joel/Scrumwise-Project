public class GainMoneyCard extends CommunityChestCard {
    // The amount of money that the player gains from this card
    private final int amount;

    // Constructor initializes the card with a description and an amount
    public GainMoneyCard(String description, int amount) {
        super(description);  // Call the superclass constructor with the description
        this.amount = amount; // Set the amount the player will gain
    }

    // Applies the effect of the card to the given player by adding the specified amount of money
    @Override
    public void apply(Player player) {
        player.addMoney(amount);  // Add the specified amount of money to the player's balance
        System.out.println(player.getName() + " receives $" + amount);  // Print a message indicating the player's gain
    }
}

