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

    public void move(int steps) {
        position = (position + steps) % 40;
    }

    /**
     * Deducts money from the player's balance.
     * Ensures the player has enough money before deducting.
     * If not enough money, prints a warning (bankruptcy handling can be added later).
     */
    public void deductMoney(int amount) {
        if (amount > 0 && money >= amount) {
            money -= amount;
        } else {
            System.out.println(name + " does not have enough money!");
            // Add bankruptcy handling logic if needed
        }
    }

    /**
     * Adds money to the player's balance.
     * This can be used for collecting rent, passing GO, or other transactions.
     */
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
}