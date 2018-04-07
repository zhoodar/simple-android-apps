package kg.jedi.tictactoe;

import java.util.HashSet;

import kg.jedi.tictactoe.ai.GameStateListener;

/**
 * Represents the Tic Tac Toe board.
 */
public class Board {

    private final int boardWidth;

    public enum State {Blank, X, O}

    private State[][] board;
    private State playersTurn;
    private State winner;
    private HashSet<Integer> movesAvailable;

    private GameStateListener gameStateListener;

    private int moveCount;
    private boolean gameOver;

    /**
     * Construct the Tic Tac Toe board.
     */
    public Board(int boardWidth, GameStateListener listener) {
        this.boardWidth = boardWidth;
        board = new State[this.boardWidth][this.boardWidth];
        movesAvailable = new HashSet<>();
        gameStateListener = listener;
        reset();
    }

    /**
     * Set the cells to be blank and load the available moves (all the moves are
     * available at the start of the game).
     */
    private void initialize() {
        for (int row = 0; row < boardWidth; row++) {
            for (int col = 0; col < boardWidth; col++) {
                board[row][col] = State.Blank;
            }
        }

        movesAvailable.clear();

        for (int i = 0; i < boardWidth * boardWidth; i++) {
            movesAvailable.add(i);
        }
    }

    private void reset() {
        moveCount = 0;
        gameOver = false;
        playersTurn = State.X;
        winner = State.Blank;
        initialize();
    }

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     *
     * @param index the position on the board (example: index 4 is location (0, 1))
     * @return true if the move has not already been played
     */
    public boolean move(int index) {
        return move(index % boardWidth, index / boardWidth);
    }

    /**
     * Places an X or an O on the specified location depending on who turn it is.
     *
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return true if the move has not already been played
     */
    public boolean move(int x, int y) {
        if (gameOver) {
            throw new IllegalStateException("TicTacToe is over. No moves can be played.");
        }

        if (board[y][x] == State.Blank) {
            board[y][x] = playersTurn;
        } else {
            return false;
        }

        moveCount++;
        movesAvailable.remove(y * boardWidth + x);

        if (moveCount == boardWidth * boardWidth) {
            winner = State.Blank;
            gameOver = true;
        }

        checkRow(y);
        checkColumn(x);
        checkDiagonalFromTopLeft(x, y);
        checkDiagonalFromTopRight(x, y);

        playersTurn = (playersTurn == State.X) ? State.O : State.X;
        if (null != gameStateListener) {
            gameStateListener.onMove(gameOver);
        }
        return true;
    }

    /**
     * Check to see if the game is over (if there is a winner or a draw).
     *
     * @return true if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Get a copy of the array that represents the board.
     *
     * @return the board array
     */
    public State[][] toArray() {
        return board.clone();
    }

    /**
     * Check to see who's turn it is.
     *
     * @return the player who's turn it is
     */
    public State getTurn() {
        return playersTurn;
    }

    /**
     * Check to see who won.
     *
     * @return the player who won (or Blank if the game is a draw)
     */
    public State getWinner() {
        if (!gameOver) {
            throw new IllegalStateException("TicTacToe is not over yet.");
        }
        return winner;
    }

    /**
     * Get the indexes of all the positions on the board that are empty.
     *
     * @return the empty cells
     */
    public HashSet<Integer> getAvailableMoves() {
        return movesAvailable;
    }

    /**
     * Checks the specified row to see if there is a winner.
     *
     * @param row the row to check
     */
    private void checkRow(int row) {
        for (int i = 1; i < boardWidth; i++) {
            if (board[row][i] != board[row][i - 1]) {
                break;
            }
            if (i == boardWidth - 1) {
                winner = playersTurn;
                gameOver = true;
            }
        }
    }

    /**
     * Checks the specified column to see if there is a winner.
     *
     * @param column the column to check
     */
    private void checkColumn(int column) {
        for (int i = 1; i < boardWidth; i++) {
            if (board[i][column] != board[i - 1][column]) {
                break;
            }
            if (i == boardWidth - 1) {
                winner = playersTurn;
                gameOver = true;
            }
        }
    }

    /**
     * Check the left diagonal to see if there is a winner.
     *
     * @param x the x coordinate of the most recently played move
     * @param y the y coordinate of the most recently played move
     */
    private void checkDiagonalFromTopLeft(int x, int y) {
        if (x == y) {
            for (int i = 1; i < boardWidth; i++) {
                if (board[i][i] != board[i - 1][i - 1]) {
                    break;
                }
                if (i == boardWidth - 1) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    /**
     * Check the right diagonal to see if there is a winner.
     *
     * @param x the x coordinate of the most recently played move
     * @param y the y coordinate of the most recently played move
     */
    private void checkDiagonalFromTopRight(int x, int y) {
        if (boardWidth - 1 - x == y) {
            for (int i = 1; i < boardWidth; i++) {
                if (board[boardWidth - 1 - i][i] != board[boardWidth - i][i - 1]) {
                    break;
                }
                if (i == boardWidth - 1) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    /**
     * Get a deep copy of the Tic Tac Toe board.
     *
     * @return an identical copy of the board
     */
    public Board getDeepCopy() {

        Board board = new Board(this.boardWidth, null);

        for (int i = 0; i < board.board.length; i++) {
            board.board[i] = this.board[i].clone();
        }

        board.playersTurn = this.playersTurn;
        board.winner = this.winner;
        board.movesAvailable = new HashSet<>();
        board.movesAvailable.addAll(this.movesAvailable);
        board.moveCount = this.moveCount;
        board.gameOver = this.gameOver;
        return board;
    }
}
