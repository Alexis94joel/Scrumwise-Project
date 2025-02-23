public class Player {
    private String name;
    private int money;

    public Player(String name) {
        this.name = name;
        this.money = 0;  // Money starts at 0
    }

    public void displayProfile() {
        System.out.println("Player: " + this.name);
        System.out.println("Money: $" + this.money);
    }

    // Getter and Setter for money (if needed in another part of your code)
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
