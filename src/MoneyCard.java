public class MoneyCard extends CommunityChestCard {
    private final int amount;

    public MoneyCard(String description, int amount) {
        super(description);
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        if (amount > 0) {
            player.addMoney(amount);
        } else {
            player.deductMoney(-amount, null); // Pass null as creditor since it's a general deduction
        }
    }
}
