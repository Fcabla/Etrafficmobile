package old;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;



public class ServerKeys {
	static ECPublicKey publicKey;
	ECPrivateKey privateKey;
	String publicKeyPath;
	String privateKeyPath;
	private static byte[] P256_HEAD = Base64.getDecoder().decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE");

	public ServerKeys() {
		this.publicKeyPath = "D:\\Documentos\\Mis docs\\Estudios\\Grado Uv\\Universidad 4\\tfg\\eclipse-workspace\\EtrafficMobile\\src\\main\\java\\keys\\pub";
		this.privateKeyPath = "D:\\Documentos\\Mis docs\\Estudios\\Grado Uv\\Universidad 4\\tfg\\eclipse-workspace\\EtrafficMobile\\src\\main\\java\\keys\\pk";
		initKeys();
	}
	
	/**
	 * @return the publicKey
	 */
	public ECPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(ECPublicKey publicKey) {
		ServerKeys.publicKey = publicKey;
	}

	/**
	 * @return the privateKey
	 */
	public ECPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(ECPrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKeyBase64() {
		byte[] pubEncoded = publicKey.getEncoded();
        //String publicKeyBase64 = BaseEncoding.base64Url().encode(pubEncoded);
        //System.out.println(publicKeyBase64);
        return Base64.getEncoder().encodeToString(pubEncoded);

	}
	
	public String getPrivateKeyBase64() {
		byte[] pubEncoded = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(pubEncoded);
	}
		
	public void initKeys(){
		Path puKPath = Paths.get(publicKeyPath);
		Path prKPath = Paths.get(privateKeyPath);
		
		if (Files.exists(puKPath) && Files.exists(prKPath)) {
			try {
				byte[] encodedPublicKey = Files.readAllBytes(Paths.get(publicKeyPath));
				byte[] encodedPrivateKey = Files.readAllBytes(Paths.get(privateKeyPath));
				KeyFactory kf = KeyFactory.getInstance("EC"); // or "EC" or whatever
				ServerKeys.publicKey = (ECPublicKey) kf.generatePublic(new X509EncodedKeySpec(encodedPublicKey));;
				this.privateKey = (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encodedPrivateKey));
			} catch (IOException e  ) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}else {
			KeyPairGenerator keyPairGenerator = null;
			try {
				keyPairGenerator = KeyPairGenerator.getInstance("EC");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			KeyPair pair = keyPairGenerator.generateKeyPair();
			ServerKeys.publicKey = (ECPublicKey) pair.getPublic();
	        this.privateKey = (ECPrivateKey) pair.getPrivate();
	        try {
				Files.write(puKPath, ServerKeys.publicKey.getEncoded());
				Files.write(prKPath, this.privateKey.getEncoded());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static byte[] toUncompressedECPublicKey(ECPublicKey publicKey) {
	    byte[] result = new byte[65];
	    byte[] encoded = publicKey.getEncoded();
	    System.arraycopy(encoded, P256_HEAD.length, result, 0,
	        encoded.length - P256_HEAD.length);
	    /*
	    for(int i=0; i< result.length ; i++) {
	         System.out.print(result[i] +", ");
	      }
	      */
	    return result;
	}
	public static String getPublicKeyBase64Un() {
		return Base64.getUrlEncoder().withoutPadding()
        .encodeToString(toUncompressedECPublicKey(publicKey));
	}
	
	public static void main(final String[] args) throws MalformedURLException, IOException {
		//ServerKeys s = new ServerKeys();
		//System.out.println(s.getPublicKeyBase64());
		//System.out.println(s.getPrivateKeyBase64());
		//System.out.println(getPublicKeyBase64Un());
	}
}
