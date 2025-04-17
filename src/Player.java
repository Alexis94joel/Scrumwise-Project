import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final Color color;
    private int money = 1500;
    private boolean eliminated = false;
    private int position = 0;
    private String token;
    private final List<Property> ownedProperties = new ArrayList<>();

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getMoney() {
        return money;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public int getPosition() {
        return position;
    }

    public void move(int steps) {
        position = (position + steps) % 40;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void eliminate() {
        this.eliminated = true;
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

    // ✅ Alias method for compatibility with GameUI and other classes
    public List<Property> getProperties() {
        return getOwnedProperties();
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
}
