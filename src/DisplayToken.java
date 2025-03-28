import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class DisplayToken extends JPanel {
    private final int CELL_SIZE = 55;
    private final int CORNER_SIZE = CELL_SIZE * 2;
    private final int BOARD_SIZE = CELL_SIZE * 9 + CORNER_SIZE * 2;
    private final List<Player> players;

    private final String[] boardSpaces = {
            "Free Parking", "Mediterranean Ave", "Community Chest", "Baltic Ave", "Income Tax",
            "Reading RR", "Oriental Ave", "Chance", "Vermont Ave", "Connecticut Ave",
            "Go To Jail", "St. Charles Place", "Electric Company", "States Ave", "Virginia Ave",
            "Pennsylvania RR", "St. James Place", "Community Chest", "Tennessee Ave", "New York Ave",
            "GO", "Kentucky Ave", "Chance", "Indiana Ave", "Illinois Ave",
            "B&O RR", "Atlantic Ave", "Ventnor Ave", "Water Works", "Marvin Gardens",
            "Jail", "Pacific Ave", "North Carolina Ave", "Community Chest", "Pennsylvania Ave",
            "Short Line RR", "Chance", "Park Place", "Luxury Tax", "Boardwalk"
    };

    private final HashMap<String, Color> propertyColors = new HashMap<>();

    public DisplayToken(List<Player> players, HashMap<Integer, Property> properties) { // Modify this
        this.players = players;
        initializePropertyColors();
    }

    private void initializePropertyColors() {
        propertyColors.put("Mediterranean Ave", new Color(139, 69, 19)); // Brown
        propertyColors.put("Baltic Ave", new Color(139, 69, 19));

        propertyColors.put("Oriental Ave", new Color(173, 216, 230)); // Light Blue
        propertyColors.put("Vermont Ave", new Color(173, 216, 230));
        propertyColors.put("Connecticut Ave", new Color(173, 216, 230));

        propertyColors.put("St. Charles Place", new Color(255, 20, 147)); // Pink
        propertyColors.put("States Ave", new Color(255, 20, 147));
        propertyColors.put("Virginia Ave", new Color(255, 20, 147));

        propertyColors.put("St. James Place", new Color(255, 140, 0)); // Orange
        propertyColors.put("Tennessee Ave", new Color(255, 140, 0));
        propertyColors.put("New York Ave", new Color(255, 140, 0));

        propertyColors.put("Kentucky Ave", new Color(255, 0, 0)); // Red
        propertyColors.put("Indiana Ave", new Color(255, 0, 0));
        propertyColors.put("Illinois Ave", new Color(255, 0, 0));

        propertyColors.put("Atlantic Ave", new Color(255, 255, 0)); // Yellow
        propertyColors.put("Ventnor Ave", new Color(255, 255, 0));
        propertyColors.put("Marvin Gardens", new Color(255, 255, 0));

        propertyColors.put("Pacific Ave", new Color(34, 139, 34)); // Green
        propertyColors.put("North Carolina Ave", new Color(34, 139, 34));
        propertyColors.put("Pennsylvania Ave", new Color(34, 139, 34));

        propertyColors.put("Park Place", new Color(0, 0, 255)); // Dark Blue
        propertyColors.put("Boardwalk", new Color(0, 0, 255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPlayers(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(50, 50, BOARD_SIZE, BOARD_SIZE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));

        drawSpace(g, 50, 50, CORNER_SIZE, CORNER_SIZE, 0); // Bottom-right (GO)
        drawSpace(g, 50 + BOARD_SIZE - CORNER_SIZE, 50, CORNER_SIZE, CORNER_SIZE, 10); // Bottom-left (Jail)
        drawSpace(g, 50, 50 + BOARD_SIZE - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE, 30); // Top-right (Go to Jail)
        drawSpace(g, 50 + BOARD_SIZE - CORNER_SIZE, 50 + BOARD_SIZE - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE, 20); // Top-left (Free Parking)

        for (int i = 0; i < 9; i++) {
            drawSpace(g, 50 + CORNER_SIZE + i * CELL_SIZE, 50 + BOARD_SIZE - CORNER_SIZE, CELL_SIZE, CORNER_SIZE, 9 - i);
            drawSpace(g, 50 + CORNER_SIZE + i * CELL_SIZE, 50, CELL_SIZE, CORNER_SIZE, 21 + i);
            drawSpace(g, 50, 50 + CORNER_SIZE + i * CELL_SIZE, CORNER_SIZE, CELL_SIZE, 19 - i);
            drawSpace(g, 50 + BOARD_SIZE - CORNER_SIZE, 50 + CORNER_SIZE + i * CELL_SIZE, CORNER_SIZE, CELL_SIZE, 31 + i);
        }
    }

    private void drawSpace(Graphics g, int x, int y, int width, int height, int index) {
        String propertyName = boardSpaces[index];

        // Check if the space has a property color
        if (propertyColors.containsKey(propertyName)) {
            g.setColor(propertyColors.get(propertyName));

            // Adjust color bar positioning based on board section
            if (x == 50) { // Left column (move bar to the left)
                g.fillRect(x, y, 15, height);
            } else if (x == 50 + BOARD_SIZE - CORNER_SIZE) { // Right column (move bar to the right)
                g.fillRect(x + width - 15, y, 15, height);
            } else { // Default (top or bottom row)
                g.fillRect(x, y, width, 15);
            }
        }

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Adjust text positioning based on location
        g.setFont(new Font("Arial", Font.PLAIN, 10));

        // Top & Bottom: Break into two lines to avoid overlap
        if (index >= 1 && index <= 9 || index >= 21 && index <= 29) {
            String[] words = propertyName.split(" ");
            String line1 = words.length > 1 ? words[0] : propertyName;
            String line2 = words.length > 1 ? propertyName.substring(line1.length()).trim() : "";

            g.drawString(line1, x + 5, y + 25);
            if (!line2.isEmpty()) g.drawString(line2, x + 5, y + 35);

            // Left & Right: Keep single line but shift position
        } else {
            g.drawString(propertyName, x + 5, y + height / 2);
        }
    }

    private void drawPlayers(Graphics g) {
        List<int[]> playerPositions = new ArrayList<>();

        for (Player player : players) {
            int[] cords = getBoardPosition(player.getPosition());
            int offsetX = 0;
            int offsetY = 0;

            for (int j = 0; j < playerPositions.size(); j++) {
                if (playerPositions.get(j)[0] == cords[0] && playerPositions.get(j)[1] == cords[1]) {
                    offsetX = (j + 1) * 12;
                    offsetY = (j + 1) * 12;
                }
            }

            playerPositions.add(new int[]{cords[0] + offsetX, cords[1] + offsetY});

            g.setColor(player.getColor());
            g.fillOval(cords[0] + offsetX + 5, cords[1] + offsetY + 5, 15, 15);
        }
    }

    private int[] getBoardPosition(int position) {
        int x = 0, y = 0;
        position = position % 40;

        if (position < 10) {
            x = 50 + CORNER_SIZE + (9 - position) * CELL_SIZE;
            y = 50 + BOARD_SIZE - CORNER_SIZE;
        } else if (position < 20) {
            x = 50;
            y = 50 + CORNER_SIZE + (19 - position) * CELL_SIZE;
        } else if (position < 30) {
            x = 50 + CORNER_SIZE + (position - 21) * CELL_SIZE;
            y = 50;
        } else {
            x = 50 + BOARD_SIZE - CORNER_SIZE;
            y = 50 + CORNER_SIZE + (position - 31) * CELL_SIZE;
        }

        return new int[]{x, y};
    }
}

