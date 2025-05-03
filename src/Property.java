import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;


// Class to represent a property in the game
public class Property {
    private final String name;
    private final int price;
    private int rent = 0;
    private Player owner;
    private boolean isMortgaged;
    private final int[] houseRents;
    private int houseCount = 0;
    private final String colorGroup;
    private int housePrice = 0;

    public Property(String name, int price, int baseRent, int[] houseRents, int housePrice) {
        this.name = name;
        this.price = price;
        this.rent = baseRent;
        this.houseRents = houseRents;
        this.owner = null;
        this.isMortgaged = false;
        this.colorGroup = determineColorGroup(name);
        this.housePrice = housePrice;
    }

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null;
        this.isMortgaged = false;
        this.houseRents = new int[0];
        this.colorGroup = "None";
        this.housePrice = housePrice;
    }

    private String determineColorGroup(String name) {
        if (name.contains("Mediterranean") || name.contains("Baltic")) return "Brown";
        if (name.contains("Oriental") || name.contains("Vermont") || name.contains("Connecticut")) return "Light Blue";
        if (name.contains("St. Charles") || name.contains("States") || name.contains("Virginia")) return "Pink";
        if (name.contains("St. James") || name.contains("Tennessee") || name.contains("New York")) return "Orange";
        if (name.contains("Kentucky") || name.contains("Indiana") || name.contains("Illinois")) return "Red";
        if (name.contains("Atlantic") || name.contains("Ventnor") || name.contains("Marvin")) return "Yellow";
        if (name.contains("Pacific") || name.contains("North Carolina") || name.contains("Pennsylvania")) return "Green";
        if (name.contains("Park") || name.contains("Boardwalk")) return "Dark Blue";
        return "None";
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        if (isMortgaged) {
            return 0;
        }
        if (houseCount > 0 && houseCount <= houseRents.length) {
            return houseRents[houseCount - 1];
        }
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
            player.deductMoney(price);
            this.owner = player;
            player.addOwnedProperty(this);
            JOptionPane.showMessageDialog(null, player.getName() + " purchased " + name + " for $" + price);
            GameUI.updateProfiles(player, owner);
        } else {
            JOptionPane.showMessageDialog(null, "Purchase failed: Either property is owned or insufficient funds.");
        }
    }

    public void payRent(Player player, int rentAmount) {
        if (owner != null && owner != player) {
            int actualRent = getRent();
            if (player.getMoney() >= actualRent) {
                player.deductMoney(actualRent);
                owner.addMoney(actualRent);
                JOptionPane.showMessageDialog(null, player.getName() + " paid $" + actualRent + " in rent to " + owner.getName());
                GameUI.updateProfiles(player, owner);
            } else {
                JOptionPane.showMessageDialog(null, player.getName() + " does not have enough money to pay rent!");
                player.handleBankruptcy();
            }
        }
    }

    public void landOnProperty(Player player, int diceRoll) {
        if (owner != null && owner != player) {
            int rentToCharge = getRent();
            if (houseCount == 0 && GameUI.playerOwnsColorGroup(owner, this)) {
                rentToCharge *= 2;
            }
            payRent(player, rentToCharge);
        }
    }

    public void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        isMortgaged = mortgaged;
    }

    public int getMortgageValue() {
        return price / 2;
    }

    public int getUnmortgageCost() {
        return (int) (getMortgageValue() * 1.1);
    }

    public int getHouseCount() {
        return houseCount;
    }

    // Modified buildHouse method to ensure HashMap update and repaint.
    public void buildHouse(Player player, HashMap<Integer, Property> properties, DisplayToken displayToken) {
        if (houseCount < 5) {
            int houseCost = getHousePrice();
            if (player.getMoney() >= houseCost) {
                player.deductMoney(houseCost);
                houseCount++;
                JOptionPane.showMessageDialog(null,
                        player.getName() + " built a house on " + name + ". Total houses: " + houseCount);
                GameUI.updateProfiles(player, owner);

                int propertyIndex = -1;
                for (Integer key : properties.keySet()) {
                    if (properties.get(key) == this) {
                        propertyIndex = key;
                        break;
                    }
                }

                if (propertyIndex != -1) {
                    properties.put(propertyIndex, this); // Update the HashMap
                    displayToken.repaint(); // Force the display to update
                } else {
                    System.err.println("Error: Property not found in HashMap when building house.");
                }

            } else {
                JOptionPane.showMessageDialog(null,
                        player.getName() + " does not have enough money to build a house on " + name);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Maximum houses reached on " + name);
        }
    }

    public void removeHouse() {
        if (houseCount > 0) {
            houseCount--;
        }
    }

    public int getHousePrice() {
        return housePrice;
    }

    public void resetHouses() {
        this.houseCount = 0;
    }

    public String getColorGroup() {
        return colorGroup;
    }

    public static int colorGroupCount(String colorGroup) {
        return switch (colorGroup.toLowerCase()) {
            case "brown", "dark blue" -> 2;
            case "light blue", "pink", "orange", "red", "yellow", "green" -> 3;
            default -> 0;
        };
    }
}