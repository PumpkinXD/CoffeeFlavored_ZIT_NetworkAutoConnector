package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

public interface Encrypting {


    public static String ZIT_Network_Encrypt(Pair<String, String> publicKeyInfo, String pwd, String mac) throws Exception {
        var salted = pwd + ">" + mac;
//        var salter = pwd + ">" + "random_salt";//still working, zit's server will ignore anything behind '>'
        return encryptedPassword(publicKeyInfo.getLeft(), publicKeyInfo.getRight(), pwd);
    }


    /***
     *
     * @author <a href="https://copilot.microsoft.com/">new bing</a> and <a href="https//github.com/PumpkinXD>PumpkinXD</a>
     * well... so <a href="https://www.ruijie.com.cn/">ruijie</a> and zit, ****....
     *
     *
     */

    public static String encryptedPassword(String publicKeyExponent, String publicKeyModulus, String password) throws Exception {
        String passwordEncode = new StringBuilder(password).reverse().toString();
        byte[] bytes = passwordEncode.getBytes(StandardCharsets.UTF_8);


        BigInteger modulusBigInt = new BigInteger(publicKeyModulus, 16);
        BigInteger exponentBigInt = new BigInteger(publicKeyExponent, 16);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);


        int chunkSize = 2 * (modulusBigInt.bitLength() / 16 - 1);


        int paddingLength = chunkSize - (bytes.length % chunkSize);
        byte[] paddedBytes = new byte[bytes.length + paddingLength];

        System.arraycopy(bytes, 0, paddedBytes, 0, bytes.length);
        ArrayUtils.reverse(paddedBytes);//just blame ruijie and security.js


        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);


        StringBuilder result = new StringBuilder();
        for (int i = 0; i < paddedBytes.length; i += chunkSize) {
            byte[] chunk = new byte[chunkSize];
            System.arraycopy(paddedBytes, i, chunk, 0, chunkSize);


            BigInteger block = new BigInteger(1, chunk);


            BigInteger encryptedBlock = block.modPow(exponentBigInt, modulusBigInt);


            result.append(encryptedBlock.toString(16)).append(" ");
        }

        return result.toString().trim();
    }


}

