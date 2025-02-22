import java.util.Random;

public class DiceRoll {
    public static int[] rollDice() {
        Random rand = new Random();
        int die1 = rand.nextInt(6) + 1;  // Generates a number between 1 and 6
        int die2 = rand.nextInt(6) + 1;
        return new int[]{die1, die2};
    }

    public static void main(String[] args) {
                int[] dice = rollDice();
                System.out.println("You rolled: " + dice[0] + " and " + dice[1] + " (Total: " + (dice[0] + dice[1]) + ")");
    }
}
