import javax.swing.*;

public class Railroad extends Property {
    private static final int[] RENT_VALUES = {25, 50, 100, 200}; // Rent increases with more railroads owned

    public Railroad(String name, int price, int rent) {
        super(name, price, rent); // Pass an empty array for house rents.  The 'rent' parameter is not used.
    }

    @Override
    public void landOnProperty(Player player, int diceRoll) {
        if (getOwner() != null && getOwner() != player) {
            int rent = RENT_VALUES[Math.min(countOwnedRailroads(getOwner()) - 1, RENT_VALUES.length - 1)];
            payRent(player, rent); // Call our overridden payRent method.
        }
    }

    @Override
    public void payRent(Player player, int rentAmount) {
        if (getOwner() != null && getOwner() != player) {
            if (player.getMoney() >= rentAmount) {
                player.deductMoney(rentAmount, getOwner());  // Pass the owner as the creditor
                getOwner().addMoney(rentAmount);  // The owner gets the rent amount
                JOptionPane.showMessageDialog(null, player.getName() + " paid $" + rentAmount + " in rent to " + getOwner().getName());
                GameUI.updateProfiles(player, getOwner()); // Add this line to update the display
            } else {
                JOptionPane.showMessageDialog(null, player.getName() + " does not have enough money to pay rent!");
                player.handleBankruptcy(getOwner());  // Transfer assets to the owner (creditor)
            }
        }
    }


    public int countOwnedRailroads(Player owner) {
        return (int) owner.getOwnedProperties().stream().filter(p -> p instanceof Railroad).count();
    }
}
