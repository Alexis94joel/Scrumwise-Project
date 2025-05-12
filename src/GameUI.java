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
    private JButton mortgageButton;
    private JButton unmortgageButton;
    private JButton buildHouseButton;
    private JButton sellHouseButton;

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

        buildHouseButton = new JButton("Build House");
        buildHouseButton.setFont(new Font("Arial", Font.BOLD, 16));
        buildHouseButton.addActionListener(e -> showBuildHouseOptions());

        sellHouseButton = new JButton("Sell House");
        sellHouseButton.setFont(new Font("Arial", Font.BOLD, 16));
        sellHouseButton.addActionListener(e -> showSellHouseOptions());

        sidePanel.add(diceLabel);
        sidePanel.add(rollButton);
        sidePanel.add(mortgageButton);
        sidePanel.add(unmortgageButton);
        sidePanel.add(buildHouseButton);
        sidePanel.add(sellHouseButton);
        frame.add(sidePanel, BorderLayout.EAST);

        updateProfiles();
        frame.setVisible(true);
    }

    public static GameUI getInstance() {
        if (instance == null) {
            instance = new GameUI(players);  // Create the instance if it doesn't exist
        }
        return instance;
    }

    public static void initialize(List<Player> players) {
        if (instance == null) {
            instance = new GameUI(players);  // Initialize GameUI with the list of players
        }
    }

    public static boolean playerOwnsColorGroup(Player player, Property property) {
        List<Property> colorGroup = getColorGroup(property);
        if (colorGroup == null) return false;

        for (Property p : colorGroup) {
            if (p.getOwner() != player) return false;
        }
        return true;
    }

    private static List<Property> getColorGroup(Property property) {
        List<Property> group = new ArrayList<>();

        String name = property.getName();
        if (name.contains("Mediterranean") || name.contains("Baltic")) {
            group.add(instance.properties.get(1));
            group.add(instance.properties.get(3));
        } else if (name.contains("Oriental") || name.contains("Vermont") || name.contains("Connecticut")) {
            group.add(instance.properties.get(6));
            group.add(instance.properties.get(8));
            group.add(instance.properties.get(9));
        } else if (name.contains("St. Charles") || name.contains("States") || name.contains("Virginia")) {
            group.add(instance.properties.get(11));
            group.add(instance.properties.get(13));
            group.add(instance.properties.get(14));
        } else if (name.contains("St. James") || name.contains("Tennessee") || name.contains("New York")) {
            group.add(instance.properties.get(16));
            group.add(instance.properties.get(18));
            group.add(instance.properties.get(19));
        } else if (name.contains("Kentucky") || name.contains("Indiana") || name.contains("Illinois")) {
            group.add(instance.properties.get(21));
            group.add(instance.properties.get(23));
            group.add(instance.properties.get(24));
        } else if (name.contains("Atlantic") || name.contains("Ventnor") || name.contains("Marvin")) {
            group.add(instance.properties.get(26));
            group.add(instance.properties.get(27));
            group.add(instance.properties.get(29));
        } else if (name.contains("Pacific") || name.contains("North Carolina") || name.contains("Pennsylvania")) {
            group.add(instance.properties.get(31));
            group.add(instance.properties.get(32));
            group.add(instance.properties.get(34));
        } else if (name.contains("Park") || name.contains("Boardwalk")) {
            group.add(instance.properties.get(37));
            group.add(instance.properties.get(39));
        } else {
            return null;
        }

        return group;
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
                instance.sidePanel.add(instance.mortgageButton);
                instance.sidePanel.add(instance.unmortgageButton);
                instance.sidePanel.add(instance.buildHouseButton);
                instance.sidePanel.add(instance.sellHouseButton);

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
        properties.put(1, new Property("Mediterranean Avenue", 60, 2, new int[]{10, 30, 90, 160, 250}, 50));
        properties.put(3, new Property("Baltic Avenue", 60, 4, new int[]{20, 60, 180, 320, 450}, 50));
        properties.put(5, new Railroad("Reading Railroad", 200, 25));
        properties.put(6, new Property("Oriental Avenue", 100, 6, new int[]{30, 90, 270, 400, 550}, 50));
        properties.put(8, new Property("Vermont Avenue", 100, 6, new int[]{30, 90, 270, 400, 550}, 50));
        properties.put(9, new Property("Connecticut Avenue", 120, 8, new int[]{40, 100, 300, 450, 600}, 50));
        properties.put(11, new Property("St. Charles Place", 140, 10, new int[]{50, 150, 450, 625, 750}, 100));
        properties.put(12, new Utility("Electric Company", 150, 0));
        properties.put(13, new Property("States Avenue", 140, 10, new int[]{50, 150, 450, 625, 750}, 100));
        properties.put(14, new Property("Virginia Avenue", 160, 12, new int[]{60, 180, 500, 700, 900}, 100));
        properties.put(15, new Railroad("Pennsylvania Railroad", 200, 25));
        properties.put(16, new Property("St. James Place", 180, 14, new int[]{70, 200, 550, 750, 950}, 100));
        properties.put(18, new Property("Tennessee Avenue", 180, 14, new int[]{70, 200, 550, 750, 950}, 100));
        properties.put(19, new Property("New York Ave", 200, 16, new int[]{80, 220, 600, 800, 1000}, 100));
        properties.put(21, new Property("Kentucky Avenue", 220, 18, new int[]{90, 250, 700, 875, 1050}, 150));
        properties.put(23, new Property("Indiana Avenue", 220, 18, new int[]{90, 250, 700, 875, 1050}, 150));
        properties.put(24, new Property("Illinois Avenue", 240, 20, new int[]{100, 300, 750, 925, 1100}, 150));
        properties.put(25, new Railroad("B&O Railroad", 200, 25));
        properties.put(26, new Property("Atlantic Avenue", 260, 22, new int[]{110, 330, 800, 975, 1150}, 150));
        properties.put(27, new Property("Ventnor Avenue", 260, 22, new int[]{110, 330, 800, 975, 1150}, 150));
        properties.put(28, new Utility("Water Works", 150, 0));
        properties.put(29, new Property("Marvin Gardens", 280, 24, new int[]{120, 360, 850, 1025, 1200}, 150));
        properties.put(31, new Property("Pacific Avenue", 300, 26, new int[]{130, 390, 900, 1100, 1275}, 200));
        properties.put(32, new Property("North Carolina Avenue", 300, 26, new int[]{130, 390, 900, 1100, 1275}, 200));
        properties.put(34, new Property("Pennsylvania Avenue", 320, 28, new int[]{150, 450, 1000, 1200, 1400}, 200));
        properties.put(35, new Railroad("Short Line Railroad", 200, 25));
        properties.put(37, new Property("Park Place", 350, 35, new int[]{175, 500, 1100, 1300, 1500}, 200));
        properties.put(39, new Property("Boardwalk", 400, 50, new int[]{200, 600, 1400, 1700, 2000}, 200));
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
        propertyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Property) {
                    Property property = (Property) value;
                    setText(property.getName());
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
        propertyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Property) {
                    Property property = (Property) value;
                    setText(property.getName());
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

    private void showBuildHouseOptions() {
        Player currentPlayer = players.get(currentIndex);
        List<Property> buildableProperties = new ArrayList<>();
        for (Property property : currentPlayer.getOwnedProperties()) {
            if (playerOwnsColorGroup(currentPlayer, property) && property.getHouseCount() < 5) {
                buildableProperties.add(property);
            }
        }

        if (buildableProperties.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have no properties eligible for building houses.");
            return;
        }

        Property[] propertyArray = buildableProperties.toArray(new Property[0]);
        JComboBox<Property> propertyComboBox = new JComboBox<>(propertyArray);
        propertyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Property) {
                    Property property = (Property) value;
                    setText(property.getName());
                }
                return this;
            }
        });

        int option = JOptionPane.showOptionDialog(
                null,
                propertyComboBox,
                "Select a Property to Build a House",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Build House", "Cancel"},
                "Build House"
        );

        if (option == JOptionPane.OK_OPTION) {
            Property selectedProperty = (Property) propertyComboBox.getSelectedItem();
            int houseCost = selectedProperty.getHousePrice();
            if (currentPlayer.getMoney() >= houseCost) {
                currentPlayer.deductMoney(houseCost);
                // Corrected line: Pass the HashMap (properties) and DisplayToken (board)
                selectedProperty.buildHouse(currentPlayer, properties, board);
                updateProfiles();
                board.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "You do not have enough money to build a house on this property.");
            }
        }
    }

    private void showSellHouseOptions() {
        Player currentPlayer = players.get(currentIndex);
        List<Property> sellableProperties = new ArrayList<>();
        for (Property property : currentPlayer.getOwnedProperties()) {
            if (property.getHouseCount() > 0) {
                sellableProperties.add(property);
            }
        }

        if (sellableProperties.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have no properties with houses to sell.");
            return;
        }

        Property[] propertyArray = sellableProperties.toArray(new Property[0]);
        JComboBox<Property> propertyComboBox = new JComboBox<>(propertyArray);
        propertyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Property) {
                    Property property = (Property) value;
                    setText(property.getName());
                }
                return this;
            }
        });

        int option = JOptionPane.showOptionDialog(
                null,
                propertyComboBox,
                "Select a Property to Sell a House",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Sell House", "Cancel"},
                "Sell House"
        );

        if (option == JOptionPane.OK_OPTION) {
            Property selectedProperty = (Property) propertyComboBox.getSelectedItem();
            boolean success = currentPlayer.sellHouse(selectedProperty);
            if (success) {
                int sellValue = selectedProperty.getHousePrice() / 2;
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + " sold a house on " + selectedProperty.getName() + " for $" + sellValue);
                updateProfiles();
                board.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to sell a house on the selected property.");
            }
        }
    }
}
