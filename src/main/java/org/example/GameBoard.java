package org.example;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

public class GameBoard {
    int boardDeficit = 3;
    //boardDeficit must be 2 for the lab
    //represents how much less than the board length in a row is required to win
    //it is safe to set to any large value
    //the code will automatically make it so at least 3 in a row is required
    int boardSize;
    String[][] board;
    String currentPlayer = "X";
    public GameBoard(int boardSize) {
        this.boardSize = boardSize;
        if (this.boardSize - boardDeficit < 2) {
            boardDeficit = this.boardSize - 2;
        }
        board = new String[boardSize][boardSize];
        resetBoard();
    }
    public void resetBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = "";
            }
        }
    }
    public void switchPlayer() {
        if (currentPlayer.equals("X")) {
            currentPlayer = "O";
        } else {
            currentPlayer = "X";
        }
    }
    public boolean makeMove(int row, int col) {
        if (board[row][col].isEmpty()) {
            board[row][col] = currentPlayer;
            return true;
        } else {
            return false;
        }
    }
    public String checkWinner() {
        if (boardSize < 5) {
            return checkWinnerSmall();
        } else {
            return checkWinnerBig();
        }
    }
    private String checkWinnerSmall() {
        boolean row_complete = false;
        boolean column_complete = false;
        for (int i = 0; i < boardSize; i++) {
            row_complete = true;
            column_complete = true;
            for (int j = 0; j < boardSize - 1; j++) {
                if ((!board[i][j].equals(board[i][j + 1])) || board[i][j].isEmpty()) {
                    row_complete = false;
                }
                if ((!board[j][i].equals(board[j + 1][i])) || board[j][i].isEmpty()) {
                    column_complete = false;
                }
            }
            if (row_complete || column_complete) {
                return currentPlayer;
            }
        }
        boolean diag1_complete = true;
        boolean diag2_complete = true;
        for (int i = 0; i < boardSize - 1; i++) {
            if ((!board[i][i].equals(board[i + 1][i + 1])) || board[i][i].isEmpty()) {
                diag1_complete = false;
            }
            if ((!board[(boardSize - i) - 1][i].equals(board[(boardSize - i) - 2][i + 1])) || board[(boardSize - i) - 1][i].isEmpty()) {
                diag2_complete = false;
            }
        }
        if (diag1_complete || diag2_complete) {
            return currentPlayer;
        } else {
            return "";
        }
    }

    private String checkWinnerBig() {
        for (int i = 0; i < (boardDeficit + 1); i++) {
            for (int j = 0; j < (boardDeficit + 1); j++) {
                String[][] smallBoard = getSmallBoard(i, j);
                if (!checkWinnerInner(smallBoard).isEmpty()) {
                    return checkWinnerInner(smallBoard);
                }
            }
        }
        return "";
    }

    private String[][] getSmallBoard(int offsetX, int offsetY) {
        String[][] smallBoard = new String[boardSize - boardDeficit][boardSize - boardDeficit];
        for (int i = offsetX; i < offsetX + (boardSize - boardDeficit); i++) {
            for (int j = offsetY; j < offsetY + (boardSize - boardDeficit); j++) {
                smallBoard[i - offsetX][j - offsetY] = board[i][j];
            }
        }
        return smallBoard;
    }

    private String checkWinnerInner(String[][] board) {
        int boardSize = this.boardSize - boardDeficit;
        boolean row_complete = false;
        boolean column_complete = false;
        for (int i = 0; i < boardSize; i++) {
            row_complete = true;
            column_complete = true;
            for (int j = 0; j < boardSize - 1; j++) {
                if ((!board[i][j].equals(board[i][j + 1])) || board[i][j].isEmpty()) {
                    row_complete = false;
                }
                if ((!board[j][i].equals(board[j + 1][i])) || board[j][i].isEmpty()) {
                    column_complete = false;
                }
            }
            if (row_complete || column_complete) {
                return currentPlayer;
            }
        }
        boolean diag1_complete = true;
        boolean diag2_complete = true;
        for (int i = 0; i < boardSize - 1; i++) {
            if ((!board[i][i].equals(board[i + 1][i + 1])) || board[i][i].isEmpty()) {
                diag1_complete = false;
            }
            if ((!board[(boardSize - i) - 1][i].equals(board[(boardSize - i) - 2][i + 1])) || board[(boardSize - i) - 1][i].isEmpty()) {
                diag2_complete = false;
            }
        }
        if (diag1_complete || diag2_complete) {
            return currentPlayer;
        } else {
            return "";
        }
    }

    public boolean checkDraw() {
        for (String[] row : board) {
            for (String square : row) {
                if (!square.equals("O") && !square.equals("X")) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getCurrentPlayer() {
        return new String(currentPlayer);
    }
}
