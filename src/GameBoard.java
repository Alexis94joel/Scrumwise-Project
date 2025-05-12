import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    // A static map to store properties associated with each board position (1 to 40).
    // Each position key maps to a Property, Railroad, or Utility instance.
    private static final Map<Integer, Property> properties = new HashMap<>();

    /**
     * Initializes the Monopoly game board by populating the properties map with Property, Railroad,
     * and Utility instances at their respective board positions.
     *
     * This method should be called once at the start of the game to set up all ownable spaces.
     */
    public static void initializeBoard() {
        properties.clear();

        // Brown
        properties.put(1, new Property("Mediterranean Avenue", 60, 2, new int[]{2, 10, 30, 90, 160}, 50));
        properties.put(3, new Property("Baltic Avenue", 60, 4, new int[]{4, 20, 60, 180, 320}, 50));

        // Railroad
        properties.put(5, new Railroad("Reading Railroad", 200, 25));

        // Light Blue
        properties.put(6, new Property("Oriental Avenue", 100, 6, new int[]{6, 30, 90, 270, 400}, 50));
        properties.put(8, new Property("Vermont Avenue", 100, 6, new int[]{6, 30, 90, 270, 400}, 50));
        properties.put(9, new Property("Connecticut Avenue", 120, 8, new int[]{8, 40, 100, 300, 450}, 50));

        // Pink
        properties.put(11, new Property("St. Charles Place", 140, 10, new int[]{10, 50, 150, 450, 625}, 100));
        properties.put(12, new Utility("Electric Company", 150, 0));
        properties.put(13, new Property("States Avenue", 140, 10, new int[]{10, 50, 150, 450, 625}, 100));
        properties.put(14, new Property("Virginia Avenue", 160, 12, new int[]{12, 60, 180, 500, 700}, 100));

        // Railroad
        properties.put(15, new Railroad("Pennsylvania Railroad", 200, 25));

        // Orange
        properties.put(16, new Property("St. James Place", 180, 14, new int[]{14, 70, 200, 550, 750}, 100));
        properties.put(18, new Property("Tennessee Avenue", 180, 14, new int[]{14, 70, 200, 550, 750}, 100));
        properties.put(19, new Property("New York Avenue", 200, 16, new int[]{16, 80, 220, 600, 800}, 100));

        // Red
        properties.put(21, new Property("Kentucky Avenue", 220, 18, new int[]{18, 90, 250, 700, 875}, 150));
        properties.put(23, new Property("Indiana Avenue", 220, 18, new int[]{18, 90, 250, 700, 875}, 150));
        properties.put(24, new Property("Illinois Avenue", 240, 20, new int[]{20, 100, 300, 750, 925}, 150));

        // Railroad
        properties.put(25, new Railroad("B&O Railroad", 200, 25));

        // Yellow
        properties.put(26, new Property("Atlantic Avenue", 260, 22, new int[]{22, 110, 330, 800, 975}, 150));
        properties.put(27, new Property("Ventnor Avenue", 260, 22, new int[]{22, 110, 330, 800, 975}, 150));
        properties.put(28, new Utility("Water Works", 150, 0));
        properties.put(29, new Property("Marvin Gardens", 280, 24, new int[]{24, 120, 360, 850, 1025}, 150));

        // Green
        properties.put(31, new Property("Pacific Avenue", 300, 26, new int[]{26, 130, 390, 900, 1100}, 200));
        properties.put(32, new Property("North Carolina Avenue", 300, 26, new int[]{26, 130, 390, 900, 1100}, 200));
        properties.put(34, new Property("Pennsylvania Avenue", 320, 28, new int[]{28, 150, 450, 1000, 1200}, 200));

        // Railroad
        properties.put(35, new Railroad("Short Line Railroad", 200, 25));

        // Dark Blue
        properties.put(37, new Property("Park Place", 350, 35, new int[]{35, 175, 500, 1100, 1300}, 200));
        properties.put(39, new Property("Boardwalk", 400, 50, new int[]{50, 200, 600, 1400, 1700}, 200));
    }


    /**
     * Returns a reference to the full map of all properties on the board.
     * Useful when you want to iterate over or inspect all properties.
     *
     * @return a map of all properties keyed by board position
     */
    public static Map<Integer, Property> getAllProperties() {
        return properties;
    }

    /**
     * Retrieves the property object located at the specified board position.
     *
     * @param position the numeric position on the board (1 to 40)
     * @return the Property (or subclass) object at that position, or null if none exists
     */
    public static Property getPropertyAt(int position) {
        return properties.get(position);
    }
}
