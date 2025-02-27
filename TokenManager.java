import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public static class TokenManager {
    private final List<String> availableTokens;
    private final List<String> assignedTokens;

    public TokenManager() {
        availableTokens = new ArrayList<>(List.of(
                "Top Hat", "Battleship", "Racecar", "Thimble",
                "Boot", "Dog", "Wheelbarrow", "Iron"
        ));
        assignedTokens = new ArrayList<>();
    }

    public List<String> getAvailableTokens() {
        return new ArrayList<>(availableTokens);
    }

    public boolean assignToken(String token) {
        if (availableTokens.contains(token)) {
            availableTokens.remove(token);
            assignedTokens.add(token);
            return true; // Token successfully assigned
        }
        return false; // Token not available
    }

    public void resetTokens() {
        availableTokens.addAll(assignedTokens);
        assignedTokens.clear();
    }

    public void displayAvailableTokens() {
        System.out.println("Available Tokens:");
        for (String token : availableTokens) {
            System.out.println("- " + token);
        }
    }
}

    public static void main(String[] args) {
        TokenManager tokenManager = new TokenManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Monopoly Token Selection!");
        while (true) {
            System.out.println("\nCurrent available tokens:");
            tokenManager.displayAvailableTokens();

            System.out.print("Enter a token name to select it (or type 'exit' to quit): ");
            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("exit")) {
                System.out.println("Exiting token selection. Have a great game!");
                break;
            }

            if (tokenManager.assignToken(choice)) {
                System.out.println("You have successfully chosen: " + choice);
            } else {
                System.out.println("Token not available or invalid. Try again.");
            }
        }

        scanner.close();
    }