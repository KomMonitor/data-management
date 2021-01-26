package de.hsbo.kommonitor.datamanagement.encrypt;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@ConditionalOnProperty(value = "encryption.enabled", havingValue = "true", matchIfMissing = false)
public class JsonDataEncryptConverter extends AbstractHttpMessageConverter<Object> {

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EncryptorAesCBC aesEncryptor;
	
	@Value("${encryption.symmetric.aes.password}")
	private String password;

	public JsonDataEncryptConverter() {
		super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json", DEFAULT_CHARSET));
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
//		return objectMapper.readValue(decrypt(inputMessage.getBody()), clazz);
		return objectMapper.readValue(inputMessage.getBody(), clazz);
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		outputMessage.getBody().write(encrypt(objectMapper.writeValueAsBytes(o)));
	}

	/**
	 * requests params of any API
	 *
	 * @param inputStream inputStream
	 * @return inputStream
	 */
	private InputStream decrypt(InputStream inputStream) {
		// this is API request params
		StringBuilder requestParamString = new StringBuilder();
		try (Reader reader = new BufferedReader(
				new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c;
			while ((c = reader.read()) != -1) {
				requestParamString.append((char) c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// replacing /n if available in request param json string

			// reference request: {"data":"thisisencryptedstringwithexpirytime"}
			
			Map<String, String> hashMap = new HashMap<>();
			hashMap.put("data", requestParamString.toString().replace("\n", ""));
			JSONObject jsob = new JSONObject(hashMap);

			JSONObject requestJsonObject = new JSONObject(jsob);

			String decryptRequestString = aesEncryptor.decrypt((String)requestJsonObject.get("data"), password);
			System.out.println("decryptRequestString: " + decryptRequestString);

			if (decryptRequestString != null) {
				return new ByteArrayInputStream(decryptRequestString.getBytes(StandardCharsets.UTF_8));
			} else {
				return inputStream;
			}
		} catch (Exception err) {
			return inputStream;
		}
	}

	/**
	 * response of API
	 *
	 * @param bytesToEncrypt byte array of response
	 * @return byte array of response
	 */
	private byte[] encrypt(byte[] bytesToEncrypt) {
		// do your encryption here
		String apiJsonResponse = new String(bytesToEncrypt);

		String encryptedString = null;
		try {
			encryptedString = aesEncryptor.encrypt(bytesToEncrypt, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (encryptedString != null) {
			// sending encoded json response in data object as follows

			// reference response: {"data":"thisisencryptedstringresponse"}

			Map<String, String> hashMap = new HashMap<>();
			hashMap.put("encryptedData", encryptedString);
			JSONObject jsob = new JSONObject(hashMap);
			return jsob.toString().getBytes();
		} else
			return bytesToEncrypt;
	}
}
