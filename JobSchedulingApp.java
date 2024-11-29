import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JobSchedulingApp extends JFrame {
    private ArrayList<Job> jobs = new ArrayList<>();
    private JTextField jobIdField;
    private JTextField weightField;
    private JTextField processingTimeField;
    private JTextField dueDatesField;
    private JLabel resultLabel;

    public JobSchedulingApp() {
        setTitle("Job Scheduling with Fuzzy Processing Times");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Input fields for jobs
        jobIdField = new JTextField();
        weightField = new JTextField();
        processingTimeField = new JTextField();
        dueDatesField = new JTextField();

        // Buttons
        JButton addButton = new JButton("Add Job");
        JButton calculateButton = new JButton("Calculate Tardiness");

        // Result label
        resultLabel = new JLabel("Result: ");

        // Add components to the frame
        add(new JLabel("Job ID:"));
        add(jobIdField);
        add(new JLabel("Weight:"));
        add(weightField);
        add(new JLabel("Fuzzy Processing Time:"));
        add(processingTimeField);
        add(new JLabel("Due Dates (comma separated):"));
        add(dueDatesField);
        add(addButton);
        add(calculateButton);
        add(resultLabel);

        // Button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addJob();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTardiness();
            }
        });
    }

    private void addJob() {
        try {
            int jobId = Integer.parseInt(jobIdField.getText());
            int weight = Integer.parseInt(weightField.getText());
            int fuzzyProcessingTime = Integer.parseInt(processingTimeField.getText());

            jobs.add(new Job(jobId, weight, fuzzyProcessingTime));
            clearFields();
            JOptionPane.showMessageDialog(this, "Job added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        }
    }

    private void calculateTardiness() {
        String[] dueDatesStr = dueDatesField.getText().split(",");
        int[] dueDates = new int[dueDatesStr.length];
        for (int i = 0; i < dueDatesStr.length; i++) {
            dueDates[i] = Integer.parseInt(dueDatesStr[i].trim());
        }

        // Here you would implement the scheduling algorithm using the jobs and due dates
        // For demonstration, we'll just display the number of jobs added.
        resultLabel.setText("Total Jobs: " + jobs.size());
    }

    private void clearFields() {
        jobIdField.setText("");
        weightField.setText("");
        processingTimeField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JobSchedulingApp app = new JobSchedulingApp();
            app.setVisible(true);
        });
    }
}

class Job {
    int jobId;
    int weight;
    int fuzzyProcessingTime;

    public Job(int jobId, int weight, int fuzzyProcessingTime) {
        this.jobId = jobId;
        this.weight = weight;
        this.fuzzyProcessingTime = fuzzyProcessingTime;
    }
}
