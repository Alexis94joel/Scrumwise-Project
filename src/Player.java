import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final Color color;
    private int money = 1500;
    private int position = 0;
    private String token;
    private boolean eliminated = false;
    private List<Property> ownedProperties = new ArrayList<>();

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public int getMoney() {
        return money;
    }

    public String getToken() {
        return token;
    }

    public Color getColor() {
        return color;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void eliminate() {
        this.eliminated = true;
    }

    public boolean move(int steps) {
        int oldPosition = position;
        position = (position + steps) % 40;
        return (oldPosition + steps) >= 40;
    }

    public void deductMoney(int amount, Player creditor) {
        if (amount > 0 && money >= amount) {
            money -= amount;
        } else {
            JOptionPane.showMessageDialog(null, name + " does not have enough money!");
            // If there's a creditor, send assets to the creditor; otherwise, send assets to the bank
            if (creditor != null) {
                handleBankruptcy(creditor);  // Transfer assets to the creditor
            } else {
                handleBankruptcy();  // Normal bankruptcy, assets go to the bank
            }
        }
    }
    public void deductMoney(int amount) {
        money -= amount;
    }



    public void addMoney(int amount) {
        if (amount > 0) {
            money += amount;
        }
    }

    public void updateMoney(int amount) {
        this.money += amount;
        if (this.money < 0) {
            eliminate();
        }
    }

    public void addOwnedProperty(Property property) {
        ownedProperties.add(property);
    }

    public List<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public List<Property> getProperties() {
        return ownedProperties;
    }

    public boolean ownsFullColorSet(String colorGroup) {
        long owned = ownedProperties.stream()
                .filter(p -> p.getColorGroup().equalsIgnoreCase(colorGroup))
                .count();
        return owned == Property.colorGroupCount(colorGroup);
    }

    public void handleBankruptcy() {
        if (!eliminated) {
            eliminated = true;
            JOptionPane.showMessageDialog(null, name + " is bankrupt!");
            for (Property property : ownedProperties) {
                property.setOwner(null);
            }
            ownedProperties.clear();
        }
    }

    public void handleBankruptcy(Player creditor) {
        if (!eliminated) {
            eliminated = true;
            JOptionPane.showMessageDialog(null, name + " is bankrupt and all assets are transferred to " + creditor.getName() + "!");

            // Transfer all properties to the creditor
            for (Property property : ownedProperties) {
                property.setOwner(creditor);  // Transfer ownership to creditor
                creditor.addOwnedProperty(property);  // Add property to creditor's list
            }

            ownedProperties.clear();  // Clear the bankrupt player's property list

            // Transfer money to the creditor only if it's greater than 0
            if (this.money > 0) {
                creditor.addMoney(this.money);  // Transfer remaining money to creditor
                this.money = 0;  // The player's money is now 0
            }
        }
    }


    public boolean mortgageProperty(Property property) {
        if (ownedProperties.contains(property) && !property.isMortgaged()) {
            property.setMortgaged(true);
            addMoney(property.getMortgageValue());
            JOptionPane.showMessageDialog(null, name + " mortgaged " + property.getName() + " for $" + property.getMortgageValue());
            return true;

        }
        return false;
    }

    public boolean unmortgageProperty(Property property) {
        if (ownedProperties.contains(property) && property.isMortgaged()) {
            int costToUnmortgage = property.getUnmortgageCost();
            if (money >= costToUnmortgage) {
                property.setMortgaged(false);
                deductMoney(costToUnmortgage);
                JOptionPane.showMessageDialog(null, name + " unmortgaged " + property.getName() + " for $" + costToUnmortgage);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, name + " does not have enough money to unmortgage " + property.getName());
                return false;
            }
        }
        return false;
    }

    public boolean sellHouse(Property property) {
        if (ownedProperties.contains(property) && (property.getHouseCount() > 0)) {
            int sellValue = property.getHousePrice() / 2;
            property.removeHouse();
            this.addMoney(sellValue);
            return true;
        } else {
            System.out.println(property.getName() + " has no house to sell");
            return false;
        }
    }

    public void moveTo(int position) {
        this.position = position;
        GameUI.getInstance().updateTokenPosition(this);
    }

    public void move(int spaces) {
        int newPosition = (this.position + spaces) % 40;
        this.position = newPosition;

        // Optional: handle passing GO
        if (this.position + spaces >= 40) {
            this.balance += 200;
            System.out.println(name + " passed GO and collected $200!");
        }
    }

    public void moveBack(int spaces) {
        this.position = (this.position - spaces + 40) % 40;  // wrap around
        GameUI.getInstance().updateTokenPosition(this);
    }

    private boolean inJail = false;

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean status) {
        inJail = status;
    }

    public void sendToJail() {
        this.position = 10; // Jail index
        this.inJail = true;
        GameUI.getInstance().updateTokenPosition(this);
    }

    public void goToJail() {
        this.setCurrentPosition(10);
        this.setInJail(true);        // Track jail status
    }

    private int getOutOfJailFreeCards = 0;

    public void addGetOutOfJailFreeCard() {
        getOutOfJailFreeCards++;
    }

    public boolean hasGetOutOfJailFreeCard() {
        return getOutOfJailFreeCards > 0;
    }

    public void useGetOutOfJailFreeCard() {
        if (getOutOfJailFreeCards > 0) {
            getOutOfJailFreeCards--;
        }
    }
}
