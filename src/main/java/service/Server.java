package service;

import Domain.Model;
import Domain.Systems.SystemEventsLog;
import ExternalSystems.AccountingSystem;
import ExternalSystems.TaxSystem;
import ExternalSystems.proxyAccounting;
import ExternalSystems.proxyTax;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class implements java Socket server
 * @author pankaj
 *
 */
public class Server {
    //updated server
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static final int PORT = 9876;
    private static ExecutorService pool = Executors.newFixedThreadPool(100);
    private static SystemEventsLog syslog=new SystemEventsLog();
    private static proxyTax taxSystem = new proxyTax();
    private static proxyAccounting accountingSystem = new proxyAccounting();

    public static void main(String args[]) throws IOException {

        ServerSocket listener = new ServerSocket(PORT);
        while (true) {
            try {
                System.out.println("Server waiting for client connection");
                taxSystem.setConnect(true);
                accountingSystem.setConnect(true);
                syslog.addEventLog("Server","Connected to the server");
                syslog.addEventLog("ExternalSystems","Connected to External Systems");
                Socket client = listener.accept();
                System.out.println("Server is connected to client!");
                ClientHandler clientThread = new ClientHandler((client));
                clients.add(clientThread);
                pool.execute(clientThread);
            } catch (SocketException e) {
                if (e.toString().contains("Socket closed") || e.toString().contains("Connection reset")
                        || e.toString().contains("Broken pipe")) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


