import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;

    public WeatherAppGui() {
        // Setup our GUI and add a title
        super("Weather App");

        // Configure GUI to end the program's process once it has been closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set the size of our GUI (in pixels)
        setSize(900, 900); // Set to 900x900

        // Load our GUI at the center of the screen
        setLocationRelativeTo(null);

        // Use GridBagLayout for better alignment
        setLayout(new GridBagLayout());

        // Prevent any resize of our GUI
        setResizable(false);

        getContentPane().setBackground(new Color(173, 216, 230));

        addGuiComponents();
    }

    private void addGuiComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Search field
        JTextField searchTextField = new JTextField();
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.8; // Allow it to grow
        add(searchTextField, gbc);

        // Search button
        JButton searchButton = new JButton(loadImage("src/assets/weatherapp_images/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2; // Smaller width for button
        add(searchButton, gbc);

        // Weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/weatherapp_images/cloudy.png"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(weatherConditionImage, gbc);

        // Temperature text
        JLabel temperatureText = new JLabel("10 °C");
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        add(temperatureText, gbc);

        // Weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        add(weatherConditionDesc, gbc);

        // Create a panel for humidity and windspeed info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 2)); // Two columns layout

        // Humidity info
        JLabel humidityImage = new JLabel(loadImage("src/assets/weatherapp_images/humidity.png"));
        JLabel humidityText = new JLabel("<html><b>Humidity:</b> 100%</html>");

        JPanel humidityPanel = new JPanel();
        humidityPanel.add(humidityImage);
        humidityPanel.add(humidityText);

        // Windspeed info
        JLabel windspeedImage = new JLabel(loadImage("src/assets/weatherapp_images/windspeed.png"));
        JLabel windspeedText = new JLabel("<html><b>Windspeed:</b> 15 km/h</html>");

        JPanel windspeedPanel = new JPanel();
        windspeedPanel.add(windspeedImage);
        windspeedPanel.add(windspeedText);

        infoPanel.add(humidityPanel);
        infoPanel.add(windspeedPanel);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(infoPanel, gbc);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText().trim();
                if (userInput.isEmpty()) return;

                weatherData = WeatherApp.getWeatherData(userInput);
                updateGui(weatherConditionImage, temperatureText, weatherConditionDesc, humidityText, windspeedText);
            }
        });
    }

    private void updateGui(JLabel weatherConditionImage, JLabel temperatureText,
                           JLabel weatherConditionDesc, JLabel humidityText,
                           JLabel windspeedText) {
        String weatherCondition = (String) weatherData.get("weather_condition");
        switch (weatherCondition) {
            case "Clear":
                weatherConditionImage.setIcon(loadImage("src/assets/weatherapp_images/clear.png"));
                break;
            case "Cloudy":
                weatherConditionImage.setIcon(loadImage("src/assets/weatherapp_images/cloudy.png"));
                break;
            case "Rain":
                weatherConditionImage.setIcon(loadImage("src/assets/weatherapp_images/rain.png"));
                break;
            case "Snow":
                weatherConditionImage.setIcon(loadImage("src/assets/weatherapp_images/snow.png"));
                break;
        }
        double temperature = (double) weatherData.get("temperature");
        temperatureText.setText(temperature + " °C");
        weatherConditionDesc.setText(weatherCondition);
        long humidity = (long) weatherData.get("humidity");
        humidityText.setText("<html><b>Humidity:</b> " + humidity + "%</html>");
        double windspeed = (double) weatherData.get("windspeed");
        windspeedText.setText("<html><b>Windspeed:</b> " + windspeed + " km/h</html>");
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }
}
