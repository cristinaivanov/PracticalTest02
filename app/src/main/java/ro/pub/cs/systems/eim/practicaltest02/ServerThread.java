package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {

    private int port = 0;
    private ServerSocket serverSocket = null;

    private HashMap<String, TimeInformation> data = new HashMap<String, TimeInformation>();

    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e("timeServerThread", "An exception has occurred: " + ioException.getMessage());
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized void setData(String ip, TimeInformation timeInformation) {
        this.data.put(ip, timeInformation) ;
    }

    public synchronized HashMap<String, TimeInformation> getData() {
        return data;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i("timeServerThread", "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i("timeServerThread", "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (IOException ioException) {
            Log.e("timeServerThread", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e("timeServerThread", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());

            }
        }
    }

}