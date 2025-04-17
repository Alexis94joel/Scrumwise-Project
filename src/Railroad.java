public class Railroad extends Property {

    public Railroad(String name, int price, int baseRent) {
        // Railroads don't use house rent structure, so pass empty array
        super(name, price, baseRent, new int[0]);
    }

    @Override
    public void landOnProperty(Player player, int diceRoll) {
        if (getOwner() != null && getOwner() != player) {
            int count = (int) getOwner().getOwnedProperties()
                    .stream()
                    .filter(p -> p instanceof Railroad)
                    .count();

            int rent = 25 * (int) Math.pow(2, count - 1); // Rent doubles: 25, 50, 100, 200
            payRent(player, rent);
        }
    }
}

