package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.HashMap;
import java.util.TooManyListenersException;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e("timeServerThread", "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("timeServerThread", "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i("timeServerThread", "[COMMUNICATION THREAD] Waiting for parameters from client time information!");
            String command = bufferedReader.readLine();
            HashMap<String, TimeInformation> data = serverThread.getData();

            command = command.replace("\n", "");
            String[] arg = command.split(",");

            String userIp = "a";

            switch (arg[0]) {
                case "set":
                    TimeInformation timeInformation = new TimeInformation(arg[1], arg[2]);
                    data.put(userIp, timeInformation);
                    printWriter.println("all set");
                    break;
                case "reset":
                    data.remove(userIp);
                    printWriter.println("reset");
                    break;
                case "poll":
                    String dayTimeProtocol = null;
                    try {
                        Socket socket = new Socket("utcnist.colorado.edu", 13);
                        BufferedReader bufferedReader2 = Utilities.getReader(socket);
                        bufferedReader2.readLine();
                        dayTimeProtocol = bufferedReader2.readLine();
                        System.out.println(dayTimeProtocol);
                        String date = dayTimeProtocol.split(" ")[2];
                        String hour =  date.split(":")[0];
                        String minute =  date.split(":")[1];

                        TimeInformation timeInformation1 = data.get(userIp);
                        if (timeInformation1 == null) {
                            printWriter.println("none\n");
                        } else if (Integer.parseInt(timeInformation1.getHour()) > Integer.parseInt(hour)) {
                            printWriter.println("inactive\n");
                        } else if ((Integer.parseInt(timeInformation1.getHour()) < Integer.parseInt(hour))) {
                            printWriter.println("active\n");
                        } else if ((Integer.parseInt(timeInformation1.getHour()) == Integer.parseInt(hour))) {
                            if ((Integer.parseInt(timeInformation1.getMinute()) >= Integer.parseInt(minute))) {
                                printWriter.println("inactive\n");
                            } else {
                                printWriter.println("active\n");
                            }
                        }
                    } catch (UnknownHostException unknownHostException) {
                        Log.d("timeServerThread", unknownHostException.getMessage());
                    } catch (IOException ioException) {
                        Log.d("timeServerThread", ioException.getMessage());
                    }
                    break;
            }

        } catch (IOException ioException) {
            Log.e("timeServerThread", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("timeServerThread", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}