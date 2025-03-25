import java.util.List;

// Class to manage the game board and property interactions
public class Board {
    private List<Property> properties;

    public Board(List<Property> properties) {
        this.properties = properties;
    }

    public Property getProperty(int position)
    {
        return properties.get(position);
    }

    public void landOnProperty(Player player, int diceRoll) {
        Property property = properties.get(player.getPosition());
        property.landOnProperty(player, diceRoll); // Pass the diceRoll
    }
}
