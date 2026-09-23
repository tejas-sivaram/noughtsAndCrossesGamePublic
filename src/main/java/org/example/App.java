package org.example;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

public class App extends Application{
    int cell_size = 80;
    int board_size = 3;
    String turn = "O";
    Button[][] cells = new Button[3][3];
    GridPane grid = new GridPane();
    Label message = new Label("Player " + turn + "'s turn");
    VBox vbox = new VBox(16);
    Scene scene = new Scene(vbox, cell_size * board_size, cell_size * board_size + 20);
    @Override
    public void start(Stage stage) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                Button cell = new Button("");
                final int rowf = row;
                final int columnf = column;
                cell.setOnAction(e -> handleCellClick(rowf, columnf));
                cell.setPrefSize(80, 80);  //dimensions of cells
                grid.add(cell, column, row);  // add(node, col, row)
                cells[row][column] = cell;
            }
        }

        vbox.getChildren().addAll(grid, message);
        stage.setScene(scene);
        stage.setTitle("App");
        stage.show();
    }

    private void handleCellClick(int row, int column) {
        if (!(cells[row][column].getText().equals("X") || cells[row][column].getText().equals("O"))) {
            cells[row][column].setText(turn);
            if (gameWin()) {
                System.out.println("gg " + turn);
                resetBoard();
            }
            if (gameDraw()) {
                System.out.println("draw");
                resetBoard();
            }
            if (turn.equals("X")) {
                turn = "O";
            } else {
                turn = "X";
            }
            message.setText("Player " + turn);
        }
    }

    private boolean gameDraw() {
        for (Button[] row : cells) {
            for (Button cell : row) {
                if (!cell.getText().equals("O") && !cell.getText().equals("X")) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean gameWin() {
        boolean row_complete = false;
        boolean column_complete = false;
        for (int i = 0; i < board_size; i++) {
            row_complete = true;
            column_complete = true;
            for (int j = 0; j < board_size - 1; j++) {
                if ((!cells[i][j].getText().equals(cells[i][j + 1].getText())) || cells[i][j].getText().isEmpty()) {
                    row_complete = false;
                }
                if ((!cells[j][i].getText().equals(cells[j + 1][i].getText())) || cells[j][i].getText().isEmpty()) {
                    column_complete = false;
                }
            }
            if (row_complete || column_complete) {
                return true;
            }
        }
        boolean diag1_complete = true;
        boolean diag2_complete = true;
        for (int i = 0; i < board_size - 1; i++) {
            if ((!cells[i][i].getText().equals(cells[i + 1][i + 1].getText())) || cells[i][i].getText().isEmpty()) {
                diag1_complete = false;
            }
            if ((!cells[(board_size - i) - 1][i].getText().equals(cells[(board_size - i) - 2][i + 1].getText())) || cells[(board_size - i) - 1][i].getText().isEmpty()) {
                diag2_complete = false;
            }
        }
        return diag1_complete || diag2_complete;
    }

    private void resetBoard() {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                cells[i][j].setText("");
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
