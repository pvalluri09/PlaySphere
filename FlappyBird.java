import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    Image startPanelImg; // Background for the start panel
    Image gameOverPanelImg; // Background for the game over panel
    Image title;

    // Audio Clip for music
    Clip backgroundMusic;

    // Bird class
    int birdX = boardWidth / 8;
    int birdY = boardWidth / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;

    // Game state
    JPanel mainPanel;
    CardLayout cardLayout;
    JPanel gamePanel;
    JPanel startPanel;
    JPanel gameOverPanel;

    JButton startButton;
    JButton playAgainButton;
    JButton exitButton;

    public FlappyBird(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("/assets/flappy_images/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("/assets/flappy_images/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/assets/flappy_images/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("/assets/flappy_images/bottompipe.png")).getImage();

        // Load start and game over panel images
        startPanelImg = new ImageIcon(getClass().getResource("/assets/flappy_images/flappybirdbg.png")).getImage();
        gameOverPanelImg = new ImageIcon(getClass().getResource("/assets/flappy_images/flappybirdbg.png")).getImage();
        title = new ImageIcon(getClass().getResource("/assets/flappy_images/flappy.png")).getImage();
        // Bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // Place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        // Game timer
        gameLoop = new Timer(1000 / 60, this);

        // Start panel with button
        startPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(startPanelImg, 0, 0, getWidth(), getHeight(), this);
                g.drawImage(title, (getWidth() - title.getWidth(null)) / 2, 30, this);
            }
        };
        startButton = createButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        startPanel.setLayout(new GridBagLayout());
        startPanel.add(startButton);

        // Game over panel
        gameOverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(gameOverPanelImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        playAgainButton = createButton("Play Again");
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        exitButton = createButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gameOverPanel.setLayout(new GridBagLayout());
        gameOverPanel.add(playAgainButton, gbc);
        gbc.gridy = 1;
        gameOverPanel.add(exitButton, gbc);

        // Add start and game over panels to the main panel
        mainPanel.add(startPanel, "StartPanel");
        mainPanel.add(this, "GamePanel");
        mainPanel.add(gameOverPanel, "GameOverPanel");

        cardLayout.show(mainPanel, "StartPanel"); // Show start panel first
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Add hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 144, 255)); // Dodger blue
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237)); // Cornflower blue
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180)); // Steel blue
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(30, 144, 255)); // Dodger blue
            }
        });

        return button;
    }

    void startGame() {
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        gameOver = false;
        score = 0;
        gameLoop.start();
        placePipeTimer.start();
        playMusic("/assets/sounds/background.wav");
        cardLayout.show(mainPanel, "GamePanel"); // Show game panel
        requestFocus(); // Request focus so that it can receive key events
    }

    void restartGame() {
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        gameOver = false;
        score = 0;
        gameLoop.start();
        placePipeTimer.start();
        playMusic("/assets/sounds/background.wav");
        cardLayout.show(mainPanel, "GamePanel"); // Show game panel
        requestFocus(); // Request focus so that it can receive key events
    }

    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
                stopMusic();
                gameOverScreen();
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
            stopMusic();
            gameOverScreen();
        }
    }

    void gameOverScreen() {
        gameLoop.stop();
        placePipeTimer.stop();
        cardLayout.show(mainPanel, "GameOverPanel");
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public void playMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
}
