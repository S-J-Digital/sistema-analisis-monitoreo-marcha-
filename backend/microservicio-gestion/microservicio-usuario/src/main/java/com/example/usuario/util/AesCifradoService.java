package com.example.usuario.util;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Bean de cifrado/descifrado AES para el intercambio de información entre la app
 * móvil y el backend. Usa la misma clave y el mismo esquema (SHA-256 sobre la clave
 * como derivación, Cipher "AES", Base64) que {@link DescrifradoBase64Android}, para
 * que lo que esta clase cifre pueda descifrarse en el móvil y viceversa.
 */
@Service
public class AesCifradoService {

    private static final String CLAVE = "08wR?!S6_wo&-v$f#0RUdrEfRoclTh";

    public String encriptar(String texto) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, obtenerClaveSecreta());
            byte[] cifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cifrado);
        } catch (Exception e) {
            throw new RuntimeException("Error en el cifrado", e);
        }
    }

    public String desencriptar(String textoBase64) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, obtenerClaveSecreta());
            byte[] datos = Base64.getDecoder().decode(textoBase64);
            byte[] descifrado = cipher.doFinal(datos);
            return new String(descifrado, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error en el descifrado", e);
        }
    }

    private SecretKey obtenerClaveSecreta() throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] claveBytes = sha.digest(CLAVE.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(claveBytes, "AES");
    }
}
