import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Class to represent a player in the game
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

    public void deductMoney(int amount) {
        if (amount > 0 && money >= amount) {
            money -= amount;
        } else {
            JOptionPane.showMessageDialog(null, name + " does not have enough money!");
            handleBankruptcy();
        }
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
}