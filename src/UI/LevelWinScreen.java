package UI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelWinScreen extends JFrame implements ActionListener {

    private final PacmanGamePanel gamePanel;
    private final int levelNumber;
    private JButton nextLevelButton, exitButton;

    public LevelWinScreen(int score, int levelNumber, PacmanGamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.levelNumber = levelNumber;

        setTitle("Level Clear");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel title = new JLabel("LEVEL " + levelNumber + " CLEARED!");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.GREEN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel question = new JLabel("Go to next level?");
        question.setFont(new Font("Arial", Font.PLAIN, 20));
        question.setForeground(Color.WHITE);
        question.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextLevelButton = new JButton("Next Level");
        nextLevelButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextLevelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextLevelButton.addActionListener(this);

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(this);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(question);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(nextLevelButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(exitButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextLevelButton) {
            // qua level mới: dùng lại cùng 1 cửa sổ game
            gamePanel.goToNextLevel();
            dispose();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
