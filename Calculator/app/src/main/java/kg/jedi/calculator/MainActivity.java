package kg.jedi.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private EditText number1, number2;
    private Button add, divide, subtract, multiply;

    private EventListener eventListener = new EventListener();


    float calcResult, num;

    float num1, num2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListeners();
    }

    private void init() {
        result = (TextView)findViewById(R.id.result);

        number1 = (EditText)findViewById(R.id.number1);
        number2 = (EditText)findViewById(R.id.number2);

        add = (Button)findViewById(R.id.add);
        divide = (Button)findViewById(R.id.divide);
        subtract = (Button)findViewById(R.id.subtract);
        multiply = (Button)findViewById(R.id.multiply);
    }

    private void setListeners() {
        add.setOnClickListener(eventListener);
        divide.setOnClickListener(eventListener);
        subtract.setOnClickListener(eventListener);
        multiply.setOnClickListener(eventListener);
    }

    private class EventListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            num1 = Integer.valueOf(number1.getText().toString());
            num2 = Integer.valueOf(number2.getText().toString());

            if (view.equals(add)) {
                calcResult = (num1) + (num2);
            }
            if (view.equals(divide)) {
                calcResult = (num1) / (num2);
            }
            if (view.equals(multiply)) {
                calcResult = (num1) * (num2);
            }
            if (view.equals(subtract)) {
                calcResult = (num1) - (num2);
            }

            result.setText(String.valueOf(calcResult));
        }
    }
}
