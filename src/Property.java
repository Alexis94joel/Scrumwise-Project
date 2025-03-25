public class Property {
    private final String name;
    private final int price;
    private final int rent;
    private Player owner;

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null; // Initially, the property is unowned.
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
            owner = player;
            player.deductMoney(price);
            player.addProperty(this);
            System.out.println(player.getName() + " purchased " + name + " for $" + price);
        } else {
            System.out.println(player.getName() + " cannot afford " + name + " or it is already owned.");
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
}