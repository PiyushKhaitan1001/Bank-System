package com.bank;

import com.bank.server.BankServer;
import com.bank.server.SessionManager;
import com.bank.service.BankService;
import com.bank.service.BankServiceImpl;
import com.bank.storage.FileStorage;
import com.bank.storage.Storage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        Storage storage = new FileStorage("data");
        BankService bankService = new BankServiceImpl(storage);
        SessionManager sessionManager = new SessionManager();
        BankServer server = new BankServer(port, bankService, sessionManager);

        try {
            server.start();
            System.out.println("=================================================");
            System.out.println("  Apex Banking Management System is running!     ");
            System.out.println("  Access URL: http://localhost:" + port);
            System.out.println("=================================================");
            System.out.println("Default Credentials:");
            System.out.println("  Admin:    admin / admin123");
            System.out.println("  Customer: john  / password123");
            System.out.println("  Customer: alice / password123");
            System.out.println("=================================================");
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
