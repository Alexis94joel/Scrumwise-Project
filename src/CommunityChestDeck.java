import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommunityChestDeck {
    private final List<CommunityChestCard> cards = new LinkedList<>();
    private int currentIndex = 0;

    public CommunityChestDeck() {
        initializeDeck();
        shuffleDeck();
    }

    private void initializeDeck() {
        cards.add(new MoneyCard("You inherit $100", 100));
        cards.add(new MoneyCard("Doctor's fee. Pay $50", -50));
        // Add more cards as needed
    }

    private void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public CommunityChestCard drawCard() {
        CommunityChestCard card = cards.get(currentIndex);
        currentIndex = (currentIndex + 1) % cards.size(); // Loop through deck
        return card;
    }

    private void initializeDeck() {
        cards.add(new GainMoneyCard("Bank error in your favor. Collect $200.", 200));
        cards.add(new GainMoneyCard("You inherit $100.", 100));
        cards.add(new PayMoneyCard("Pay hospital fees of $100.", 100));
        cards.add(new PayMoneyCard("Doctor's fees. Pay $50.", 50));
        cards.add(new MoveToSpaceCard("Advance to Go (Collect $200)", 0));
        cards.add(new GetOutOfJailCard("Get out of Jail Free. Keep until needed or sold."));

    }

}