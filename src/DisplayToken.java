import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayToken extends JPanel {
    private final int CELL_SIZE = 55; // Size of normal spaces
    private final int CORNER_SIZE = CELL_SIZE * 2; // Bigger corner spaces
    private final int BOARD_SIZE = CELL_SIZE * 9 + CORNER_SIZE * 2; // Adjusted board size
    private final List<Player> players; // List of players to be displayed on the board

    public DisplayToken(List<Player> players) {
        this.players = players;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g); // Draw the Monopoly board
        drawPlayers(g); // Draw the players on the board
    }

    // Method to draw the Monopoly board
    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(50, 50, BOARD_SIZE, BOARD_SIZE); // Draw outer square

        // Draw the four corners (bigger than regular spaces)
        g.drawRect(50, 50, CORNER_SIZE, CORNER_SIZE); // Bottom-right (GO)
        g.drawRect(50 + BOARD_SIZE - CORNER_SIZE, 50, CORNER_SIZE, CORNER_SIZE); // Bottom-left (Jail)
        g.drawRect(50, 50 + BOARD_SIZE - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE); // Top-right (Go to Jail)
        g.drawRect(50 + BOARD_SIZE - CORNER_SIZE, 50 + BOARD_SIZE - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE); // Top-left (Free Parking)

        // Draw normal spaces (along the edges)
        for (int i = 0; i < 9; i++) {
            // Bottom row
            g.drawRect(50 + CORNER_SIZE + i * CELL_SIZE, 50 + BOARD_SIZE - CORNER_SIZE, CELL_SIZE, CORNER_SIZE);
            // Top row
            g.drawRect(50 + CORNER_SIZE + i * CELL_SIZE, 50, CELL_SIZE, CORNER_SIZE);
            // Left column
            g.drawRect(50, 50 + CORNER_SIZE + i * CELL_SIZE, CORNER_SIZE, CELL_SIZE);
            // Right column
            g.drawRect(50 + BOARD_SIZE - CORNER_SIZE, 50 + CORNER_SIZE + i * CELL_SIZE, CORNER_SIZE, CELL_SIZE);
        }
    }

    // Method to draw players on the board
    private void drawPlayers(Graphics g) {

        List<int[]> playerPositions = new ArrayList<>(); // List to track occupied positions

        for (Player player : players) {
            int[] cords = getBoardPosition(player.getPosition()); // Get board coordinates

            int offsetX = 0;
            int offsetY = 0;

            // Check if another player is already in the same position
            for (int j = 0; j < playerPositions.size(); j++) {
                if (playerPositions.get(j)[0] == cords[0] && playerPositions.get(j)[1] == cords[1]) {
                    // If overlap happens, adjust the offset
                    offsetX = (j + 1) * 12;
                    offsetY = (j + 1) * 12;
                }
            }

            // Add the new position to the list
            playerPositions.add(new int[]{cords[0] + offsetX, cords[1] + offsetY});

            // Draw the player with the adjusted position
            g.setColor(player.getColor());
            g.fillOval(cords[0] + offsetX + 5, cords[1] + offsetY + 5, 15, 15);
        }
    }

    // Method to get the coordinates of a board position
    private int[] getBoardPosition(int position) {
        int x = 0, y = 0;
        position = position % 40; // Ensure position wraps around

        if (position < 10) { // Bottom row
            x = 50 + CORNER_SIZE + (9 - position) * CELL_SIZE;
            y = 50 + BOARD_SIZE - CORNER_SIZE;
        } else if (position < 20) { // Left column
            x = 50;
            y = 50 + CORNER_SIZE + (19 - position) * CELL_SIZE;
        } else if (position < 30) { // Top row
            x = 50 + CORNER_SIZE + (position - 20) * CELL_SIZE;
            y = 50;
        } else { // Right column
            x = 50 + BOARD_SIZE - CORNER_SIZE;
            y = 50 + CORNER_SIZE + (position - 30) * CELL_SIZE;
        }

        return new int[]{x, y}; // Return calculated coordinates
    }
}