package de.hsbo.kommonitor.datamanagement.encrypt;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptorAesCBC {

	private static final String ENCRYPT_ALGO = "AES/CBC/PKCS5Padding";

	@Value("${encryption.symmetric.aes.iv.length_byte:16}")
	private int IV_LENGTH_BYTE;
	@Value("${encryption.symmetric.aes.key.length_byte:16}")
	private int KEY_LENGTH_BYTE;
	private final Charset UTF_8 = StandardCharsets.UTF_8;

	// return a base64 encoded AES encrypted text
	public String encrypt(byte[] pText, String password) throws Exception {

		// GCM recommended 12 bytes iv?
		byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

		// secret key from password
		SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyHashFromPassword(password, KEY_LENGTH_BYTE);

		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

		// ASE-GCM needs GCMParameterSpec
		cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, ivParameterSpec);

		byte[] cipherText = cipher.doFinal(pText);

		// prefix IV and Salt to cipher text
		byte[] cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length).put(iv).put(cipherText).array();

		// string representation, base64, send this string to other for decryption.
		return Base64.getEncoder().encodeToString(cipherTextWithIv);

	}

	// we need the same password, salt and iv to decrypt it
	public String decrypt(String cText, String password) throws Exception {

		byte[] decode = Base64.getDecoder().decode(cText.getBytes(UTF_8));

		// Extract IV.
		byte[] iv = new byte[IV_LENGTH_BYTE];
		System.arraycopy(decode, 0, iv, 0, iv.length);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

		// Extract encrypted part.
		int encryptedSize = decode.length - IV_LENGTH_BYTE;
		byte[] encryptedBytes = new byte[encryptedSize];
		System.arraycopy(decode, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedSize);

		// Hash key.
		SecretKey secretKeySpec = CryptoUtils.getAESKeyHashFromPassword(password, KEY_LENGTH_BYTE);

		// Decrypt.
		Cipher cipherDecrypt = Cipher.getInstance(ENCRYPT_ALGO);
		cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

		return new String(decrypted, UTF_8);

	}
}
