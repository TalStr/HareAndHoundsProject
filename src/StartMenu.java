import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame {
    // 0 = 2 players
    int difficulty;
    String playerChoice = "Wolves";
    public StartMenu() {

        setTitle("Game Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        // Create a title label
        JLabel titleLabel = new JLabel("Game Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Create a JPanel with a GridBagLayout for better positioning
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Create the first option button for 2 players
        JButton twoPlayerButton = new JButton("2 Players");
        twoPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        // Create a JComboBox to choose between wolves and rabbit
        String[] choices = {"Wolves", "Rabbit"};
        JComboBox<String> playerChoiceComboBox = new JComboBox<>(choices);
        playerChoiceComboBox.setVisible(false);

        // Create a JComboBox for the 1 player option with difficulty selection
        String[] difficulties = {"1 Player - Difficulty 1", "1 Player - Difficulty 2", "1 Player - Difficulty 3"};
        JComboBox<String> onePlayerComboBox = new JComboBox<>(difficulties);

        // Add action listener to handle selection change
        onePlayerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) onePlayerComboBox.getSelectedItem();
                difficulty = Character.getNumericValue(selectedItem.charAt(22));
                playerChoiceComboBox.setVisible(true);
                onePlayerComboBox.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                twoPlayerButton.setBorder(null);
            }
        });

        // Add action listener to handle button clicks
        twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulty = 0;
                playerChoiceComboBox.setVisible(false);
                twoPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                onePlayerComboBox.setBorder(null);
            }
        });
        // Create a JComboBox to choose between wolves and rabbit

        // Add action listener to handle selection change
        playerChoiceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerChoice = (String) playerChoiceComboBox.getSelectedItem();
            }
        });

        // Create a start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestBoard board;
                if(difficulty == 0)
                    board = new TestBoard(StartMenu.this);
                else
                    board = new TestBoard(StartMenu.this,difficulty, (playerChoice.charAt(0) == 'W')? GameType.WOLF: GameType.RABBIT);
                board.setVisible(true);
                setVisible(false);
            }
        });

        // Position and add the components to the JPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel.add(twoPlayerButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel.add(onePlayerComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(playerChoiceComboBox, gbc);

        // Add the title label, JPanel, and start button to the JFrame
        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);

        // Make the JFrame visible
        setVisible(true);
    }

}

