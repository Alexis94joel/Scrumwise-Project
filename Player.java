import java.awt.*;

public class Player {
    private final String name;
    private final Color color;
    private int money = 1500;
    private int position = 0;
    private String token;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name; }
    public int getPosition() {
        return position; }
    public int getMoney() {
        return money; }
    public String getToken() {
        return token; }
    public Color getColor() {
        return color; }
    public void setToken(String token) {
        this.token = token; }
    public void move(int steps) {
        position = (position + steps) % 40;  // Wrap around board
    }
}