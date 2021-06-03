package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private int port;
    private String minute;
    private String hour;
    private String userIp;
    private String command;

    private Socket socket;

    public ClientThread(String userIp, String command, int port, String minute, String hour) {
        this.port = port;
        this.minute = minute;
        this.hour = hour;
        this.userIp = userIp;
        this.command = command;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(userIp, port);
            if (socket == null) {
                Log.e("timeServerThread", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("timeServerThread", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            String tmp;
            if (command.equals("set")) {
                tmp = command + "," + hour + "," + minute;
            } else {
                tmp = command;
            }
            printWriter.println(tmp);
            printWriter.flush();
            String serverResponse;
            while ((serverResponse = bufferedReader.readLine()) != null) {
                System.out.println(serverResponse);
            }
        } catch (IOException ioException) {
            Log.e("timeServerThread", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("timeServerThread", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }
}
