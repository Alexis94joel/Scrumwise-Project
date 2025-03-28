import javax.swing.*;

// Class to represent a property in the game
public class Property {
    private final String name; // Name of the property
    private final int price; // Price of the property
    private int rent = 0; // Rent of the property
    private Player owner; // Owner of the property (null if unowned)

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isAvailable() {
        return owner == null;
    }

    public void purchase(Player player) { // Method to handle the purchase of the property
        // Check if the property is available and the player has enough money
        if (isAvailable() && player.getMoney() >= price) {
            player.updateMoney(-price);
            this.owner = player;
            player.addOwnedProperty(this);
            JOptionPane.showMessageDialog(null, player.getName() + " purchased " + name + " for $" + price);
            GameUI.updateProfiles(player, owner);
        } else {
            JOptionPane.showMessageDialog(null, "Purchase failed: Either property is owned or insufficient funds.");
        }
    }
    public void payRent(Player player, int rentAmount) { // Method to handle the payment of rent
        if (owner != null && owner != player) {
            if (player.getMoney() >= rentAmount) {
                player.deductMoney(rentAmount);
                owner.addMoney(rentAmount);
                JOptionPane.showMessageDialog(null, player.getName() + " paid $" + rentAmount + " in rent to " + owner.getName());
                GameUI.updateProfiles(player, owner);
            } else {
                JOptionPane.showMessageDialog(null, player.getName() + " does not have enough money to pay rent!");
                player.handleBankruptcy();
            }
        }
    }

    // This method is intended to be overridden by subclasses like Utility
    public void landOnProperty(Player player, int diceRoll) {
        if (owner != null && owner != player) {
            payRent(player, rent);
        }
    }
    public void setOwner(Player newOwner){
        this.owner = newOwner;
    }
}