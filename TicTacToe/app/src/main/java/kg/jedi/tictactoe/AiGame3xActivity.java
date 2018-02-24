package kg.jedi.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import kg.jedi.tictactoe.ai.Algorithms;
import kg.jedi.tictactoe.ai.GameStateListener;

public class AiGame3xActivity extends AppCompatActivity {

    private final static int GAME_X = 3;
    private final static String STATE_X = "X";
    private final static String STATE_O = "O";

    private Board board;
    private Button[][] cells;
    private SparseArray<String> btnVals;
    private EvenListener evenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_game3x);

        init();
        setListeners();
    }

    private void init() {
        evenListener = new EvenListener();
        btnVals = new SparseArray<>();
        cells = new Button[GAME_X][GAME_X];
        board = new Board(GAME_X, evenListener);

        cells[0][0] = findViewById(R.id.b0);
        cells[0][1] = findViewById(R.id.b1);
        cells[0][2] = findViewById(R.id.b2);
        cells[1][0] = findViewById(R.id.b3);
        cells[1][1] = findViewById(R.id.b4);
        cells[1][2] = findViewById(R.id.b5);
        cells[2][0] = findViewById(R.id.b6);
        cells[2][1] = findViewById(R.id.b7);
        cells[2][2] = findViewById(R.id.b8);

        btnVals.append(R.id.b0, "0:0");
        btnVals.append(R.id.b1, "0:1");
        btnVals.append(R.id.b2, "0:2");
        btnVals.append(R.id.b3, "1:0");
        btnVals.append(R.id.b4, "1:1");
        btnVals.append(R.id.b5, "1:2");
        btnVals.append(R.id.b6, "2:0");
        btnVals.append(R.id.b7, "2:1");
        btnVals.append(R.id.b8, "2:2");
    }

    private void setListeners() {
        for (Button[] bs : cells) {
            for (Button b : bs) {
                b.setOnClickListener(evenListener);
            }
        }
    }

    private void move(int x, int y) {
        if (!board.isGameOver()) {
            boolean validMove = board.move(x, y);
            if (validMove && !board.isGameOver()) {
                Algorithms.alphaBetaAdvanced(board);
            }
        }
    }

    private void repaintBoard() {
        Board.State[][] boardArray = board.toArray();

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (boardArray[y][x] == Board.State.X) {
                    cells[x][y].setText(STATE_X);
                } else if (boardArray[y][x] == Board.State.O) {
                    cells[x][y].setText(STATE_O);
                }
            }
        }
    }

    private void finishGame() {
        Board.State winner = board.getWinner();
        String message = "No one won this game";

        if (winner == Board.State.X) {
            message = "The player X is the winner!";
        } else if (winner == Board.State.O) {
            message = "The player O is the winner!";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AiGame3xActivity.this);
        builder.setMessage(message)
                .setPositiveButton("New Game", (dialog, id) -> {
                    dialog.dismiss();
                    recreate();
                });

        builder.show();
    }

    private class EvenListener implements View.OnClickListener, GameStateListener {

        @Override
        public void onClick(View v) {
            Button button = (Button) v;

            if (button.getText().toString().trim().isEmpty()) {
                int id = button.getId();
                String[] indexes = btnVals.get(id).split(":");
                Integer x = Integer.valueOf(indexes[0]);
                Integer y = Integer.valueOf(indexes[1]);

                move(x, y);
            }
        }

        @Override
        public void onMove(boolean state) {
            repaintBoard();
            if (state) {
                finishGame();
            }
        }
    }
}
