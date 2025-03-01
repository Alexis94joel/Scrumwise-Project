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

        // Create a player
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        Player player = new Player(playerName);

        // Token Selection Loop
        while (player.getToken() == null) {
            System.out.println("\nAvailable Tokens:");
            tokenManager.displayAvailableTokens();

            System.out.print("Choose a token: ");
            String chosenToken = scanner.nextLine().trim();

            if (tokenManager.assignToken(chosenToken)) {
                player.setToken(chosenToken);
                System.out.println("Token assigned successfully!");
            } else {
                System.out.println("Invalid or unavailable token. Try again.");
            }
        }

        // Display updated player profile
        player.displayProfile();
        scanner.close();
    }