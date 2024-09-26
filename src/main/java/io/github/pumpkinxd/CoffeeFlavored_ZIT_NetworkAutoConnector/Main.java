package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
                            var encrypted = Encrypting.ZIT_Network_Encrypt(Pair.of(config.getPubKeyExponent(), config.getPubKeyModulus()), password,config.getMac());
                            config.setEncryptedPWD(encrypted);
                            config.notify();
                        }
                    }

                } else if (line.equals("ChangePassword")) {
                    synchronized (config) {
                        if (workerThread.isblocked.get()) {
                            var new_pwd = lineReader.readLine("Enter new password: ", '*');
                            var encrypted = Encrypting.ZIT_Network_Encrypt(Pair.of(config.getPubKeyExponent(), config.getPubKeyModulus()), new_pwd,config.getMac());
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

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (keeplooping.get()) {
            var rsp_fromKDE = Networking.TryConnect2_networkcheck_kde_org();
            if (rsp_fromKDE != null) {
                if (rsp_fromKDE.getRight().contains("<script>top.self.location.href='")) {
                    var uRLAndQueryString = StringParser.extractURLAndQueryString(rsp_fromKDE.getRight());
                    var keyinfo = Networking.extractRSAPublicKeyExponentAndRSAPublicKeyModulus(uRLAndQueryString);
                    var mac = StringParser.getMacFromQueryString(uRLAndQueryString.getRight());
                    if(config.getMac().isEmpty()) {config.setMac(mac);}
                    while (!keyinfo.getLeft().equals(config.getPubKeyExponent()) || !keyinfo.getRight().equals(config.getPubKeyModulus()) || config.getUserID().isEmpty() || config.getEncryptedPWD().isEmpty()) {
                        System.out.println("Password expired, please re-enter password or change account");
                        isblocked.set(true);
                        try {
                            config.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    while (true){
                        var respond = Networking.PostLoginRequest(config.getUserID(), config.getEncryptedPWD(), uRLAndQueryString);
                        //if (respond.getRight()) {} //respond is json, if not success ask user to change password...
                        if (respond != null && respond.getLeft().equals(200)) {
                            var loginInfo=StringParser.parseLoginRespondString(respond.getRight());
                            if (!loginInfo.getMiddle().contains("success")){
                                System.out.println("Login failed, please re-enter password or change account");
                                isblocked.set(true);
                                try {
                                    config.wait();
                                } catch (InterruptedException ignored) {}
                                continue;
                            }else break;
                        }
                    }


                }

            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("Worker thread stopped");
    }
}


