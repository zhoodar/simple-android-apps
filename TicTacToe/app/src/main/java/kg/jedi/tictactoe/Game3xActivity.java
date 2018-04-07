package kg.jedi.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game3xActivity extends AppCompatActivity {

    private EvenListener eventListener;
    private int F_PLAYER = 1;
    private int S_PLAYER = 2;
    private String markX = "X";
    private String markO = "O";
    private List<String> xsIds = new ArrayList<>();
    private List<String> osIds = new ArrayList<>();

    final List<String> winPatterns = Arrays.asList("b1:b2:b3", "b1:b4:b7", "b1:b5:b9",
            "b2:b5:b8", "b4:b5:b6", "b3:b5:b7", "b3:b6:b9", "b9:b8:b7");

    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private int TURN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        init();
        setListeners();
    }

    private void init() {
        eventListener = new EvenListener();

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);

        TURN = F_PLAYER;
    }

    private void setListeners() {
        b1.setOnClickListener(eventListener);
        b2.setOnClickListener(eventListener);
        b3.setOnClickListener(eventListener);
        b4.setOnClickListener(eventListener);
        b5.setOnClickListener(eventListener);
        b6.setOnClickListener(eventListener);
        b7.setOnClickListener(eventListener);
        b8.setOnClickListener(eventListener);
        b9.setOnClickListener(eventListener);
    }

    private void handleGame(String v, String id) {
        if (v.equals(markO)) {
            osIds.add(id);
        } else {
            xsIds.add(id);
        }

        if (xsIds.size() >= 3 && checkWinner(xsIds)) {

            finishGame("The player X is winner!");
        } else if (osIds.size() >= 3 && checkWinner(osIds)) {
            finishGame("The player O is winner!");
        }

        if (xsIds.size() == 5 && osIds.size() == 4 ||
                xsIds.size() == 4 && osIds.size() == 5) {
            finishGame("No one won a game");
        }
    }

    private void finishGame(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Game3xActivity.this);
        builder.setMessage(message)
                .setPositiveButton("New Game", (dialog, id) -> {
                    dialog.dismiss();
                    recreate();
                });

        builder.show();
    }

    private boolean checkWinner(List<String> pttS) {
        for (String wP : winPatterns) {
            List<String> p = Arrays.asList(wP.split(":"));

            if (pttS.containsAll(p)) {
                return true;
            }
        }

        return false;
    }

    private String getXmlId(int id) {
        switch (id) {
            case R.id.b1:
                return "b1";
            case R.id.b2:
                return "b2";
            case R.id.b3:
                return "b3";
            case R.id.b4:
                return "b4";
            case R.id.b5:
                return "b5";
            case R.id.b6:
                return "b6";
            case R.id.b7:
                return "b7";
            case R.id.b8:
                return "b8";
            case R.id.b9:
                return "b9";
            default:
                return "b10";
        }
    }

    private class EvenListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button button = (Button) v;

            if (button.getText().toString().trim().isEmpty()) {
                if (TURN == F_PLAYER) {
                    TURN = S_PLAYER;
                    button.setText(markX);

                } else if (TURN == S_PLAYER) {
                    TURN = F_PLAYER;
                    button.setText(markO);
                }
            }
            handleGame(button.getText().toString(), getXmlId(button.getId()));
        }
    }

}
