package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;


/***
 *
 * @see https://github.com/HuangZhiAn/SAPortals/blob/master/src/main/webapp/static/js/security.js or resources/security.js
 *
 *
 *
 */



import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RSAKey {
    private BigInteger e; // Encryption exponent
    private BigInteger m; // Modulus
    private int chunkSize;
    private int radix;
    private BigInteger mu;

    public RSAKey(String encryptionExponent, String modulus) {
        this.e = new BigInteger(encryptionExponent, 16);
        this.m = new BigInteger(modulus, 16);
        this.chunkSize = 2 * (m.bitLength() / 16 - 1);
        this.radix = 16;
        this.mu = BigInteger.ONE.shiftLeft(2 * m.bitLength()).divide(m);
    }

    public BigInteger getExponent() {
        return e;
    }

    public BigInteger getModulus() {
        return m;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getRadix() {
        return radix;
    }

    public BigInteger powMod(BigInteger base, BigInteger exp) {
        return base.modPow(exp, m);
    }

    public String encryptedString(String s) {
        byte[] a = s.getBytes(StandardCharsets.UTF_8);
        int sl = a.length;
        int i = 0;

        while (a.length % getChunkSize() != 0) {
            a = Arrays.copyOf(a, a.length + 1);
            a[i++] = 0;
        }

        int al = a.length;
        StringBuilder result = new StringBuilder();
        int j, k;
        BigInteger block;

        for (i = 0; i < al; i += getChunkSize()) {
            block = BigInteger.ZERO;
            j = 0;
            for (k = i; k < i + getChunkSize(); ++j) {
                block = block.add(BigInteger.valueOf(a[k++] & 0xFF));
                if (k < i + getChunkSize()) {
                    block = block.add(BigInteger.valueOf((a[k++] & 0xFF) << 8));
                }
            }
            BigInteger crypt = powMod(block, getExponent());
            String text = getRadix() == 16 ? crypt.toString(16) : crypt.toString(getRadix());
            result.append(text).append(" ");
        }
        return result.substring(0, result.length() - 1); // Remove last space.
    }
}
