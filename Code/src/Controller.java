import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class Controller extends JPanel {
    private final TwentyFortyEight tfe; // model for the game
    private final JLabel status; // status
    private final JLabel score; // score

    private final JLabel[][] board;

    private static final int BOARD_WIDTH = 300;
    private static final int BOARD_HEIGHT = 300;

    public Controller(JLabel statusInit, JLabel scoreInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        this.board = new JLabel[4][4];
        this.setLayout(new GridLayout(4, 4));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new JLabel("", SwingConstants.CENTER);
                this.add(board[i][j]);
            }
        }
        tfe = new TwentyFortyEight(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        score = scoreInit;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (tfe.getGameOver()) {
                    if (tfe.getPlayerWon()) {
                        displayStatusMessage("You won!");
                    } else {
                        displayStatusMessage("You lost.");
                    }
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    tfe.playTurn(0);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    tfe.playTurn(1);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    tfe.playTurn(2);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tfe.playTurn(3);
                }
                displayStatusMessage("");
                updateScore();
                repaint();
                if (tfe.playerHasWon()) {
                    displayStatusMessage("You won!");
                    return;
                }
                if (tfe.checkGameOver()) {
                    displayStatusMessage("You lost.");
                }
            }
        });
    }

    private void updateScore() {
        score.setText(String.valueOf(tfe.getScore()));
    }

    private void displayStatusMessage(String msg) {
        status.setText(msg);
    }

    void reset() {
        tfe.reset();
        updateScore();
        repaint();
        requestFocusInWindow();
        displayStatusMessage("Game has been reset.");
    }

    void undoButtonCallback() {
        String message = tfe.decrementGameState();
        updateScore();
        repaint();
        requestFocusInWindow();
        displayStatusMessage(message);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JLabel label = board[i][j];
                int tileValue = tfe.getCell(i, j);
                if (tileValue == 0) {
                    label.setText("");
                } else {
                    label.setText(String.valueOf(tileValue));
                }
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(getColor(tileValue));
            }
        }
    }

    // iterate through board
    // use map to turn integer into JComponent
    // add that JComponent to the JPanel
    public static Color getColor(int tileValue) {
        return switch (tileValue) {
            case 2 -> Color.decode("#eee4da");
            case 4 -> Color.decode("#ede0c9");
            case 8 -> Color.decode("#f2b178");
            case 16 -> Color.decode("#f59662");
            case 32 -> Color.decode("#f67d5f");
            case 64 -> Color.decode("#f65e3b");
            case 128 -> Color.decode("#edcf73");
            case 256 -> Color.decode("#eecc62");
            case 512 -> Color.decode("#edc851");
            case 1024 -> Color.decode("#edc63f");
            case 2048 -> Color.decode("#edc22e");
            default -> Color.decode("#ccc1b4");
        };
    }

    public void save() {
        String message = tfe.writeGameToFile();
        requestFocusInWindow();
        displayStatusMessage(message);
    }

    public void load() {
        String[] files = new File("files/").list();
        if (files == null) {
            System.err.println("Files directory doesn't exist.");
            displayStatusMessage("Files directory doesn't exist.");
            requestFocusInWindow();
            return;
        } else if (files.length == 0) {
            System.err.println("No valid game files.");
            displayStatusMessage("No valid game files.");
            requestFocusInWindow();
            return;
        }
        ArrayList<String> validFileNames = new ArrayList<>();
        for (String fileName : files) {
            if (fileName.contains("game") && fileName.contains(".txt")) {
                validFileNames.add(fileName);
            }
        }
        if (validFileNames.isEmpty()) {
            System.err.println("No valid game files.");
            displayStatusMessage("No valid game files.");
            requestFocusInWindow();
            return;
        }
        Object[] choices = validFileNames.toArray();
        String selectedFileName = (String) JOptionPane.showInputDialog(
                null, "Choose a file to load.", "Input", JOptionPane.INFORMATION_MESSAGE,
                null,
                choices, choices[0]
        );
        if (selectedFileName == null) {
            displayStatusMessage("User selected cancel.");
            requestFocusInWindow();
            return;
        }
        String message = tfe.readGame("files/" + selectedFileName);
        repaint();
        updateScore();
        displayStatusMessage(message);
        requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
