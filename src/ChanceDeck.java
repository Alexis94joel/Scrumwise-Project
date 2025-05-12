import java.util.*;

public class ChanceDeck {
    // Singleton instance of the deck
    private static ChanceDeck instance;

    // Queue to store the shuffled chance cards
    private final Queue<ChanceCard> cards;

    // Private constructor to prevent external instantiation (singleton pattern)
    private ChanceDeck() {
        cards = new LinkedList<>();
        initializeCards();  // Add predefined cards
        shuffleDeck();      // Shuffle them
    }

    // Returns the singleton instance of ChanceDeck, creating it if necessary
    public static ChanceDeck getInstance() {
        if (instance == null) {
            instance = new ChanceDeck();
        }
        return instance;
    }

    // Adds predefined Chance cards with associated actions to the deck
    private void initializeCards() {
        cards.add(new ChanceCard("Advance to GO", player -> player.moveTo(0)));
        cards.add(new ChanceCard("Bank pays you dividend of $50", player -> player.addMoney(50)));
        cards.add(new ChanceCard("Go back 3 spaces", player -> player.moveBack(3)));
        cards.add(new ChanceCard("Pay poor tax of $15", player -> player.deductMoney(15, null)));
        cards.add(new ChanceCard("Go to Jail – do not pass GO", player -> player.sendToJail()));
        // Add more cards as needed
    }

    // Shuffles the current deck of cards to ensure randomness
    private void shuffleDeck() {
        List<ChanceCard> tempList = new ArrayList<>(cards); // Copy current cards
        Collections.shuffle(tempList);                      // Shuffle the copy
        cards.clear();                                      // Clear original deck
        cards.addAll(tempList);                             // Re-add shuffled cards
    }

    // Draws the next card from the top of the deck and cycles it to the bottom
    public ChanceCard drawCard() {
        ChanceCard card = cards.poll();  // Take the card from the front
        cards.offer(card);               // Place it at the back
        return card;
    }
}
