import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Class to represent a property in the game
public class Property {
    private final String name;
    private final int price;
    private int rent = 0;
    private Player owner; // Null if unowned

    public Property(String name, int price, int rent) { //Added rent
        this.name = name;
        this.price = price;
        this.rent = rent; //added this
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

    public void purchase(Player player) {
        if (isAvailable() && player.getMoney() >= price) {
            player.updateMoney(-price); // Use the new updateMoney method
            this.owner = player;
            player.addOwnedProperty(this); // Use the new addOwnedProperty method
            System.out.println(player.getName() + " purchased " + name + " for $" + price);
        } else {
            System.out.println("Purchase failed: Either property is owned or insufficient funds.");
        }
    }

    public void payRent(Player player) {
        if (owner != null && owner != player) {
            if (player.getMoney() >= rent) {
                player.deductMoney(rent);
                owner.addMoney(rent);
                System.out.println(player.getName() + " paid $" + rent + " in rent to " + owner.getName());
            } else {
                System.out.println(player.getName() + " does not have enough money to pay rent!");
                // Handle bankruptcy logic here if needed
            }
        }
    }

    // This method is intended to be overridden by subclasses like Utility
    public void landOnProperty(Player player, int diceRoll) {
        if (owner != null && owner != player) {
            //  Default implementation (e.g., for regular properties)
            //  Rent calculation would happen here, and would likely involve
            //  querying the owner for rent amount, or the property itself.
            System.out.println(player.getName() + " landed on " + name + " owned by " + owner.getName());
        }
    }
}