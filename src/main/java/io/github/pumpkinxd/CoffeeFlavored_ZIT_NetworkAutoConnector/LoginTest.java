package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;


/**
 * testing codes are here.........
 * remove in future......
 *
 * @author PumpkinXD
 */


import org.apache.commons.lang3.tuple.Pair;

import java.util.Scanner;

public class LoginTest {

    public static void main(String[] args) {

        var rsp_fromKDE = Networking.TryConnect2_networkcheck_kde_org();
        if (rsp_fromKDE != null) {
            if (rsp_fromKDE.getRight().contains("<script>top.self.location.href='")) {
                var uRLAndQueryString = StringParser.extractURLAndQueryString(rsp_fromKDE.getRight());
                var keyinfo = Networking.extractRSAPublicKeyExponentAndRSAPublicKeyModulus(uRLAndQueryString);
                var mac = StringParser.getMacFromQueryString(uRLAndQueryString.getRight());
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter userid: ");
                String userid = scanner.nextLine();
                System.out.print("Enter password: ");
                var pwd = scanner.nextLine();
                String encryped = "";
                try {
                    encryped = Encrypting.ZIT_Network_Encrypt(keyinfo, pwd, mac);
                } catch (Throwable ignored) {
                    ;
                }
                var res = Networking.PostLoginRequest(userid, encryped, uRLAndQueryString);
                System.out.println(res.getLeft() + "\n" + res.getRight());
            } else {
                //this part tests encrypting, if network is on
                var keyinfo = Pair.of("10001", "94dd2a8675fb779e6b9f7103698634cd400f27a154afa67af6166a43fc26417222a79506d34cacc7641946abda1785b7acf9910ad6a0978c91ec84d40b71d2891379af19ffb333e7517e390bd26ac312fe940c340466b4a5d4af1d65c3b5944078f96a1a51a5a53e4bc302818b7c9f63c4a1b07bd7d874cef1c3d4b2f5eb7871");
                try {
                    var en = Encrypting.ZIT_Network_Encrypt(keyinfo, "1145141919810", "65535");
                    System.out.println(en);

                } catch (Throwable ignored) {

                }
            }
        }
    }
}