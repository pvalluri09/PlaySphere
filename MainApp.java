import java.awt.*;
import javax.swing.*;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("Main Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showApplicationSelector(); // Show the application selector on startup
    }

    public void showApplicationSelector() {
        ApplicationSelectorPanel selectorPanel = new ApplicationSelectorPanel(this);
        setContentPane(selectorPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}

class ApplicationSelectorPanel extends JPanel {

    private MainApp mainApp;

    public ApplicationSelectorPanel(MainApp app) {
        this.mainApp = app;

        setLayout(new BorderLayout());
        setBackground(new Color(255, 228, 225)); // Light pink background

        // Header Label
        JLabel titleLabel = new JLabel("Select an Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(128, 0, 128)); // Purple color
        add(titleLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Create buttons for each application with different colors
        JButton wordleButton = createButton("Game 1: Wordle", "src/assets/flappy_images/wordl.jpg", new Color(255, 99, 71)); // Tomato
        JButton flappyBirdButton = createButton("Game 2: Flappy Bird", "src/assets/flappy_images/flap.jpeg", new Color(135, 206, 250)); // Sky Blue
        JButton breakoutBallButton = createButton("Game 3: Breakout Ball", "src/assets/flappy_images/breakouts.jpeg", new Color(50, 205, 50)); // Lime Green
        JButton weatherButton = createButton("Weather App", "src/assets/flappy_images/cloudy.jpeg", new Color(255, 215, 0)); // Gold

        // Add action listeners to buttons
        wordleButton.addActionListener(e -> openWordleGame());
        flappyBirdButton.addActionListener(e -> openFlappyBirdGame());
        breakoutBallButton.addActionListener(e -> openBreakoutBallButton());
        weatherButton.addActionListener(e -> openWeatherApp());

        // Add buttons to the panel
        buttonPanel.add(wordleButton);
        buttonPanel.add(flappyBirdButton);
        buttonPanel.add(breakoutBallButton);
        buttonPanel.add(weatherButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String imagePath, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(bgColor); // Set the button background color
        button.setForeground(Color.BLACK); // Text color
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // White border
        button.setPreferredSize(new Dimension(200, 80)); // Adjusted height for better icon-text alignment
        button.setFocusPainted(false); // Remove focus border

        // Load the image icon
        ImageIcon icon = new ImageIcon(imagePath);
        // Resize the icon to fit nicely with the button text
        Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage)); // Set the resized icon on the button

        // Set text and icon positioning
        button.setHorizontalTextPosition(SwingConstants.LEFT); // Position text to the left of the icon
        button.setVerticalTextPosition(SwingConstants.CENTER); // Center the text vertically with the icon

        return button;
    }

    private void openWordleGame() {
        JOptionPane.showMessageDialog(this, "Redirecting to Wordle Game!");
        new Timer(100, e -> {
            ((Timer) e.getSource()).stop();
            new Wordle(); // Transition to the Wordle game
        }).start();
    }

    private void openBreakoutBallButton() {
        JOptionPane.showMessageDialog(this, "Redirecting to Breakout Ball Game!");
        SwingUtilities.invokeLater(() -> new Gameplay().setVisible(true));
    }

    private void openFlappyBirdGame() {
        JOptionPane.showMessageDialog(this, "Redirecting to Flappy Bird Game!");
        FlappyBirdGame();
    }

    private void openWeatherApp() {
        JOptionPane.showMessageDialog(this, "Redirecting to Weather App!");
        SwingUtilities.invokeLater(() -> new WeatherAppGui().setVisible(true));
    }

    private void FlappyBirdGame() {
        int boardWidth = 360;
        int boardHeight = 640;
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a CardLayout and JPanel to switch between start/game over and game screens
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create the FlappyBird game and pass the panel and card layout
        FlappyBird flappyBird = new FlappyBird(mainPanel, cardLayout);

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
