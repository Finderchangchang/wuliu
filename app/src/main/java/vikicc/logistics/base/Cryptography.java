package vikicc.logistics.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

/**
 * AES、MD5、密码
 */
public class Cryptography {

    private static String CHARSET = "UTF-8";
    private static String SALT = "Kiwi";

    public static String AES_Encode(String str, String key, String vector)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = str.getBytes(CHARSET);
        byte[] keyBytes = hashKey(key);
        byte[] ivBytes = hashVector(vector);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return new String(Base64.encode(cipher.doFinal(textBytes), Base64.DEFAULT));
        //return new BASE64Encoder().encode(cipher.doFinal(textBytes));
    }

    public static String AES_Decode(String str, String key, String vector)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, IOException {


        byte[] textBytes = Base64.decode(str, Base64.DEFAULT);
        byte[] keyBytes = hashKey(key);
        byte[] ivBytes = hashVector(vector);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), CHARSET);
    }

    public static String hashPassword(String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        password = password.toUpperCase().trim();
        for (int i = 0; i < 3; i++) {
            password = new String(md5(password + SALT)).substring(0, 20);
        }
        return password;
    }
    // 355921041219450

    private static byte[] java2csharp(String text) {
        byte[] bytes = new byte[text.length() / 2];
        for (int i = 0; i < text.length(); i += 2) {
            int n = char2hex(text.charAt(i)) * 16 + char2hex(text.charAt(i + 1));
            if (n > 127)
                n -= 256;
            bytes[i / 2] = (byte) n;
        }
        return bytes;
    }

    private static int char2hex(char c) {
        return (c >= 'A' && c <= 'Z') ? 10 + c - 'A' : c - '0';
    }

    public static byte[] hashKey(String key)
            throws UnsupportedEncodingException {
        String md5 = md5((key + SALT));//长度32
        return java2csharp(md5 + md5);

    }

    public static byte[] hashVector(String vector)
            throws UnsupportedEncodingException {
        return java2csharp(md5((vector + SALT)));
    }

    /*
        public static byte[] hashKey(String key)
                throws UnsupportedEncodingException {
            return md5((key + SALT).getBytes(CHARSET));
        }
    
        public static byte[] hashVector(String vector)
                throws UnsupportedEncodingException {
            byte[] buffer = md5((vector + SALT).getBytes(CHARSET));
            byte[] iv = new byte[16];
            System.arraycopy(buffer, 0, iv, 0, iv.length);
            return iv;
        }
    */
    public static String md5(String source) throws UnsupportedEncodingException {
        return new String(md5(source.getBytes(CHARSET)));
    }

    public static byte[] md5(byte[] source) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            byte str[] = new byte[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = (byte) hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = (byte) hexDigits[byte0 & 0xf];
            }
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
} 

