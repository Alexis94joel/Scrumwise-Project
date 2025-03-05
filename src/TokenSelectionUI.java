import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TokenSelectionUI extends JFrame {
    private final List<Player> players;
    private final TokenManager tokenManager;
    private int currentPlayerIndex = 0;
    private final JComboBox<String> tokenDropdown;
    private final JLabel playerLabel;
    private final Runnable onComplete;

    public TokenSelectionUI(List<Player> players, TokenManager tokenManager, Runnable onComplete) {
        this.players = players;
        this.tokenManager = tokenManager;
        this.onComplete = onComplete;

        setTitle("Select Your Token");
        setSize(400, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window

        playerLabel = new JLabel();
        tokenDropdown = new JComboBox<>();
        JButton confirmButton = new JButton("Confirm Token");

        confirmButton.addActionListener(e -> selectToken());

        add(playerLabel);
        add(new JLabel("Choose your token:"));
        add(tokenDropdown);
        add(confirmButton);

        updateUIForCurrentPlayer();
        setVisible(true);
    }

    private void updateUIForCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            playerLabel.setText("Player " + (currentPlayerIndex + 1) + " - " + currentPlayer.getName());

            tokenDropdown.setModel(new DefaultComboBoxModel<>(tokenManager.getTokens().toArray(new String[0])));
        } else {
            SwingUtilities.invokeLater(onComplete);
            dispose();
        }
    }

    private void selectToken() {
        String selectedToken = (String) tokenDropdown.getSelectedItem();
        Player currentPlayer = players.get(currentPlayerIndex);

        if (selectedToken != null && tokenManager.assignToken(currentPlayer, selectedToken)) {
            JOptionPane.showMessageDialog(this, currentPlayer.getName() + " selected " + selectedToken);
            currentPlayerIndex++;
            updateUIForCurrentPlayer();
        } else {
            JOptionPane.showMessageDialog(this, "Token unavailable. Choose another.");
        }
    }
}