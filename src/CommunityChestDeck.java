import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// Manages the deck of Community Chest cards
public class CommunityChestDeck {
    // List to hold all Community Chest cards
    private final List<CommunityChestCard> cards = new LinkedList<>();

    // Index of the next card to be drawn
    private int currentIndex = 0;

    // Constructor initializes and shuffles the deck
    public CommunityChestDeck() {
        initializeDeck(); // Add predefined cards to the deck
        shuffleDeck();    // Randomize the order of the cards
    }

    // Adds all predefined Community Chest cards to the deck
    private void initializeDeck() {
        cards.add(new MoneyCard("You inherit $100", 100));
        cards.add(new MoneyCard("Doctor's fee. Pay $50", -50));

        cards.add(new GainMoneyCard("Bank error in your favor. Collect $200.", 200));
        cards.add(new GainMoneyCard("You inherit $100.", 100));

        cards.add(new PayMoneyCard("Pay hospital fees of $100.", 100));
        cards.add(new PayMoneyCard("Doctor's fees. Pay $50.", 50));

        cards.add(new MoveToSpaceCard("Advance to Go (Collect $200)", 0));

        cards.add(new GetOutOfJailCard("Get out of Jail Free. Keep until needed or sold."));
    }

    // Randomizes the order of the cards in the deck
    private void shuffleDeck() {
        Collections.shuffle(cards);
    }

    // Draws the next card from the deck and advances the index
    // Loops back to the start when the end of the deck is reached
    public CommunityChestCard drawCard() {
        CommunityChestCard card = cards.get(currentIndex);
        currentIndex = (currentIndex + 1) % cards.size(); // Ensure index wraps around
        return card;
    }
}
