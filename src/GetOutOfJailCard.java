public class GetOutOfJailCard extends CommunityChestCard {
    public GetOutOfJailCard(String description) {
        super(description);
    }

    @Override
    public void apply(Player player) {
        player.addGetOutOfJailFreeCard(this); // You must implement this in Player class
        System.out.println(player.getName() + " gets a Get Out of Jail Free card.");
    }
}