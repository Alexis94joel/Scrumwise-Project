public class GainMoneyCard extends CommunityChestCard {
    private final int amount;

    public GainMoneyCard(String description, int amount) {
        super(description);
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        player.addMoney(amount);
        System.out.println(player.getName() + " receives $" + amount);
    }
}