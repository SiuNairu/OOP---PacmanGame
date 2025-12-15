package UI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class GameWinScreen extends JFrame implements ActionListener {

    private JButton replayButton, exitButton;

    public GameWinScreen(int score) {
        setTitle("You Win!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel title = new JLabel("YOU WIN!");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.GREEN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Final Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        replayButton = new JButton("Play Again");
        replayButton.setFont(new Font("Arial", Font.BOLD, 20));
        replayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        replayButton.addActionListener(this);

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(this);

        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(replayButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(exitButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == replayButton) {

            // Chơi lại từ level 0
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

