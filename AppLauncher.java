//import javax.swing.*;
//
//public class AppLauncher {
//    public static void main(String args[])
//    {
//        SwingUtilities.invokeLater(new Runnable(){
//            @Override
//            public void run()
//            {
//                //display the weather app gui
//                new WeatherAppGui().setVisible(true);
//                //SwingUtilities.invokeLater(() -> new Wordle());
//
//
//
//            }
//        });
//    }
//}


import javax.swing.*;
import java.awt.*;

public class AppLauncher {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;

        // Create main frame
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

        // Pack and display the frame
        frame.pack();
        frame.setVisible(true);
    }
}
