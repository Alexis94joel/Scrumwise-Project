import java.util.Random;

public class DiceRoll {
    private final Random random = new Random();

    // Method to roll two dice
    public int[] rollDice() {
        int die1 = random.nextInt(6) + 1; // Roll first die (1-6)
        int die2 = random.nextInt(6) + 1; // Roll second die (1-6)
        return new int[]{die1, die2};
    }

    // Method to check if both dice are the same (doubles)
    public boolean isDoubles(int[] diceResults) {
        return diceResults[0] == diceResults[1]; // Check if both dice are equal
    }
}