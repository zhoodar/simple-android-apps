package kg.jedi.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EvenListener eventListener;
    private int F_PLAYER = 1;
    private int S_PLAYER = 2;
    private String markX = "X";
    private String markO = "O";
    private List<String> xsIds = new ArrayList<>();
    private List<String> osIds = new ArrayList<>();

    final List<String> wPatterns = Arrays.asList("b1:b2:b3", "b1:b4:b7", "b1:b5:b9",
            "b2:b5:b8", "b4:b5:b6", "b3:b5:b7", "b3:b6:b9", "b9:b8:b7");

    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private int TURN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListeners();
    }

    private void init() {
        eventListener = new EvenListener();

        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);
        b6 = (Button) findViewById(R.id.b6);
        b7 = (Button) findViewById(R.id.b7);
        b8 = (Button) findViewById(R.id.b8);
        b9 = (Button) findViewById(R.id.b9);

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

    private void endGame(String v, String id) {
        if (v.equals(markO)) {
            osIds.add(id);
        } else {
            xsIds.add(id);
        }

        if (xsIds.size() >= 3 && checkWinner(xsIds)) {
            Toast.makeText(MainActivity.this, "Winner player X!", Toast.LENGTH_LONG).show();
        } else if (osIds.size() >= 3 && checkWinner(osIds)) {
            Toast.makeText(MainActivity.this, "Winner player O!", Toast.LENGTH_LONG).show();
        }
    }

    private void finishGame() {

    }

    private boolean checkWinner(List<String> pttS) {
        State state = new State();
        wPatterns.forEach(wP -> {
            List p = Arrays.asList(wP.split(":"));
            pttS.forEach(it -> {
                state.setState(false);
                if (p.contains(it)) {
                    state.setState(true);
                }
            });

        });

        return state.getState();
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

    private class State {

        private Boolean state;

        State() {
            this.state = false;
        }

        Boolean getState() {
            return state;
        }
        void setState(Boolean state) {
            this.state = state;
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
            endGame(button.getText().toString(), getXmlId(button.getId()));
        }
    }

}
