package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;


/**
 * testing codes are here.........
 * remove in future......
 *
 * @author PumpkinXD
 */


import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main_old {


    static String eportal;
    static String querying_string;

    static public AtomicBoolean WorkerRunning = new AtomicBoolean(false);
    static public AtomicBoolean WorkerPausing = new AtomicBoolean(false);


    public static void main(String[] args) {

        var rsp_fromKDE = Networking.TryConnect2_networkcheck_kde_org();
        if (rsp_fromKDE != null) {
            if (rsp_fromKDE.getRight().contains("<script>top.self.location.href='")) {
                var linkandquestr = Networking.extractURLAndParams(rsp_fromKDE.getRight());
                var keyinfo = Networking.extractRSAPublicKeyExponentAndRSAPublicKeyModulus(linkandquestr);
                var userid = " id";
                var pwd = " pwd";
                String encryped = "";
                try {
                    encryped = Encrypting.ZIT_Network_Encrypt3(keyinfo, pwd);
                } catch (Throwable ignored) {
                    ;
                }
                var res = Networking.PostLoginRequest(userid, encryped, linkandquestr);
                System.out.println(res.getLeft() + "\n" + res.getRight());
            } else {
                var keyinfo = Pair.of("10001", "94dd2a8675fb779e6b9f7103698634cd400f27a154afa67af6166a43fc26417222a79506d34cacc7641946abda1785b7acf9910ad6a0978c91ec84d40b71d2891379af19ffb333e7517e390bd26ac312fe940c340466b4a5d4af1d65c3b5944078f96a1a51a5a53e4bc302818b7c9f63c4a1b07bd7d874cef1c3d4b2f5eb7871");
                try {
                    var en = Encrypting.ZIT_Network_Encrypt3(keyinfo, "pwd");
                    System.out.println(en);

                } catch (Throwable ignore) {

                }
            }
        }
//        System.out.println(rsp_fromKDE.getLeft() + "\n" + rsp_fromKDE.getRight());
//        ExtractURL_test();


//        Thread workerThread = new Thread(new Worker());
//        while (true){
        //read command from cli...
        //stop: stop the worker thread
        //chang_password: change new password then encrypt when WorkerRunning==false/WorkerPausing==true (and RSA info is not null)
//            String cmd;

//        }


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