package kg.jedi.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnTwoPlayers, btnSinglePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSinglePlayer = findViewById(R.id.btnSinglePlayer);
        btnTwoPlayers = findViewById(R.id.btnTwoPlayer);

        btnSinglePlayer.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,"Sooon, will be implemented! :)", Toast.LENGTH_LONG).show();
        });

        btnTwoPlayers.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Game3xActivity.class));
        });
    }
}
