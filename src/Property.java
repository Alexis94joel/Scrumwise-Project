import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


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
            int actualRent = rentAmount;
            if (player.getMoney() >= actualRent) {
                player.deductMoney(actualRent, owner);
                owner.addMoney(actualRent);
                JOptionPane.showMessageDialog(null, player.getName() + " paid $" + actualRent + " in rent to " + owner.getName());
                GameUI.updateProfiles(player, owner);
            } else {
                JOptionPane.showMessageDialog(null, player.getName() + " does not have enough money to pay rent!");
                player.handleBankruptcy(owner);
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
    public void buildHouse(Player player, HashMap<Integer, Property> properties, DisplayToken board) {
        // First, ensure the player owns all properties of a color set
        String colorGroup = this.getColorGroup();
        if (player.ownsFullColorSet(colorGroup)) {
            // List of all properties of the same color group
            List<Property> colorGroupProperties = new ArrayList<>();
            for (Property p : player.getOwnedProperties()) {
                if (p.getColorGroup().equalsIgnoreCase(colorGroup)) {
                    colorGroupProperties.add(p);
                }
            }

            // Ensure houses are built evenly across the properties
            int houseCount = getHousePrice();  // Define the house price
            int numProperties = colorGroupProperties.size();

            // First, build one house on each property in the color set
            for (Property p : colorGroupProperties) {
                if (p.getHouseCount() < 5) {
                    p.buildHouse(player, properties, board);  // Build house on the property
                }
            }

            // Now, check if we can build extra houses
            int extraHouses = player.getMoney() / houseCount;  // Calculate how many houses player can afford
            extraHouses = Math.min(extraHouses, numProperties);  // We can only add as many houses as there are properties in the color set

            // Ask the player where to place the extra houses
            for (int i = 0; i < extraHouses; i++) {
                // Ask the player to choose where to place the extra house
                Property propertyToBuildOn = choosePropertyForExtraHouse(colorGroupProperties);
                propertyToBuildOn.buildHouse(player, properties, board);
            }
        } else {
            // Notify the player they need to own the full color set
            JOptionPane.showMessageDialog(null, "You must own all properties in the color set to build houses!");
        }
    }

    private Property choosePropertyForExtraHouse(List<Property> properties) {
        Property[] propertyArray = properties.toArray(new Property[0]);
        JComboBox<Property> comboBox = new JComboBox<>(propertyArray);

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Property) {
                    Property property = (Property) value;
                    setText(property.getName() + " (Houses: " + property.getHouseCount() + ")");
                }
                return this;
            }
        });

        int option = JOptionPane.showConfirmDialog(
                null,
                comboBox,
                "Choose property to build extra house",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            return (Property) comboBox.getSelectedItem();
        }

        return null;  // Cancelled or invalid
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