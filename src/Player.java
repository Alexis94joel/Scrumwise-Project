import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final Color color;
    private int money = 1500; // Starting money for each player
    private int position = 0; // Position on the board (0-39)
    private String token; // The player's chosen game token
    private List<Property> properties; // List of properties owned by the player

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.properties = new ArrayList<>(); // Initialize property list
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

    public List<Property> getProperties() {
        return properties; // Returns the list of properties owned by the player
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void move(int steps) {
        position = (position + steps) % 40; // Moves the player and wraps around the board
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

    /**
     * Adds a property to the player's owned property list.
     * Ensures the player does not own duplicate properties.
     */
    public void addProperty(Property property) {
        if (!properties.contains(property)) {
            properties.add(property);
        }
    }

    /**
     * Allows the player to purchase a property if they have enough money.
     * The property must also be available (not owned by another player).
     */
    public void purchaseProperty(Property property) {
        if (money >= property.getPrice() && property.isAvailable()) {
            property.purchase(this); // Assign ownership
            addProperty(property); // Add property to player's list
            System.out.println(name + " purchased " + property.getName());
        } else {
            System.out.println(name + " cannot afford " + property.getName());
        }
    }

    /**
     * Handles the rent payment when a player lands on a property owned by another player.
     * Deducts the rent amount from the current player and gives it to the owner.
     * If the player cannot afford rent, it prints a message (bankruptcy logic can be added).
     */
    public void payRent(Property property) {
        if (property.getOwner() != null && property.getOwner() != this) {
            int rent = property.getRent();
            if (money >= rent) {
                deductMoney(rent); // Deduct rent from player
                property.getOwner().addMoney(rent); // Give rent to owner
                System.out.println(name + " paid " + rent + " in rent to " + property.getOwner().getName());
            } else {
                System.out.println(name + " does not have enough money to pay rent!");
                // Add bankruptcy handling logic here
            }
        }
    }
}