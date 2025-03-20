import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final Color color;
    private int money = 1500;
    private int position = 0;
    private String token;
    private List<Property> properties;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.properties = new ArrayList<>();
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
        return properties;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void move(int steps) {
        position = (position + steps) % 40; // Wrap around board
    }

    public void deductMoney(int amount) {
        if (amount > 0 && money >= amount) {
            money -= amount;
        } else {
            System.out.println(name + " does not have enough money!");
            // Add bankruptcy handling logic if needed
        }
    }

    public void addMoney(int amount) {
        if (amount > 0) {
            money += amount;
        }
    }

    public void addProperty(Property property) {
        if (!properties.contains(property)) {
            properties.add(property);
        }
    }

    public void purchaseProperty(Property property) {
        if (money >= property.getPrice() && property.isAvailable()) {
            property.purchase(this); // Assign ownership
            addProperty(property);
            System.out.println(name + " purchased " + property.getName());
        } else {
            System.out.println(name + " cannot afford " + property.getName());
        }
    }

    public void payRent(Property property) {
        if (property.getOwner() != null && property.getOwner() != this) {
            int rent = property.getRent();
            if (money >= rent) {
                deductMoney(rent);
                property.getOwner().addMoney(rent);
                System.out.println(name + " paid " + rent + " in rent to " + property.getOwner().getName());
            } else {
                System.out.println(name + " does not have enough money to pay rent!");
                // Add bankruptcy handling logic here
            }
        }
    }
}
