package com.yzr.dida.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionService {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_BITS = 128;
    private static final int IV_BYTES = 12;

    private final SecretKey key;
    private final SecureRandom random = new SecureRandom();

    public EncryptionService(@Value("${security.encryption.key:}") String base64Key) {
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalStateException("Missing security.encryption.key (base64-encoded 16/24/32 bytes)");
        }
        byte[] raw = Base64.getDecoder().decode(base64Key);
        this.key = new SecretKeySpec(raw, "AES");
    }

    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_BYTES];
            random.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] ct = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(ct);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failure", e);
        }
    }

    public String decrypt(String payload) {
        try {
            String[] parts = payload.split(":", 2);
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] ct = Base64.getDecoder().decode(parts[1]);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] pt = cipher.doFinal(ct);
            return new String(pt);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failure", e);
        }
    }
}

