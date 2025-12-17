package UI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class GameOverScreen extends JFrame implements ActionListener {
    private JButton retryButton, exitButton;

    public GameOverScreen(int score) {
        setTitle("Game Over");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        // Tiêu đề Game Over
        JLabel title = new JLabel("GAME OVER");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.RED);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hiển thị điểm
        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nút Retry
        retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Arial", Font.BOLD, 20));
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(this);

        // Nút Exit
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(this);

        // Thêm vào panel
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(retryButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(exitButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == retryButton) {
            // Mở lại game
            JFrame frame = new JFrame("Pacman");
            PacmanGamePanel pacmanGame = new PacmanGamePanel();
            frame.add(pacmanGame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            pacmanGame.requestFocus();

            this.dispose(); // đóng Game Over screen
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
