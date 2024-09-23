package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import org.apache.commons.lang3.tuple.Pair;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = Config.loadFromFile("./ZITNetworkAutoConnector.json");
        WorkerThread workerThread = new WorkerThread(config);
        Thread thread = new Thread(workerThread);
        workerThread.keeplooping.set(true);
        thread.start();

        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
            String prompt = "> ";
            while (true) {
                String line = lineReader.readLine(prompt);
                if (line.equals("Exit")) {
                    workerThread.keeplooping.set(false);
                    thread.join();
                    System.out.println("Shutting down...");
                    break;
                } else if (line.equals("SetIDAndPassword")) {
                    synchronized (config) {
                        if (workerThread.isblocked.get()) {
                            //input ID and password...
                            var id=lineReader.readLine("Enter ID: ");
                            var password=lineReader.readLine("Enter Password: ",'*');
                            config.setUserID(id);
                            var encrypted = Encrypting.ZIT_Network_Encrypt(Pair.of(config.getPubKeyExponent(), config.getPubKeyModulus()), password);
                            config.setEncryptedPWD(encrypted);
                            config.notify();
                        }
                    }

                } else if (line.equals("ChangePassword")) {
                    synchronized (config) {
                        if (workerThread.isblocked.get()) {
                            var new_pwd = lineReader.readLine("Enter new password: ", '*');
                            var encrypted = Encrypting.ZIT_Network_Encrypt(Pair.of(config.getPubKeyExponent(), config.getPubKeyModulus()), new_pwd);
                            config.setEncryptedPWD(encrypted);
                            config.notify();
                        } else {
                            ;//TODO:tell the user they need to stop/blocking the worker thread first?
                        }

                    }

                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


class WorkerThread implements Runnable {
    public AtomicBoolean isblocked=new AtomicBoolean(false);
    public AtomicBoolean keeplooping=new AtomicBoolean(false);

    private final Config config;

    public WorkerThread(Config config) {
        this.config = config;
    }

    @Override
    public void run() {
        while (keeplooping.get()) {}
        System.out.println("Worker thread stopped");

    }
}