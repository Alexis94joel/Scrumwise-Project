//railroad class
public class Railroad extends Property{
    private static final int[] RENT_VALUES = {25, 50, 100, 200}; // Rent increases with more railroads owned
    public Railroad(String name, int price, int rent){
        super(name, price, rent);
    }


    @Override
    public void landOnProperty(Player player, int diceRoll){
        if(getOwner() !=null && getOwner() != player){
            int rent=RENT_VALUES[Math.min(countOwnedRailroads(getOwner()) - 1, RENT_VALUES.length - 1)];
            payRent(player,rent);
        }
    }


    public int countOwnedRailroads(Player owner){
        return (int) owner.getOwnedProperties().stream().filter(p -> p instanceof Railroad).count();
    }
}