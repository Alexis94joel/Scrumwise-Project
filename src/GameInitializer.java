import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameInitializer {
    public static void main(String[] args) {
        TokenManager tokenManager = new TokenManager();
        List<Player> players = new ArrayList<>();
        int numPlayers;

        while (true) {
            String input = JOptionPane.showInputDialog("Enter number of players (2-8):");

            if (input == null) {
                JOptionPane.showMessageDialog(null, "Game setup canceled.");
                System.exit(0);
            }

            try {
                numPlayers = Integer.parseInt(input);
                if (numPlayers >= 2 && numPlayers <= 8) break;
                JOptionPane.showMessageDialog(null, "Please enter a number between 2 and 8.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
            }
        }

        List<String> playerNames = new ArrayList<>();
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA};

        for (int i = 1; i <= numPlayers; i++) {
            String defaultName = "Player " + i;
            String name = JOptionPane.showInputDialog(null, "Enter name for Player " + i + " (Leave blank for default):", defaultName);

            if (name == null) {
                JOptionPane.showMessageDialog(null, "Game setup canceled.");
                System.exit(0);
            }

            name = name.trim();
            if (name.isEmpty()) name = defaultName;

            while (playerNames.contains(name)) {
                name = JOptionPane.showInputDialog(null, "Name already taken. Enter a different name for Player " + i + ":", defaultName);
                if (name == null) {
                    JOptionPane.showMessageDialog(null, "Game setup canceled.");
                    System.exit(0);
                }
                name = name.trim();
                if (name.isEmpty()) name = defaultName;
            }

            playerNames.add(name);
            Color color = colors[(i - 1) % colors.length];
            players.add(new Player(name, color));
        }

        // Start token selection UI, which will then start the game
        SwingUtilities.invokeLater(() -> new TokenSelectionUI(players, tokenManager, () -> new GameUI(players)));
    }
}
