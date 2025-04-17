import javax.swing.*;

public class Property {
    private final String name;
    private final int price;
    private final int baseRent;
    private final int[] houseRents; // Rent depending on house count (up to 5 houses)
    private int houseCount = 0;
    private Player owner;
    private final String colorGroup;

    public Property(String name, int price, int baseRent, int[] houseRents) {
        this.name = name;
        this.price = price;
        this.baseRent = baseRent;
        this.houseRents = houseRents;
        this.colorGroup = determineColorGroup(name);
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

    public int getBaseRent() {
        return baseRent;
    }

    public int getRent() {
        if (houseCount > 0 && houseCount <= houseRents.length) {
            return houseRents[houseCount - 1];
        }
        return baseRent;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public String getColorGroup() {
        return colorGroup;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isAvailable() {
        return owner == null;
    }

    public void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public void resetHouses() {
        this.houseCount = 0;
    }

    public void purchase(Player player) {
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

    public void buildHouse() {
        if (houseCount < 5) {
            houseCount++;
            JOptionPane.showMessageDialog(null, owner.getName() + " built a house on " + name + ". Total houses: " + houseCount);
        } else {
            JOptionPane.showMessageDialog(null, "Maximum houses reached on " + name);
        }
    }

    public void landOnProperty(Player player, int diceRoll) {
        if (owner != null && owner != player) {
            int rentAmount = getRent();

            // Double base rent if no houses and player owns all in color group
            if (houseCount == 0 && GameUI.playerOwnsColorGroup(owner, this)) {
                rentAmount *= 2;
            }

            payRent(player, rentAmount);
        }
    }

    public void payRent(Player player, int rentAmount) {
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

    public static int colorGroupCount(String colorGroup) {
        return switch (colorGroup.toLowerCase()) {
            case "brown", "dark blue" -> 2;
            case "light blue", "pink", "orange", "red", "yellow", "green" -> 3;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return name + " ($" + price + ") | Rent: $" + getRent() + " | Houses: " + houseCount;
    }
}
