package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText hourEditText;
    private EditText minuteEditText;
    private Button setButton, resetButton, pollButton, connectButton;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;
    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            serverThread = new ServerThread(7314);
            if (serverThread.getServerSocket() == null) {
                Log.e("timeServerThread", "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private SetButtonClickListener setButtonClickListener = new SetButtonClickListener();
    private class SetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String hour = hourEditText.getText().toString();
            String minute = minuteEditText.getText().toString();

            clientThread = new ClientThread(clientAddress, "set", 7314, minute, hour);
            clientThread.start();
        }
    }

    private ResetButtonClickListener resetButtonClickListener = new ResetButtonClickListener();
    private class ResetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String hour = hourEditText.getText().toString();
            String minute = minuteEditText.getText().toString();

            clientThread = new ClientThread(clientAddress, "reset", 7314, minute, hour);
            clientThread.start();
        }
    }

    private PollButtonClickListener pollButtonClickListener = new PollButtonClickListener();
    private class PollButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String hour = hourEditText.getText().toString();
            String minute = minuteEditText.getText().toString();

            clientThread = new ClientThread(clientAddress, "poll", 7314, minute, hour);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        hourEditText = (EditText)findViewById(R.id.editTextTextPersonName);
        minuteEditText = (EditText)findViewById(R.id.editTextTextPersonName2);

        setButton = (Button)findViewById(R.id.button);
        resetButton = (Button)findViewById(R.id.button2);
        pollButton = (Button)findViewById(R.id.button3);
        connectButton = (Button)findViewById(R.id.button4);

        connectButton.setOnClickListener(connectButtonClickListener);
        setButton.setOnClickListener(setButtonClickListener);
        resetButton.setOnClickListener(resetButtonClickListener);
        pollButton.setOnClickListener(pollButtonClickListener);
    }

    @Override
    protected void onDestroy() {
        Log.i("timeServerThread", "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}