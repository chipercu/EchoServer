package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



public class Server {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {

        final Server server = new Server();

        try(ServerSocket serverSocket = new ServerSocket(8189)) {

            System.out.println("Сервер запущен. \n Ожидается подключение...");
            server.setSocket(serverSocket.accept());
            System.out.println("Клиент подключился");
            server.setIn(new DataInputStream(server.getSocket().getInputStream()));
            server.setOut(new DataOutputStream(server.getSocket().getOutputStream()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Scanner scanner = new Scanner(System.in);
                    while (true){
                        final String msg = scanner.nextLine();
                        try {
                            server.getOut().writeUTF(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            while (true){
                final String msg = server.getIn().readUTF();
                System.out.println("Client : " + msg);

                if ("/q".equalsIgnoreCase(msg)){
                    server.getOut().writeUTF("/q");
                    break;
                }
               // server.getOut().writeUTF( msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Сервер остановлен");

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

}
