public class PayMoneyCard extends CommunityChestCard {
    private final int amount;

    public PayMoneyCard(String description, int amount) {
        super(description);
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        player.deductMoney(amount, null);  // No creditor in this case
        System.out.println(player.getName() + " pays $" + amount);
    }
}
