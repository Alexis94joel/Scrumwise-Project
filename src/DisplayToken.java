import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayToken extends JPanel {
    private final int CELL_SIZE = 55; // Size of normal spaces
    private final int CORNER_SIZE = CELL_SIZE * 2; // Bigger corner spaces
    private final int BOARD_SIZE = CELL_SIZE * 9 + CORNER_SIZE * 2; // Adjusted board size
    private final List<Player> players;

    public DisplayToken(List<Player> players) {
        this.players = players;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPlayers(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(50, 50, BOARD_SIZE, BOARD_SIZE); // Draw outer square

        // Draw the four corners (bigger than regular spaces)
        g.drawRect(50, 50, CORNER_SIZE, CORNER_SIZE); // Bottom-left (GO)
        g.drawRect(50 + BOARD_SIZE - CORNER_SIZE, 50, CORNER_SIZE, CORNER_SIZE); // Bottom-right (Jail)
        g.drawRect(50, 50 + BOARD_SIZE - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE); // Top-left (Go to Jail)
        g.drawRect(50 + BOARD_SIZE - CORNER_SIZE, 50 + BOARD_SIZE - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE); // Top-right (Free Parking)

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

    private void drawPlayers(Graphics g) {
        // Store the last player positions to handle overlaps
        List<int[]> playerPositions = new ArrayList<>();

        for (Player player : players) {
            int[] cords = getBoardPosition(player.getPosition());

            // Check if the current position is already taken by another player
            int offsetX = 0;
            int offsetY = 0;

            // Check for overlap
            for (int j = 0; j < playerPositions.size(); j++) {
                if (playerPositions.get(j)[0] == cords[0] && playerPositions.get(j)[1] == cords[1]) {
                    // If overlap happens, adjust the offset
                    offsetX = (j + 1) * 12;  // Increase the offset
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

    private int[] getBoardPosition(int position) {
        int[][] boardPositions = new int[40][2];

        int index = 0;
        // Bottom row (left to right)
        for (int i = 9; i >= 0; i--)
            boardPositions[index++] = new int[]{50 + CORNER_SIZE + i * CELL_SIZE, 50 + BOARD_SIZE - CORNER_SIZE};
        // Left column (bottom to top)
        for (int i = 8; i > 0; i--)
            boardPositions[index++] = new int[]{50, 50 + CORNER_SIZE + i * CELL_SIZE};
        // Top row (right to left)
        for (int i = 0; i < 9; i++)
            boardPositions[index++] = new int[]{50 + CORNER_SIZE + i * CELL_SIZE, 50};
        // Right column (top to bottom)
        for (int i = 1; i < 9; i++)
            boardPositions[index++] = new int[]{50 + BOARD_SIZE - CORNER_SIZE, 50 + CORNER_SIZE + i * CELL_SIZE};

        // Fix position if it's greater than 39 (ensure it wraps correctly)
        return boardPositions[position % 40];
    }
}