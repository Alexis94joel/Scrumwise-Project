import java.util.*;

public class ChanceDeck {
    private final Queue<ChanceCard> cards;

    public ChanceDeck() {
        cards = new LinkedList<>();
        initializeCards();
        shuffleDeck();
    }

    private void initializeCards() {
        cards.add(new ChanceCard("Advance to GO", player -> player.moveTo(0)));
        cards.add(new ChanceCard("Bank pays you dividend of $50", player -> player.addMoney(50)));
        cards.add(new ChanceCard("Go back 3 spaces", player -> player.moveBack(3)));
        cards.add(new ChanceCard("Pay poor tax of $15", player -> player.deductMoney(15)));
        cards.add(new ChanceCard("Go to Jail – do not pass GO", player -> player.sendToJail()));
        // Add more cards as needed
    }

    private void shuffleDeck() {
        List<ChanceCard> tempList = new ArrayList<>(cards);
        Collections.shuffle(tempList);
        cards.clear();
        cards.addAll(tempList);
    }

    public ChanceCard drawCard() {
        ChanceCard card = cards.poll();
        cards.offer(card);  // Put it back at the end of the deck
        return card;
    }
}