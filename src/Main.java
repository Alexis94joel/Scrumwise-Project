import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create two players
        Player alice = new Player("Alice", Color.BLUE);
        Player bob = new Player("Bob", Color.RED);

        // Create properties using the correct constructor
        Property mediterranean = new Property("Mediterranean Avenue", 60, 2, new int[]{10, 30, 90, 160, 250});
        Property baltic = new Property("Baltic Avenue", 60, 4, new int[]{20, 60, 180, 320, 450});

        // Assign properties to Alice
        mediterranean.purchase(alice);
        baltic.purchase(alice);

        // Bob lands on Mediterranean (should pay double rent because Alice owns the full set)
        System.out.println("\n--- Bob lands on Mediterranean (no houses) ---");
        mediterranean.landOnProperty(bob, 0);

        // Alice builds a house on Mediterranean
        System.out.println("\n--- Alice builds 1 house on Mediterranean ---");
        mediterranean.buildHouse();

        // Bob lands again, now rent should be higher (not doubled base)
        System.out.println("\n--- Bob lands on Mediterranean (1 house) ---");
        mediterranean.landOnProperty(bob, 0);
    }
}
