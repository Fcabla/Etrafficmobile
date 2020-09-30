package webPush;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.jose4j.lang.JoseException;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;

public class PushController {
	private static PushController pushControllerInstance = null; 
	private Map<String, Subscription> subscriptions;
	private static final String PK = "4ZG6qXBC67su8orKaLrfUcoW19csTYQVoUxBpzbaey8=";
	private static final String PUB = "BP5FDDDsCjQUzHrSv+MWoSmyBOvYrUBJ+6ER6D2ADTRUNHuRU3xU1MypCrQ2X7OnqOBYS5WxGdkHD7hBa4pNRrQ=";
	//private static final int TTL = 255;
	
	/**
	 * PushController constructor
	 */
	private PushController() {
		this.subscriptions = new HashMap<String, Subscription>();
	 }
	
	/**
	 * Function that returns the only instance of PushController
	 * @return PushController instance
	 */
	public static PushController getInstance(){ 
        if (pushControllerInstance == null) {
        	pushControllerInstance = new PushController(); 
        }
    	return pushControllerInstance; 
    }
	
	/**
	 * Function for register a subscription from an user
	 * @param sub new user subscription
	 */
	public void subscribeUser(Subscription sub) {
		this.subscriptions.put(sub.getEndpoint(), sub);
	}
	
	/**
	 * Function to get a user subscription 
	 * @param endpoint the user navigator endpoint
	 * @return
	 */
	public Subscription getUser(String endpoint) {
		return subscriptions.get(endpoint);
	}
	
	/**
	 * Function that sends a push message to a user
	 * @param sub user subscription
	 * @param payload the message
	 */
	public void sendPushMessage(Subscription sub, byte[] payload) {
		Notification notification = null;
		PushService pushService;
		
		// Create a notification with the endpoint, userPublicKey from the subscription and a custom payload
		try {
			notification = new Notification(
				sub.getEndpoint(),
				sub.getUserPublicKey(),
				sub.getAuthAsBytes(),
				payload
			);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		pushService = new PushService();
	
		try {
			pushService.setPublicKey(Utils.loadPublicKey(PUB));
			pushService.setPrivateKey(Utils.loadPrivateKey(PK));
		} catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		// Send the notification
		try {
			pushService.send(notification);
		} catch (GeneralSecurityException | IOException | JoseException | ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(final String[] args) throws MalformedURLException, IOException {
		/*
		Subscription sub = new Subscription(
				"m6ig9CDmiQv9Uqkey0ASJA==",
				"BKsbsDyDLmoHL1+5y3tpi14/puczVL5KO2RYvLjCnj9gbj2qaQ1SCIarVskQJdd8gSqYlYukCl5JvAbCQ7HsssQ=",
				"https://fcm.googleapis.com/fcm/send/d2zqoYbGvY8:APA91bH9i5EPKJSbz8RNLXz5CpRmnzom5Spyqjtiyn8PZB3Jz1k-flAVFM8rK3RGuLwVDKpKQgWw5SEFtr8K76u6vka9F3xu_BKWZvHslldXS-OMIoH3MSGncbNJAzNUfhYzjoSTz2fr"
		);
		
		PushController pc = PushController.getInstance();
		pc.subscribeUser(sub);
		String inputString = "Hello World!";
		byte[] payload = inputString.getBytes();
		pc.sendPushMessage(sub, payload);
		*/
	}
	
}
