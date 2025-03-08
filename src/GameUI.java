import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class GameUI {
    private final List<Player> players;
    private final DisplayToken board;
    private final JLabel[] profiles;
    private final JLabel diceLabel;
    private int currentIndex = 0;

    public GameUI(List<Player> players) {
        this.players = players;
        // Shuffle the players list to randomize turn order
        Collections.shuffle(players);
        JFrame frame = new JFrame("Monopoly");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        board = new DisplayToken(players);
        frame.add(board, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        profiles = new JLabel[players.size()];
        for (int i = 0; i < players.size(); i++) {
            profiles[i] = new JLabel();
            profiles[i].setFont(new Font("Arial", Font.PLAIN, 16));
            sidePanel.add(profiles[i]);
        }
        diceLabel = new JLabel("Roll the dice!");
        diceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.addActionListener(e -> rollDice());
        sidePanel.add(diceLabel);
        sidePanel.add(rollButton);
        frame.add(sidePanel, BorderLayout.EAST);

        updateProfiles();
        frame.setVisible(true);
    }

    private void rollDice() {
        Player player = players.get(currentIndex);
        Random rand = new Random();
        int roll = rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
        diceLabel.setText(player.getName() + " rolled: " + roll);
        player.move(roll);
        board.repaint();
        updateProfiles();
        currentIndex = (currentIndex + 1) % players.size();
    }

    private void updateProfiles() {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            profiles[i].setText(p.getName() + " | Token: " + p.getToken() + " | $" + p.getMoney());
        }
    }
}