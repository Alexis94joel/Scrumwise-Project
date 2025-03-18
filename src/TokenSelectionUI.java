import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TokenSelectionUI extends JFrame {
    private final List<Player> players; // List of players selecting tokens
    private final TokenManager tokenManager; // Manages available tokens
    private int currentPlayerIndex = 0; // Tracks the current player selecting a token
    private final JComboBox<String> tokenDropdown; // Dropdown for selecting a token
    private final JLabel playerLabel; // Label to display the current player
    private final Runnable onComplete; // Callback function to execute after token selection is complete

    // Constructs the Token Selection UI.
    public TokenSelectionUI(List<Player> players, TokenManager tokenManager, Runnable onComplete) {
        this.players = players; // The list of players in the game.
        this.tokenManager = tokenManager; // The manager handling token assignments.
        this.onComplete = onComplete; // The callback to execute when all players have selected their tokens.

        // Set up window properties
        setTitle("Select Your Token");
        setSize(400, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen

        // Initialize UI components
        playerLabel = new JLabel();
        tokenDropdown = new JComboBox<>();
        JButton confirmButton = new JButton("Confirm Token");

        // Add event listener for the confirm button
        confirmButton.addActionListener(e -> selectToken());

        // Add components to the UI
        add(playerLabel);
        add(new JLabel("Choose your token:"));
        add(tokenDropdown);
        add(confirmButton);

        // Update UI for the first player
        updateUIForCurrentPlayer();
        setVisible(true);
    }

    // Updates the UI to display the token selection options for the current player.
    private void updateUIForCurrentPlayer() {
        if (currentPlayerIndex < players.size()) { // If all players have selected their tokens, the window closes and the next step is triggered.
            Player currentPlayer = players.get(currentPlayerIndex); // Get the current player
            playerLabel.setText("Player " + (currentPlayerIndex + 1) + " - " + currentPlayer.getName());

            // Update the dropdown with available tokens
            tokenDropdown.setModel(new DefaultComboBoxModel<>(tokenManager.getTokens().toArray(new String[0])));
        } else {
            // All players have selected their tokens, invoke the next step and close UI
            SwingUtilities.invokeLater(onComplete);
            dispose();
        }
    }

    // Handles the token selection process when the player confirms their choice.
    private void selectToken() {
        // If the token is available, it is assigned to the player; otherwise, an error message is shown.
        String selectedToken = (String) tokenDropdown.getSelectedItem();
        Player currentPlayer = players.get(currentPlayerIndex);

        if (selectedToken != null && tokenManager.assignToken(currentPlayer, selectedToken)) {
            // Display confirmation message
            JOptionPane.showMessageDialog(this, currentPlayer.getName() + " selected " + selectedToken);
            currentPlayerIndex++; // Move to the next player
            updateUIForCurrentPlayer(); // Update UI for next selection
        } else {
            // Show error message if token selection fails
            JOptionPane.showMessageDialog(this, "Token unavailable. Choose another.");
        }
    }
}