import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GameUI {
    private static GameUI instance;
    private final List<Player> players;
    private final DisplayToken board;
    private final JLabel[] profiles;
    private final JLabel diceLabel;
    private int currentIndex = 0;
    private final HashMap<Integer, Property> properties = new HashMap<>();
    private final JPanel sidePanel;
    private final JPanel[] playerPropertyPanels;
    private final JButton rollButton;
    private JButton mortgageButton; // Added for mortgage
    private JButton unmortgageButton; // Added for unmortgage

    public GameUI(List<Player> players) {
        instance = this;
        this.players = new ArrayList<>(players);
        JFrame frame = new JFrame("Monopoly");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        initializeProperties();
        board = new DisplayToken(this.players, properties);
        frame.add(board, BorderLayout.CENTER);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        profiles = new JLabel[this.players.size()];
        playerPropertyPanels = new JPanel[this.players.size()];
        for (int i = 0; i < this.players.size(); i++) {
            profiles[i] = new JLabel();
            profiles[i].setFont(new Font("Arial", Font.PLAIN, 16));
            sidePanel.add(profiles[i]);

            playerPropertyPanels[i] = new JPanel();
            playerPropertyPanels[i].setLayout(new BoxLayout(playerPropertyPanels[i], BoxLayout.Y_AXIS));
            sidePanel.add(playerPropertyPanels[i]);
        }
        diceLabel = new JLabel("Roll the dice!");
        diceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.addActionListener(e -> rollDice());

        mortgageButton = new JButton("Mortgage");
        mortgageButton.setFont(new Font("Arial", Font.BOLD, 16));
        mortgageButton.addActionListener(e -> showMortgageOptions());

        unmortgageButton = new JButton("Unmortgage");
        unmortgageButton.setFont(new Font("Arial", Font.BOLD, 16));
        unmortgageButton.addActionListener(e -> showUnmortgageOptions());

        sidePanel.add(diceLabel);
        sidePanel.add(rollButton);
        sidePanel.add(mortgageButton); // Add mortgage button
        sidePanel.add(unmortgageButton); // Add unmortgage button
        frame.add(sidePanel, BorderLayout.EAST);

        updateProfiles();
        frame.setVisible(true);
    }

    private void rollDice() {
        Player player = players.get(currentIndex);
        Random rand = new Random();

        int die1 = rand.nextInt(6) + 1;
        int die2 = rand.nextInt(6) + 1;
        int totalRoll = die1 + die2;
        boolean isDouble = (die1 == die2);

        diceLabel.setText(player.getName() + " rolled: " + die1 + " and " + die2 + (isDouble ? " (Double!)" : ""));

        int oldPosition = player.getPosition();
        boolean passedGO = player.move(totalRoll);

        if (passedGO || player.getPosition() == 0 && oldPosition != 0) {
            player.addMoney(200);
            GameUI.updateProfiles(player, player);
            JOptionPane.showMessageDialog(null, player.getName() + " passed GO and collected $200!");
        }

        handleSpace(player, totalRoll);

        if (player.isEliminated()) {
            handleBankruptcy(player);
        } else {
            board.repaint();
            updateProfiles();
            currentIndex = (currentIndex + 1) % players.size();
        }

        checkForWinner();
    }

    private void handleSpace(Player player, int diceRoll) {
        int playerPosition = player.getPosition();
        // take $200 income tax
        if (playerPosition == 4) {
            player.deductMoney(200);
            JOptionPane.showMessageDialog(null, player.getName() + " landed on Income Tax and paid $200!");
            GameUI.updateProfiles(player, player);
        }
        if (properties.containsKey(playerPosition)) {
            Property property = properties.get(playerPosition);

            board.repaint();

            if (property.isAvailable()) {
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to purchase " + property.getName() + " for $" + property.getPrice() + "?", "Purchase", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    if (player.getMoney() >= property.getPrice()) {
                        property.purchase(player);
                        SwingUtilities.invokeLater(() -> {
                            updateProfiles();
                            board.repaint();
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "You do not have enough money to purchase this property.");
                    }
                }
            } else {
                property.landOnProperty(player, diceRoll);
                if (player.isEliminated()) {
                    handleBankruptcy(player);
                } else {
                    SwingUtilities.invokeLater(() -> {
                        updateProfiles();
                        board.repaint();
                    });
                }
            }
        } else {
            board.repaint();
        }
    }

    private void handleBankruptcy(Player player) {
        player.handleBankruptcy();

        for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
            Player p = it.next();
            if (p == player) {
                it.remove();
                break;
            }
        }

        if (currentIndex >= players.size()) {
            currentIndex = 0;
        }
        updateProfiles();
        board.repaint();
        checkForWinner();
    }

    private void checkForWinner() {
        if (players.size() == 1) {
            JOptionPane.showMessageDialog(null, players.get(0).getName() + " is the winner!");
            System.exit(0);
        }
    }

    public static void updateProfiles() {
        if (instance != null) {
            SwingUtilities.invokeLater(() -> {
                instance.sidePanel.removeAll();
                for (int i = 0; i < instance.players.size(); i++) {
                    Player p = instance.players.get(i);
                    instance.profiles[i].setText(p.getName() + " | Token: " + p.getToken() + " | $" + p.getMoney() + (p.isEliminated() ? " (Eliminated)" : ""));
                    instance.playerPropertyPanels[i].removeAll();

                    if (!p.getProperties().isEmpty()) {
                        JLabel titleLabel = new JLabel("Properties:");
                        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        instance.playerPropertyPanels[i].add(titleLabel);
                        for (Property prop : p.getProperties()) {
                            String propertyLabelText = prop.getName() + " (Rent: $" + prop.getRent() + ")";
                            if (prop.isMortgaged()) {
                                propertyLabelText += " (Mortgaged)";
                            }
                            JLabel propertyLabel = new JLabel(propertyLabelText);
                            propertyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                            instance.playerPropertyPanels[i].add(propertyLabel);
                        }
                    }
                    instance.sidePanel.add(instance.profiles[i]);
                    instance.sidePanel.add(instance.playerPropertyPanels[i]);
                }
                instance.sidePanel.add(instance.diceLabel);
                instance.sidePanel.add(instance.rollButton);
                instance.sidePanel.add(instance.mortgageButton); // Add mortgage button
                instance.sidePanel.add(instance.unmortgageButton); // Add unmortgage button
                instance.sidePanel.revalidate();
                instance.board.repaint();
            });
        }
    }

    public static void updateProfiles(Player player, Player owner) {
        if (instance != null) {
            SwingUtilities.invokeLater(() -> {
                int payingPlayerIndex = instance.players.indexOf(player);
                int receivingPlayerIndex = instance.players.indexOf(owner);
                if (payingPlayerIndex != -1) {
                    instance.profiles[payingPlayerIndex].setText(player.getName() + " | Token: " + player.getToken() + " | $" + player.getMoney() + (player.isEliminated() ? " (Eliminated)" : ""));
                }
                if (receivingPlayerIndex != -1) {
                    instance.profiles[receivingPlayerIndex].setText(owner.getName() + " | Token: " + owner.getToken() + " | $" + owner.getMoney() + (owner.isEliminated() ? " (Eliminated)" : ""));
                }
            });
        }
    }

    private void initializeProperties() {
        properties.put(1, new Property("Mediterranean Avenue", 60, 2));
        properties.put(3, new Property("Baltic Avenue", 60, 4));
        properties.put(5, new Railroad("Reading Railroad", 200, 25));
        properties.put(6, new Property("Oriental Avenue", 100, 6));
        properties.put(8, new Property("Vermont Avenue", 100, 6));
        properties.put(9, new Property("Connecticut Avenue", 120, 8));
        properties.put(11, new Property("St. Charles Place", 140, 10));
        properties.put(12, new Utility("Electric Company", 150, 0));
        properties.put(13, new Property("States Avenue", 140, 10));
        properties.put(14, new Property("Virginia Avenue", 160, 12));
        properties.put(15, new Railroad("Pennsylvania Railroad", 200, 25));
        properties.put(16, new Property("St. James Place", 180, 14));
        properties.put(18, new Property("Tennessee Avenue", 180, 14));
        properties.put(19, new Property("New York Ave", 200, 16));
        properties.put(21, new Property("Kentucky Avenue", 220, 18));
        properties.put(23, new Property("Indiana Avenue", 220, 18));
        properties.put(24, new Property("Illinois Avenue", 240, 20));
        properties.put(25, new Railroad("B&O Railroad", 200, 25));
        properties.put(26, new Property("Atlantic Avenue", 260, 22));
        properties.put(27, new Property("Ventnor Avenue", 260, 22));
        properties.put(28, new Utility("Water Works", 150, 0));
        properties.put(29, new Property("Marvin Gardens", 280, 24));
        properties.put(31, new Property("Pacific Avenue", 300, 26));
        properties.put(32, new Property("North Carolina Avenue", 300, 26));
        properties.put(34, new Property("Pennsylvania Avenue", 320, 28));
        properties.put(35, new Railroad("Short Line Railroad", 200, 25));
        properties.put(37, new Property("Park Place", 350, 35));
        properties.put(39, new Property("Boardwalk", 400, 50));
    }
    private void showMortgageOptions() {
        Player currentPlayer = players.get(currentIndex);
        List<Property> mortgagableProperties = new ArrayList<>();
        for (Property property : currentPlayer.getOwnedProperties()) {
            if (!property.isMortgaged()) {
                mortgagableProperties.add(property);
            }
        }

        if (mortgagableProperties.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have no properties eligible for mortgaging.");
            return;
        }

        Property[] propertyArray = mortgagableProperties.toArray(new Property[0]);
        JComboBox<Property> propertyComboBox = new JComboBox<>(propertyArray);
        // Use a custom renderer to display property names in the JComboBox
        propertyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Property) {
                    Property property = (Property) value;
                    setText(property.getName()); // Display the property name
                }
                return this;
            }
        });

        int option = JOptionPane.showOptionDialog(
                null,
                propertyComboBox,
                "Select a Property to Mortgage",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Mortgage", "Cancel"},
                "Mortgage"
        );

        if (option == JOptionPane.OK_OPTION) {
            Property selectedProperty = (Property) propertyComboBox.getSelectedItem();
            boolean success = currentPlayer.mortgageProperty(selectedProperty);
            if (success) {
                updateProfiles();
                board.repaint();
            }
        }
    }

    private void showUnmortgageOptions() {
        Player currentPlayer = players.get(currentIndex);
        List<Property> unmortgagableProperties = new ArrayList<>();
        for (Property property : currentPlayer.getOwnedProperties()) {
            if (property.isMortgaged()) {
                unmortgagableProperties.add(property);
            }
        }

        if (unmortgagableProperties.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have no properties eligible for unmortgaging.");
            return;
        }

        Property[] propertyArray = unmortgagableProperties.toArray(new Property[0]);
        JComboBox<Property> propertyComboBox = new JComboBox<>(propertyArray);
        // Use a custom renderer to display property names in the JComboBox
        propertyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Property) {
                    Property property = (Property) value;
                    setText(property.getName()); // Display the property name
                }
                return this;
            }
        });

        int option = JOptionPane.showOptionDialog(
                null,
                propertyComboBox,
                "Select a Property to Unmortgage",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Unmortgage", "Cancel"},
                "Unmortgage"
        );

        if (option == JOptionPane.OK_OPTION) {
            Property selectedProperty = (Property) propertyComboBox.getSelectedItem();
            boolean success = currentPlayer.unmortgageProperty(selectedProperty);
            if (success) {
                updateProfiles();
                board.repaint();
            }
        }
    }
}

