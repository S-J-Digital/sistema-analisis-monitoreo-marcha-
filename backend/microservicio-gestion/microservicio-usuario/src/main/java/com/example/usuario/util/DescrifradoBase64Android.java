package com.example.usuario.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DescrifradoBase64Android {

    private static final String key ="08wR?!S6_wo&-v$f#0RUdrEfRoclTh";

    public static String getdesencriptacion(String passwordBase64) {
        String desencrip = null;
        try {
            SecretKey key = getSecretKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] datosDesencriptadosBase64 = Base64.getDecoder().decode(passwordBase64);
            byte[] datosDesencriptadosByte = cipher.doFinal(datosDesencriptadosBase64);
            desencrip = new String(datosDesencriptadosByte, "UTF-8");

        } catch (Exception e) {
            // Manejo de excepciones más genérico para simplificar
            throw new RuntimeException("Error en el descifrado", e);
        }
        return desencrip;
    }

    private static SecretKey getSecretKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyByte = key.getBytes("UTF-8");
        keyByte = sha.digest(keyByte);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte,"AES");
        return secretKeySpec;
    }
}
