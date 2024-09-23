package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;


import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

public interface Encrypting {
    static public String ZIT_Network_Encrypt(Pair<String, String> publicKeyInfo, String plainText) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String reversed = new StringBuilder(plainText).reverse().toString();
//        System.out.println(reversed);
        BigInteger modulusBigInt = new BigInteger(publicKeyInfo.getRight(), 16);
        BigInteger exponentBigInt = new BigInteger(publicKeyInfo.getLeft());


        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);


        byte[] originalBytes = reversed.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(originalBytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : encryptedBytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }


    public static String ZIT_Network_Encrypt2(Pair<String, String> publicKeyInfo, String s) throws Exception {
        String reversed = new StringBuilder(s).reverse().toString();
        RSAKey rsaKey=new RSAKey(publicKeyInfo.getLeft(),publicKeyInfo.getRight());
        return rsaKey.encryptedString(reversed);
    }


    public static String ZIT_Network_Encrypt3(Pair<String, String> publicKeyInfo, String s) throws Exception {
//        return encryptedPassword2(publicKeyInfo.getLeft(), publicKeyInfo.getRight(), s);
        return ZIT_Network_Encrypt2(publicKeyInfo,s);
    }




    /***
     *
     * @author <a href="https://copilot.microsoft.com/">new bing</a>
     * well.. it doesn't generate correct encrypted pass
     *
     *
     */

    public static String encryptedPassword(String publicKeyExponent, String publicKeyModulus, String password) throws Exception {

        String passwordEncode = new StringBuilder(password).reverse().toString();


        byte[] bytes = passwordEncode.getBytes(StandardCharsets.UTF_8);


        BigInteger modulusBigInt = new BigInteger(publicKeyModulus, 16);
        BigInteger exponentBigInt = new BigInteger(publicKeyExponent, 10);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);


        int chunkSize = 2 * (modulusBigInt.bitLength() / 16 - 1);


        int paddingLength = chunkSize - (bytes.length % chunkSize);
        byte[] paddedBytes = new byte[bytes.length + paddingLength];
        System.arraycopy(bytes, 0, paddedBytes, 0, bytes.length);


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

