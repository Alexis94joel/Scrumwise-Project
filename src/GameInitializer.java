import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameInitializer {
    public static void main(String[] args) {
        TokenManager tokenManager = new TokenManager();
        List<Player> players = new ArrayList<>();
        int numPlayers = 0;
        while (true) {
            String input = JOptionPane.showInputDialog("Enter number of players (2-8):");

            // Handle cancel or closing the dialog
            if (input == null) {
                JOptionPane.showMessageDialog(null, "Game setup canceled.");
                System.exit(0); // Exit the program safely
            }

            try {
                numPlayers = Integer.parseInt(input);
                if (numPlayers >= 2 && numPlayers <= 8) break;
                JOptionPane.showMessageDialog(null, "Please enter a number between 2 and 8.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
            }
        }

        List<String> playerNames = new ArrayList<>(); // To check for duplicate names

        for (int i = 1; i <= numPlayers; i++) {
            String name;
            while (true) {
                name = JOptionPane.showInputDialog("Player " + i + " name:");

                if (name == null) {
                    JOptionPane.showMessageDialog(null, "Game setup canceled.");
                    System.exit(0);
                }

                name = name.trim(); // Remove leading/trailing whitespace

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Player name cannot be empty.");
                } else if (playerNames.contains(name)) {
                    JOptionPane.showMessageDialog(null, "Player name already exists. Please enter a unique name.");
                } else {
                    playerNames.add(name);
                    break; // Valid name entered
                }
            }

            Color color = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA}[i % 8];
            players.add(new Player(name, color));
        }

        // Now, show TokenSelectionUI before starting the game
        SwingUtilities.invokeLater(() -> new TokenSelectionUI(players, tokenManager, () -> new GameUI(players)));
    }
}