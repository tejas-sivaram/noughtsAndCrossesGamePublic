package org.example;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.application.Platform;

public class NoughtsAndCrossesApp extends Application{
    int cell_size = 150;
    int board_size;
    String playerNameX = "";
    String playerNameO = "";
    int scoreX = 0;
    int scoreO = 0;
    int draws = 0;
    GameBoard gameBoard;
    Button[][] cells;
    Button scoreViewer;
    Label message = new Label("");
    @Override
    public void start(Stage stage) {
        stage.setScene(createStartupScene(stage));
        stage.setTitle("Noughts and Crosses game");
        stage.show();
    }

    private void handleCellClick(int row, int column) {
        if (gameBoard.makeMove(row, column)) {
            cells[row][column].setText(gameBoard.getCurrentPlayer());
            if (!gameBoard.checkWinner().isEmpty()) {
                showWinnerPopup(gameBoard.checkWinner());
            } else if (gameBoard.checkDraw()) {
                showDrawPopup();
            }
            gameBoard.switchPlayer();
            updateMessage();
        }
    }

    private void resetBoard() {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                cells[i][j].setText("");
            }
        }
        gameBoard.resetBoard();
    }

    public void updateMessage() {
        if (gameBoard.getCurrentPlayer().equals("X")) {
            message.setText("Player " + playerNameX + "'s turn (X)");
        } else {
            message.setText("Player " + playerNameO + "'s turn (O)");
        }
    }

    private Scene createStartupScene(Stage stage) {
        Label title = new Label("Noughts & Crosses");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(cell_size * 3);

        Label nameLabel = new Label("Enter players' names:");
        TextField nameFieldX = new TextField();
        nameFieldX.setPromptText("Player X name:");
        TextField nameFieldO = new TextField();
        nameFieldO.setPromptText("Player O name:");
        TextField boardSizeField = new TextField();
        boardSizeField.setPromptText("Board size (default of 3)");

        Button startButton = new Button("Start Game");
        startButton.setAlignment(Pos.CENTER);
        startButton.setMaxWidth(cell_size * 3);
        startButton.setStyle("-fx-background-color: #50c878; -fx-text-fill: white;");
        startButton.setOnAction(e -> {
            String nameX = nameFieldX.getText().trim();
            if (!nameFieldX.getText().trim().isEmpty() && !nameFieldO.getText().trim().isEmpty() && !nameFieldX.getText().trim().equals(nameFieldO.getText().trim())) {
                playerNameX = nameFieldX.getText().trim();
                playerNameO = nameFieldO.getText().trim();
                try {
                    if (boardSizeField.getText().trim().isEmpty()) {
                        board_size = 3;
                    } else {
                        board_size = Integer.parseInt(boardSizeField.getText().trim());
                        if (board_size < 3) {
                            throw new NumberFormatException("too small");
                        }
                        if (board_size >= 7) {
                            cell_size = 900 / board_size;
                        }
                    }
                    gameBoard = new GameBoard(board_size);
                    cells = new Button[board_size][board_size];
                    stage.setScene(createGameScene(stage));
                } catch (NumberFormatException exceptionE) {
                    nameLabel.setText("board size must be an integer which is at least 3");
                }
            } else if (nameFieldX.getText().trim().equals(nameFieldO.getText().trim())) {
                nameLabel.setText("Both names cannot be the same!");
            } else {
                nameLabel.setText("Please enter two names");
            }
        });

        VBox layout = new VBox(16);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(title, nameLabel, nameFieldX, nameFieldO, boardSizeField, startButton);
        return new Scene(layout, 400, 300);
    }

    private Scene createGameScene(Stage stage) {
        message.setStyle("-fx-font-size: 24px;");
        message.setAlignment(Pos.CENTER);
        message.setMaxWidth(cell_size * board_size);
        updateMessage();
        GridPane grid = new GridPane();
        VBox vbox = new VBox(16);
        Scene scene = new Scene(vbox, cell_size * board_size, cell_size * board_size + 20);
        stage.setWidth(cell_size * board_size);
        stage.setHeight(cell_size * board_size + 60);
        for (int row = 0; row < board_size; row++) {
            for (int column = 0; column < board_size; column++) {
                Button cell = new Button("");
                final int rowf = row;
                final int columnf = column;
                cell.setOnAction(e -> handleCellClick(rowf, columnf));
                cell.setPrefSize(cell_size, cell_size);
                grid.add(cell, column, row);
                cells[row][column] = cell;
            }
        }
        Button scoreViewer = new Button("View score");
        scoreViewer.setOnAction(e -> showScorePopup());
        scoreViewer.setPrefWidth(cell_size * board_size * 0.67);
        scoreViewer.setPrefHeight(cell_size * 0.5);
        scoreViewer.setMaxWidth(cell_size * board_size * 0.67);
        scoreViewer.setStyle("-fx-background-color: #0096FF; -fx-text-fill: white;");
        Button quitButton = new Button("Quit game");
        quitButton.setOnAction(e -> Platform.exit());
        quitButton.setPrefWidth(cell_size * board_size * 0.67);
        quitButton.setPrefHeight(cell_size * 0.5);
        quitButton.setMaxWidth(cell_size * board_size * 0.67);
        quitButton.setStyle("-fx-background-color: #d2042d; -fx-text-fill: white;");
        VBox buttonBox = new VBox(8);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(scoreViewer, quitButton);
        grid.setPadding(new Insets(20));
        vbox.getChildren().addAll(grid, buttonBox, message);
        return scene;
    }

    private void showWinnerPopup(String winner) {
        String winnerName;
        if (winner.equals("X")) {
            scoreX += 1;
            winnerName = playerNameX;
        } else {
            scoreO += 1;
            winnerName = playerNameO;
        }
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle("Game over!");
        popup.setHeaderText("Player " + winnerName + " (" + winner + ") wins!");
        popup.setContentText("Current standings:\n" + playerNameX + "(X) " + scoreX + " - " + scoreO + " "
        + playerNameO + " (O)\nDraws: " + draws);
        popup.showAndWait();
        resetBoard();
    }

    private void showDrawPopup() {
        draws += 1;
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle("Game over.");
        popup.setHeaderText("Draw");
        popup.setContentText("Current standings:\n" + playerNameX + " (X) " + scoreX + " - " + scoreO + " "
        + playerNameO + " (O)\nDraws: " + draws);
        popup.showAndWait();
        resetBoard();
    }

    private void showScorePopup() {
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle("View score");
        if (scoreX > scoreO) {
            popup.setHeaderText("Player " + playerNameX + " (X) is winning.");
        } else if (scoreO > scoreX) {
            popup.setHeaderText("Player " + playerNameO + " (O) is winning.");
        } else {
            popup.setHeaderText("Both players are currently tied");
        }
        popup.setContentText("Current standings:\n" + playerNameX + " (X) " + scoreX + " - " + scoreO + " "
        + playerNameO + " (O)\nDraws: " + draws);
        popup.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
