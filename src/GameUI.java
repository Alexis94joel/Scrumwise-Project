import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Collections;

// Represents the main game UI for Monopoly.
public class GameUI {
    private final List<Player> players; // List of players in the game
    private final DisplayToken board; // The board display for player tokens
    private final JLabel[] profiles; // Labels displaying player information
    private final JLabel diceLabel; // Label to show dice roll results
    private int currentIndex = 0; // Tracks the current player's turn

    // Constructs the Monopoly game UI.
    public GameUI(List<Player> players) {
        this.players = players;

        // Shuffle the players list to randomize turn order
        Collections.shuffle(players);

        // Create the main game window
        JFrame frame = new JFrame("Monopoly");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize and add the board display
        board = new DisplayToken(players);
        frame.add(board, BorderLayout.CENTER);

        // Create the side panel for player information and controls
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        // Initialize player profile labels
        profiles = new JLabel[players.size()];
        for (int i = 0; i < players.size(); i++) {
            profiles[i] = new JLabel();
            profiles[i].setFont(new Font("Arial", Font.PLAIN, 16));
            sidePanel.add(profiles[i]); // Add each player profile to the side panel
        }

        // Label to display dice roll results
        diceLabel = new JLabel("Roll the dice!");
        diceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Button to roll the dice
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.addActionListener(e -> rollDice()); // Assign action to roll dice when clicked

        // Add dice label and roll button to the side panel
        sidePanel.add(diceLabel);
        sidePanel.add(rollButton);

        // Add the side panel to the game window
        frame.add(sidePanel, BorderLayout.EAST);

        updateProfiles(); // Update player profiles with initial values
        frame.setVisible(true); // Display the game window
    }

    // Simulates rolling two dice, updates the player's position, and moves to the next player's turn.
    private void rollDice() {
        Player player = players.get(currentIndex);
        Random rand = new Random();
        int roll = rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
        diceLabel.setText(player.getName() + " rolled: " + roll);
        player.move(roll);

        if (player.getMoney() < 0) {
            handleBankruptcy(player);
        } else {
            board.repaint();
            updateProfiles();
            currentIndex = (currentIndex + 1) % players.size();
        }
        checkForWinner();
    }

    public void handleBankruptcy(Player player) {
        JOptionPane.showMessageDialog(null, player.getName() + " is bankrupt!");

        // Iterate through the player's properties
        List<Property> bankruptPlayerProperties = player.getProperties();
        for (Property property : bankruptPlayerProperties) {
            property.getOwner();
        }
        bankruptPlayerProperties.clear(); //clear the properties

        // Remove the player from the game.
        for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
            Player p = it.next();
            if (p == player) {
                it.remove(); // Safely remove the player
                break; // Important:  We've removed the player, so exit the loop
            }
        }

        if (currentIndex >= players.size()) {
            currentIndex = 0; // Reset currentIndex if it's out of bounds
        }
        updateProfiles();
        board.repaint();
        checkForWinner(); //check winner
    }

    private void checkForWinner() {
        if (players.size() == 1) {
            JOptionPane.showMessageDialog(null, players.get(0).getName() + " is the winner!");
            System.exit(0);
        }
    }

    // Updates the side panel to reflect the latest player details, including name, token, and money.
    private void updateProfiles() {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            profiles[i].setText(p.getName() + " | Token: " + p.getToken() + " | $" + p.getMoney());
        }
    }
}