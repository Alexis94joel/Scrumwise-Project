import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    // List of available tokens in the game.
    private final List<String> tokens = new ArrayList<>(List.of(
            "Top Hat", "Battleship", "Racecar", "Thimble",
            "Boot", "Dog", "Wheelbarrow", "Iron"
    ));

    public List<String> getTokens() { return new ArrayList<>(tokens); }
    public boolean assignToken(Player player, String token) {
        if (tokens.remove(token)) { // Removes token from available list if it exists
            player.setToken(token); // Assigns the token to the player
            return true; // Token successfully assigned
        }
        return false; // Token was not available
    }
}