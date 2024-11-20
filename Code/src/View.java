import javax.swing.*;
import java.awt.*;

public class View implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("TwentyFortyEight");

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);
        final JLabel status = new JLabel("");
        status_panel.add(status);
        final JLabel score = new JLabel("0");
        status_panel.add(score);

        // Game board
        final Controller board = new Controller(
                status, score
        );
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undoButtonCallback());
        final JButton save = new JButton("Save");
        save.addActionListener(e -> board.save());
        final JButton load = new JButton("Load");
        load.addActionListener(e -> board.load());
        control_panel.add(reset);
        control_panel.add(undo);
        control_panel.add(save);
        control_panel.add(load);

        // Instructions
        JOptionPane.showMessageDialog(null, """
                Welcome to 2048!
                Instructions:
                (1) Use your arrow keys to move the tiles up, down, left, or right
                (2) Two adjacent tiles with the same value will combine to one double-valued\s
                tile if you move the tiles in their direction
                (3) If the board fills up AND there are no combinable tiles, you will lose\s
                and the game stops.
                (4) If you get to 2048, you win and the game stops.
                Good luck and have fun!""");

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
