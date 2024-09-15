package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    static String eportal;
    static String querying_string;

    public static void main(String[] args) {

        var rsp_fromKDE = Networking.TryConnect2_networkcheck_kde_org();
        System.out.println(rsp_fromKDE.getLeft() + "\n" + rsp_fromKDE.getRight());
        ExtractURL_test();


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
