public class GameInitializer {
    private Player player;
    private TokenTracker token;

    public GameInitializer(String playerName) {
        player = new Player(playerName); // Player starts with $1500, position 0
        token = new TokenTracker(); // Token starts at 'Go'
    }

    public void startGame() {
        System.out.println("Starting Monopoly Game...");
        player.displayProfile();
        System.out.println(token);
    }

    public static void main(String[] args) {
        GameInitializer game = new GameInitializer("Player 1");
        game.startGame();
    }

    private class TokenTracker {
    }
}