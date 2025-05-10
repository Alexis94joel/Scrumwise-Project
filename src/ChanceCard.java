public class ChanceCard {
    private final String description;
    private final Consumer<Player> action;

    public ChanceCard(String description, Consumer<Player> action) {
        this.description = description;
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void apply(Player player) {
        action.accept(player);
    }
}