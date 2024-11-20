import java.io.*;
import java.util.*;

public class TwentyFortyEight {

    private int[][] board;

    private int score;

    private int numTurns;

    private final Random rand;

    private TreeMap<Integer, GameState> previousTurns;

    private boolean gameOver = false;

    private boolean playerWon = false;

    TwentyFortyEight() {
        rand = new Random();
        reset();
    }

    // 0 for left, 1 for right, 2 for up, 3 for down.
    void playTurn(int move) {

        // Update the board based on arrow key AND update score
        updateBoard(move);

        // Generate a random tile (2 or 4) at open edge position
        generateRandomTile();

        numTurns++;

        previousTurns.put(numTurns, new GameState(score, cloneBoard(board)));

    }

    void updateBoard(int move) {
        int[] line = new int[4];
        if (move == 0) {
            for (int i = 0; i < 4; i++) {
                iterateThroughLine(board[i]);
            }
        } else if (move == 1) {
            for (int i = 0; i < 4; i++) {
                reverseArray(iterateThroughLine(reverseArray(board[i])));
            }
        } else if (move == 2) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    line[j] = board[j][i];
                }
                iterateThroughLine(line);
                for (int j = 0; j < 4; j++) {
                    board[j][i] = line[j];
                }
            }
        } else if (move == 3) {
            for (int i = 0; i < 4; i++) {
                for (int j = 3; j >= 0; j--) {
                    line[4 - j - 1] = board[j][i];
                }
                iterateThroughLine(line);
                for (int j = 3; j >= 0; j--) {
                    board[j][i] = line[4 - j - 1];
                }
            }
        }
    }

    public static int[] reverseArray(int[] arrayToReverse) {
        int indexForSwap = 3;
        for (int i = 0; i < 2; i++) {
            int valueAtIndexI = arrayToReverse[i];
            arrayToReverse[i] = arrayToReverse[indexForSwap];
            arrayToReverse[indexForSwap] = valueAtIndexI;
            indexForSwap--;
        }
        return arrayToReverse;
    }

    int[] iterateThroughLine(int[] line) {
        int previousIndex = 0;
        int previousValue = 0;
        for (int i = 0; i < 4; i++) {
            int currentValue = line[i];
            if (currentValue == 0) {
                continue;
            }
            if (currentValue == previousValue) {
                // Update line at current value to double
                line[i] = currentValue * 2;
                score += currentValue * 2;
                // Remove the previous value
                line[previousIndex] = 0;
                if (i < 3) {
                    previousIndex = i + 1;
                    previousValue = line[i + 1];
                }
                i++;
            } else {
                previousIndex = i;
                previousValue = currentValue;
            }
        }

        int availableIndex = 0;
        for (int i = 0; i < 4; ++i) {
            if (line[i] != 0) {
                if (i != availableIndex) {
                    line[availableIndex] = line[i];
                    line[i] = 0;
                }
                ++availableIndex;
            }
        }
        return line;
    }

    boolean areThereAnyEmptyTiles() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void generateRandomTile() {
        if (!areThereAnyEmptyTiles()) {
            return;
        }
        int numSpaces = 0;
        ArrayList<Integer> xCoords = new ArrayList<>();
        ArrayList<Integer> yCoords = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    xCoords.add(i);
                    yCoords.add(j);
                    numSpaces++;
                }
            }
        }
        int selectedTile = rand.nextInt(numSpaces);
        int tileValue = (rand.nextInt(10) > 0) ? 2 : 4;
        board[xCoords.get(selectedTile)][yCoords.get(selectedTile)] = tileValue;
    }

    boolean checkGameOver() {
        if (areThereAnyEmptyTiles()) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (anyTilesAreCombinable(board[i])) {
                return false;
            }
        }
        int[] line = new int[4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                line[j] = board[j][i];
            }
            if (anyTilesAreCombinable(line)) {
                return false;
            }
        }
        gameOver = true;
        return true;
    }

    private boolean anyTilesAreCombinable(int[] line) {
        int previousValue = 0;
        for (int i = 0; i < 4; i++) {
            int currentValue = line[i];
            if (currentValue == 0) {
                continue;
            }
            if (currentValue == previousValue) {
                return true;
            } else {
                previousValue = currentValue;
            }
        }
        return false;
    }

    boolean playerHasWon() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 2048) {
                    gameOver = true;
                    playerWon = true;
                    return true;
                }
            }
        }
        return false;
    }

    int getScore() {
        return score;
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    void reset() {
        numTurns = 0;
        score = 0;
        previousTurns = new TreeMap<>();
        gameOver = false;
        playerWon = false;
        board = new int[4][4];
        generateRandomTile();
        generateRandomTile();
        previousTurns.put(numTurns, new GameState(score, cloneBoard(board)));
    }

    public static int[][] cloneBoard(int[][] boardToClone) {
        int[][] clonedBoard = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(boardToClone[i], 0, clonedBoard[i], 0, 4);
        }
        return clonedBoard;
    }

    int getCell(int r, int c) {
        return board[r][c];
    }

    String decrementGameState() {
        if (numTurns == 0) {
            return "Can't undo anymore.";
        }
        previousTurns.remove(numTurns);
        numTurns -= 1;
        score = previousTurns.get(numTurns).getScore();
        board = previousTurns.get(numTurns).getBoard();
        if (playerHasWon()) {
            gameOver = true;
            playerWon = true;
        } else {
            gameOver = checkGameOver();
            playerWon = false;
        }
        return "One turn undone.";
    }

    String writeGameToFile() {
        File file = getValidFile();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            Iterator<Integer> keys = previousTurns.keySet().iterator();
            Iterator<GameState> values = previousTurns.values().iterator();
            while (keys.hasNext()) {
                int currentTurnNumber = keys.next();
                GameState currentGameState = values.next();
                bw.write(String.valueOf(currentTurnNumber));
                bw.newLine();
                bw.write(String.valueOf(currentGameState.getScore()));
                bw.newLine();
                int[][] currentBoard = currentGameState.getBoard();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        bw.write(currentBoard[i][j] + " ");
                    }
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
            return "Game saved.";
        } catch (IOException e) {
            System.err.println("FileIO Exception.");
            return "FileIO Exception.";
        }
    }

    public static boolean isPowerOfTwo(int num) {
        if (num > 0) {
            while ((num % 2) == 0) {
                num /= 2;
            }
        }
        return (num == 1 || num == 0);
    }

    String readGame(String filePath) {
        try {
            TreeMap<Integer, GameState> newGame = new TreeMap<>();
            Scanner scan = new Scanner(new File(filePath));
            int numberOfTurns = 0;
            int newScore = 0;
            if (!scan.hasNextInt()) {
                System.err.println("Invalid file.");
                return "Invalid file.";
            }
            while (scan.hasNextInt()) {
                numberOfTurns = scan.nextInt();
                if (numberOfTurns > 0 && !newGame.containsKey(numberOfTurns - 1)) {
                    System.err.println("Invalid file.");
                    return "Invalid file.";
                }
                newScore = scan.nextInt();
                if (numberOfTurns > 0 && newScore < newGame.get(numberOfTurns - 1).getScore()) {
                    System.err.println("Invalid file.");
                    return "Invalid file.";
                }
                int[][] newBoard = new int[4][4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        int nextNum = scan.nextInt();
                        if (nextNum <= 2048 && isPowerOfTwo(nextNum)) {
                            newBoard[i][j] = nextNum;
                        } else {
                            System.err.println("Invalid file.");
                            return "Invalid file.";
                        }
                    }
                }
                newGame.put(numberOfTurns, new GameState(newScore, newBoard));
            }
            score = newScore;
            numTurns = numberOfTurns;
            board = newGame.get(numTurns).getBoard();
            previousTurns = newGame;
            if (playerHasWon()) {
                gameOver = true;
            } else {
                gameOver = checkGameOver();
                playerWon = false;
            }
            return "Game loaded.";
        } catch (FileNotFoundException e) {
            System.err.println("Invalid file.");
            return "File not found.";
        } catch (NoSuchElementException e) {
            System.err.println("Invalid file.");
            return "Invalid file.";
        } catch (IllegalStateException e) {
            System.err.println("Scanner was closed.");
            return "Scanner was closed.";
        }
    }

    public static File getValidFile() {
        String currentFilePath = "files/";
        String currentFileName = "game1";
        String suffix = ".txt";
        int i = 1;
        File file = new File(currentFilePath + currentFileName + suffix);
        while (file.exists()) {
            i++;
            currentFileName = "game" + i;
            file = new File(currentFilePath + currentFileName + suffix);
        }
        return file;
    }

    public int[][] getBoard() {
        return cloneBoard(board);
    }

    public void setBoard(int[][] board) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int currentTileValue = board[i][j];
                if (currentTileValue > 2048 || !isPowerOfTwo(currentTileValue)) {
                    return;
                }
            }
        }
        this.board = board;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getPlayerWon() {
        return playerWon;
    }
}
