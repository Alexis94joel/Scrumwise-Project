// Class to represent a Utility property
public class Utility extends Property {
    public Utility(String name, int price, int rent) {
        super(name, price, rent);
    }

    @Override
    public void landOnProperty(Player player, int diceRoll) {
        if (getOwner() != null && getOwner() != player) {
            int rent = calculateUtilityRent(diceRoll, getOwner());
            payRent(player, rent);
        }
    }

    private int calculateUtilityRent(int diceRoll, Player owner) {
        int utilityCount = (int) owner.getOwnedProperties().stream().filter(p -> p instanceof Utility).count();
        return (utilityCount == 1) ? diceRoll * 4 : diceRoll * 10;
    }
}