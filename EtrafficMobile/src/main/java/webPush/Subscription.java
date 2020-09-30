package webPush;


import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

public class Subscription {
	private String author;
	private String key;
	private String endpoint;
	
	/**
	 * Subscription constructor
	 * @param author auth key
	 * @param key p256dh key
	 * @param endpoint URL to user
	 */
	public Subscription(String author, String key, String endpoint) {
		this.author = author;
		this.key = key;
		this.endpoint = endpoint;
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

    /**
     * Returns the base64 encoded auth string as a byte[]
     */
    public byte[] getAuthAsBytes() {
        return Base64.getDecoder().decode(getAuthor());
    }
    /**
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * 
     * @return
     */
    public String getKey() {
        return key;
    }
    /**
     * 
     * @param endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    /**
     * 
     * @return
     */
    public String getEndpoint() {
    	return endpoint;
    }
    /**
     * Returns the base64 encoded public key string as a byte[]
     */
    public byte[] getKeyAsBytes() {
    	System.out.println(getKey());
        return Base64.getDecoder().decode(getKey());
    }

    /**
     * Returns the base64 encoded public key as a PublicKey object
     */
    public PublicKey getUserPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        ECPoint point = ecSpec.getCurve().decodePoint(getKeyAsBytes());
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
        
        return kf.generatePublic(pubSpec);
    }
    
}
