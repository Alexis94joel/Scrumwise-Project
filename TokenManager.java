import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    private final List<String> tokens = new ArrayList<>(List.of(
            "Top Hat", "Battleship", "Racecar", "Thimble",
            "Boot", "Dog", "Wheelbarrow", "Iron"
    ));

    public List<String> getTokens() { return new ArrayList<>(tokens); }
    public boolean assignToken(Player player, String token) {
        if (tokens.remove(token)) {
            player.setToken(token);
            return true;
        }
        return false;
    }
}