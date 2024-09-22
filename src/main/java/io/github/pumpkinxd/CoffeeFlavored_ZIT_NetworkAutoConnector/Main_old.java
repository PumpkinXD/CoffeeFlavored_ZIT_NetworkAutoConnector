package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;


/**
 * testing codes are here.........
 * remove in future......
 * @author PumpkinXD
 */



import java.util.concurrent.atomic.AtomicBoolean;

public class Main_old {


    static String eportal;
    static String querying_string;

    static public AtomicBoolean WorkerRunning = new AtomicBoolean(false);
    static public AtomicBoolean WorkerPausing = new AtomicBoolean(false);


    public static void main(String[] args) {

//        var rsp_fromKDE = Networking.TryConnect2_networkcheck_kde_org();
//        System.out.println(rsp_fromKDE.getLeft() + "\n" + rsp_fromKDE.getRight());
//        ExtractURL_test();





        Thread workerThread = new Thread(new Worker());
        while (true){
            //read command from cli...
            //stop: stop the worker thread
            //chang_password: change new password then encrypt when WorkerRunning==false/WorkerPausing==true (and RSA info is not null)
            String cmd;

        }






    }


    ///by newbing
    static public void ExtractURL_test() {
        {
            String script = "<script>top.self.location.href='http://10.100.110.12/eportal/index.jsp?wlanuserip=4aa7833e585f06d58152011e65958660&wlanacname=e7e93dc2b56e9ce19a10e4caccd575fe&ssid=&nasip=d7a4fc3b3dafc413f94784d07bd01cf7&mac=6769ec404d3130ddcf99a021e6351d52&t=wireless-v2&url=ddcf351fa234578223b70ac491f90cb8b2b63ae860fa91c5a3c6e0acabff1f12'";

            var result = Networking.extractURLAndParams(script);
            if (result != null) {
                System.out.println("Extracted URL: " + result.getKey());
                System.out.println("Extracted Parameters: " + result.getValue());
                var keyinfo = Networking.extractRSAPublicKeyExponentAndRSAPublicKeyModulus(result);
                if (keyinfo != null) {
                    System.out.println("PublicKeyExponent:" + keyinfo.getLeft());
                    System.out.println("PublicKeyModulus:" + keyinfo.getRight());
                }


            } else {
                System.out.println("URL or parameters not found");
            }
        }


    }


}

class Worker implements Runnable {
    public void run() {

        while (Main_old.WorkerRunning.get()) {
            var respondAndInfo = Networking.TryConnect2_networkcheck_kde_org();
            assert respondAndInfo != null;
            if (respondAndInfo.getLeft().equals(200)&&respondAndInfo.getRight().startsWith("<script>top.self.location.href='"))
            {
                var URLAndQueryString=Networking.extractURLAndParams(respondAndInfo.getRight());

                assert URLAndQueryString != null;
                var RSAPublicKeyExponentAndRSAPublicKeyModulus=Networking.extractRSAPublicKeyExponentAndRSAPublicKeyModulus(URLAndQueryString);

                var config=ConfigManager.getConfig();
                var rwlock=ConfigManager.getLock();
                //if config.PubKeyModulus != null and PubKeyExponent != null
                //check RSAPublicKeyExponentAndRSAPublicKeyModulus==config
                //if config == null, request the user input password


                synchronized (config){
                    while (!(RSAPublicKeyExponentAndRSAPublicKeyModulus.getLeft().equals(config.getPubKeyExponent())||!(RSAPublicKeyExponentAndRSAPublicKeyModulus.getRight().equals(config.getPubKeyModulus())))){
                        //out:empty ID/PWD or PWD expired
                        Main_old.WorkerPausing.set(true);
                        try {
                            config.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Main_old.WorkerPausing.set(false);
                    }
                }





                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } else if (respondAndInfo.getLeft().equals(200)&&respondAndInfo.getRight().equals("OK")) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //TODO:
        }
    }
}
