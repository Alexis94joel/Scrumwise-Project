import javax.swing.*;
import java.awt.*;

class Player {
    private String name;
    private int money;
    private int position; // 0 = GO
    private String token;

    public Player(String name) {
        this.name = name;
        this.money = 1500; // Start with $1500
        this.position = 0; // Start at GO
        this.token = null; // No token assigned initially
    }

    public void displayProfile() {
        System.out.println("\nPlayer Profile:");
        System.out.println("Player: " + this.name);
        System.out.println("Token: " + (token != null ? token : "Not assigned"));
        System.out.println("Money: $" + this.money);
        System.out.println("Position: " + position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int newPosition) {
        this.position = newPosition;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int amount) {
        this.money = amount;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}

public class DisplayToken extends JPanel {
    private Image boardImage;
    private Player player;

    public DisplayToken() {
        boardImage = new ImageIcon("MonopolyBoard.png").getImage(); // Load board image
        player = new Player("Player 1"); // Create a player
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this); // Scales board to fit

        // Draw the player token on top of the board
        g.setColor(Color.RED); // Token color
        int tokenX = 320 + (player.getPosition() * 50); // Adjust based on board layout
        int tokenY = 70; 
        g.fillOval(tokenX, tokenY, 20, 20); // Draw token as a circle
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Monopoly Board with Player Token");
        DisplayToken board = new DisplayToken();
        frame.add(board);
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
