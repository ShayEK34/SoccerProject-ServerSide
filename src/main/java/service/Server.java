package service;

import Domain.Model;
import Domain.Systems.SystemErrorLogs;
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
    private static SystemErrorLogs syserror=new SystemErrorLogs();

    public static void main(String args[]) throws IOException {

        ServerSocket listener = new ServerSocket(PORT);
        try {
            syslog.addEventLog("Server","Connected to the server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            syslog.addEventLog("ExternalSystems","Connected to External Systems");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                System.out.println("Server waiting for client connection");
                taxSystem.setConnect(true);
                accountingSystem.setConnect(true);
                Socket client = listener.accept();
                System.out.println("Server is connected to client!");
                ClientHandler clientThread = new ClientHandler((client),clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            } catch (SocketException e) {
                try {
                    syserror.addErrorLog("Server","Connection Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (e.toString().contains("Socket closed") || e.toString().contains("Connection reset")
                        || e.toString().contains("Broken pipe")) {
                }
            } catch (Exception e) {
                try {
                    syserror.addErrorLog("Server","Connection Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}

