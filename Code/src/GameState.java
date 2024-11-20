public class GameState implements Comparable<GameState> {
    final private int score;

    final private int[][] board;

    GameState(int score, int[][] board) {
        this.score = score;
        this.board = board;
    }

    public int getScore() {
        return score;
    }

    public int[][] getBoard() {
        return TwentyFortyEight.cloneBoard(board);
    }

    @Override
    public int compareTo(GameState o) {
        if (this.score > o.getScore()) {
            return 1;
        } else if (this.score < o.getScore()) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        GameState that = (GameState) o;
        return (that.getScore() == this.score);
    }
}
