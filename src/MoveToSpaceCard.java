public class MoveToSpaceCard extends CommunityChestCard {
    private final int destination;

    public MoveToSpaceCard(String description, int destination) {
        super(description);
        this.destination = destination;
    }

    @Override
    public void apply(Player player) {
        player.moveTo(destination);
        System.out.println(player.getName() + " moves to space " + destination);
        GameUI.getInstance().handleSpaceAction(player); // You must define this
    }
}