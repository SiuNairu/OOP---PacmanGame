package UI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Menu extends JFrame implements ActionListener {
    private JButton playButton, exitButton;

    public Menu() {
        setTitle("Pacman Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        // main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        // Ttle
        JLabel title = new JLabel("PACMAN GAME");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.YELLOW);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // play button
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.addActionListener(this);

        // Exit button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(this);

        // Add panel
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(playButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(exitButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            // Open game
            JFrame frame = new JFrame("Pacman");
            PacmanGamePanel pacmanGame = new PacmanGamePanel();
            frame.add(pacmanGame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            pacmanGame.requestFocus();

            this.dispose(); 
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}