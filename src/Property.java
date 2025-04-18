import javax.swing.*;

// Class to represent a property in the game
public class Property {
    private final String name;
    private final int price;
    private final int housePrice;
    private int rent = 0;
    private Player owner;
    private boolean isMortgaged;
    private  int houseCount;

    public Property(String name, int price, int rent, int housePrice) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null;
        this.isMortgaged = false;
        this.housePrice = housePrice;
        this.houseCount=0;
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
            payRent(player, rent);
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

    public void addHouse() {
        if(houseCount < 4)
        {
            houseCount++;
        }
        else{
            JOptionPane.showMessageDialog(null, "Maximum houses reached." );
        }
        
    }

    public int getHouseCount()
    {
        return houseCount;
    }


    public void removeHouse()
    {
        if(houseCount >0)
        {
            houseCount--;
        }
    }
    public int getHousePrice()
    {
        return housePrice;
    }
}