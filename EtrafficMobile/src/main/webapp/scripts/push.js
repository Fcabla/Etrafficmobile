
const publicKey = new Uint8Array([0x04, 0xfe, 0x45, 0x0c, 0x30, 0xec, 0x0a, 0x34, 0x14, 0xcc, 0x7a, 0xd2, 0xbf, 0xe3, 0x16, 0xa1, 0x29, 0xb2, 0x04, 0xeb, 0xd8, 0xad, 0x40, 0x49, 0xfb, 0xa1, 0x11, 0xe8, 0x3d, 0x80, 0x0d, 0x34, 0x54, 0x34, 0x7b, 0x91, 0x53, 0x7c, 0x54, 0xd4, 0xcc, 0xa9, 0x0a, 0xb4, 0x36, 0x5f, 0xb3, 0xa7, 0xa8, 0xe0, 0x58, 0x4b, 0x95, 0xb1, 0x19, 0xd9, 0x07, 0x0f, 0xb8, 0x41, 0x6b, 0x8a, 0x4d, 0x46, 0xb4]);
var jsonData;
//navigator.serviceWorker.getRegistrations().then(function(registrations) { for(let registration of registrations) { registration.unregister() } })

function urlBase64ToUint8Array(base64String) {
    var padding = '='.repeat((4 - base64String.length % 4) % 4);
    var base64 = (base64String + padding)
        .replace(/\-/g, '+')
        .replace(/_/g, '/');

    var rawData = window.atob(base64);
    var outputArray = new Uint8Array(rawData.length);

    for (var i = 0; i < rawData.length; ++i) {
        outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
}

function registerServiceWorker() {
  	return navigator.serviceWorker.register('/EtrafficMobile/scripts/service-worker.js')
  	.then(function(registration) {
	    console.log('Service worker successfully registered.');
		askPermission();
	    return registration;
  	})
  	.catch(function(err) {
    	console.error('Unable to register service worker.', err);
  	});
}

function askPermission() {
  	return new Promise(function(resolve, reject) {
    	const permissionResult = Notification.requestPermission(function(result) {
      		resolve(result);
		});

    	if (permissionResult) {
      		permissionResult.then(resolve, reject);
    	}
  	})	
  	.then(function(permissionResult) {
    	if (permissionResult !== 'granted') {
      		throw new Error('We weren\'t granted permission.');
    	}
		subscribeUserToPush();
  	});
}

function subscribeUserToPush() {
  	return navigator.serviceWorker.register('/EtrafficMobile/scripts/service-worker.js')
  	.then(function(registration) {
    	const subscribeOptions = {
      		userVisibleOnly: true,
      		//applicationServerKey: urlBase64ToUint8Array('')
			applicationServerKey: publicKey
    	};
    	return registration.pushManager.subscribe(subscribeOptions);
  	})
  	.then(function(pushSubscription) {
    	console.log('Received PushSubscription: ', JSON.stringify(pushSubscription));
		sendSubscriptionToBackEnd(pushSubscription)
    	return pushSubscription;
  	});
}

function sendSubscriptionToBackEnd(subscription) {
	
	var key = subscription.getKey ? subscription.getKey('p256dh') : '';
	
    var auth = subscription.getKey ? subscription.getKey('auth') : '';
	
	jsonData = {
		key : key ? btoa(String.fromCharCode.apply(null, new Uint8Array(key))) : '',
		auth : auth ? btoa(String.fromCharCode.apply(null, new Uint8Array(auth))) : '',
		endpoint : subscription.endpoint
	};
	
	$.post('PushSubscriptionServlet', JSON.stringify(jsonData) , function(data, status, xhr) {   
                console.log('status: ' + status);
            },'json');
}

function sendPushNotification(incID, dist){
	dist = Number((dist).toFixed(1));
	var notificationData = {
		endpoint : jsonData["endpoint"],
		incidentID : incID,
		distance : dist.toString()
	};
	$.post('PushNotificationServlet', JSON.stringify(notificationData) , function(data, status, xhr) {   
                console.log('status: ' + status);
            },'json');
}

if (!('serviceWorker' in navigator) || !('PushManager' in window)) {
	console.log("Service Worker isn't supported on this browser, disable or hide UI or Push isn't supported on this browser, disable or hide UI.");
}else{
	
	registerServiceWorker();
	//askPermission();
	//var subscription = subscribeUserToPush();
	//console.log(subscription);
	//sendSubscriptionToBackEnd({"PushSubscription": JSON.stringify(subscription)})
	//const subscriptionObjectToo = JSON.stringify(pushSubscription);
	
}

/* */
