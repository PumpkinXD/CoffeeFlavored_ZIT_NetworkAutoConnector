package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import org.apache.commons.lang3.tuple.Pair;

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

public interface Encrypting {
    static public String ZIT_Network_Encrypt(Pair<String,String> publicKeyInfo, String plainText) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String reversed = new StringBuilder(plainText).reverse().toString();
        BigInteger modulusBigInt = new BigInteger(publicKeyInfo.getRight(), 16);
        BigInteger exponentBigInt = new BigInteger(publicKeyInfo.getLeft(), 10);


        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);


        byte[] originalBytes = reversed.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(originalBytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : encryptedBytes) {
            hexString.append(String.format("%02X", b));
        }



        return hexString.toString();
    }
}
