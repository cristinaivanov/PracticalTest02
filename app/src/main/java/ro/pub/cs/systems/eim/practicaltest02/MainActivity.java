package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText hourEditText;
    private EditText minuteEditText;
    private Button setButton, resetButton, pollButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        hourEditText = (EditText)findViewById(R.id.editTextTextPersonName);
        minuteEditText = (EditText)findViewById(R.id.editTextTextPersonName2);

        setButton = (Button)findViewById(R.id.button);
        resetButton = (Button)findViewById(R.id.button2);
        pollButton = (Button)findViewById(R.id.button3);
    }
}