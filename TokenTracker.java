public class TokenTracker {
    private String name;
    private int position; // Board positions: 0 to 39

    public TokenTracker(String name) {
        this.name = name;
        this.position = 0; // Start at 'Go'
    }

    public void move(int steps) {
        position = (position + steps) % 40; // Wrap around the board
        System.out.println(name + " moved to position " + position);
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name + " is at position " + position;
    }
}

    public void main(String[] args) {
        TokenTracker token = new TokenTracker("Player 1");
        System.out.println(token);

        int[] dice = DiceRoll.rollDice(); // Use Alexis's DiceRoll class
        int steps = dice[0] + dice[1];
        System.out.println("Rolled: " + dice[0] + " and " + dice[1] + " (Total: " + steps + ")");

        token.move(steps); // Move token based on dice roll
        System.out.println(token); // Display new token position
    }

