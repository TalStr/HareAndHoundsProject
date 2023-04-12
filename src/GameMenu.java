import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JFrame {
    // 0 = 2 players
    int difficulty;
    String playerChoice = "Wolves";
    JButton selectedMode;
    Switch saveGame;
    Switch playerPlaying;
    Switch bot1;
    Switch bot2;
    Switch bot3;
    JPanel settings;
    public GameMenu() {

        setTitle("Game Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        // Create a title label
        JLabel titleLabel = new JLabel("Game Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Create a JPanel with a GridBagLayout for better positioning
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 10px padding on all sides

        JPanel topCenter = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton twoPlayerButton = new JButton("2 Players");
        twoPlayerButton.setName("@P ");
        twoPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        twoPlayerButton.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);
        twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMode.setBorder(null);
                twoPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                selectedMode = twoPlayerButton;
                settings.removeAll();
                showTwoPlayerSettings();

            }
        });
        topCenter.add(twoPlayerButton, gbc);
        selectedMode = twoPlayerButton;


        JButton onePlayerButton = new JButton("Player vs Bot");
        onePlayerButton.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);
        onePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMode.setBorder(null);
                onePlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                selectedMode = onePlayerButton;
                settings.removeAll();
                showOnePlayerSettings();
            }
        });
        topCenter.add(onePlayerButton, gbc);

        JButton simulationButton = new JButton("Bot Simulation");
        simulationButton.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);
        simulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMode.setBorder(null);
                simulationButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                selectedMode = simulationButton;
                settings.removeAll();
                showSimulationSettings();
            }
        });
        topCenter.add(simulationButton, gbc);

        centerPanel.add(topCenter, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        settings = new JPanel(new GridBagLayout());
        centerPanel.add(settings, BorderLayout.CENTER);

        twoPlayerButton.doClick();

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                TestBoard board;
//                if(difficulty == 0)
//                    board = new TestBoard(StartMenu.this);
//                else
//                    board = new TestBoard(StartMenu.this,difficulty, (playerChoice.charAt(0) == 'W')? GameType.WOLF: GameType.RABBIT);
//                board.setVisible(true);
//                setVisible(false);
            }
        });
        add(startButton, BorderLayout.SOUTH);

        // Make the JFrame visible
        setVisible(true);
    }
    public void showTwoPlayerSettings()
    {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label1 = new JLabel("Save Game");
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(label1, gbc);

        Switch switch1 = new Switch("Yes", "No");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        settings.add(switch1, gbc);
        settings.revalidate();
        settings.repaint();


    }
    public void showOnePlayerSettings()
    {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label1 = new JLabel("Save Game");
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(label1, gbc);

        Switch switch1 = new Switch("Yes", "No");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        settings.add(switch1, gbc);

        JLabel label2 = new JLabel("Playing");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(label2, gbc);

        Switch switch2 = new Switch("Wolves", "Rabbit");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        settings.add(switch2, gbc);

        JLabel label3 = new JLabel("Difficulty");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(label3, gbc);

        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        settings.add(difficultyComboBox, gbc);

        settings.revalidate();
        settings.repaint();

    }
    public void showSimulationSettings()
    {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label1 = new JLabel("Save Game");
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(label1, gbc);

        Switch switch1 = new Switch("Yes", "No");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        settings.add(switch1, gbc);

        JLabel vis = new JLabel("Visible");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(vis, gbc);

        Switch visSwitch = new Switch("Yes", "No");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        settings.add(visSwitch, gbc);

        JLabel wolfDiff = new JLabel("Wolf Difficulty");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(wolfDiff, gbc);

        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox<String> wolfDiffChoice = new JComboBox<>(difficulties);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        settings.add(wolfDiffChoice, gbc);

        JLabel rabbitDiff = new JLabel("Rabbit Difficulty");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        settings.add(rabbitDiff, gbc);

        JComboBox<String> rabbitDiffChoice = new JComboBox<>(difficulties);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        settings.add(rabbitDiffChoice, gbc);

        settings.revalidate();
        settings.repaint();

    }


}

