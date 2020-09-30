function getImagePath(incidentType){
	var imagePath = "/EtrafficMobile/images/imagesB/";
	switch (incidentType) {
		case "ConstructionWorks":
		case "NetworkManagement":
		case "MaintenanceWorks":
			imagePath += "operatorWorks.png";
			break; 
		case "VehicleObstruction":
			imagePath += "vehicleObstruction.png";
			break;
		case "AbnormalTraffic":
			imagePath += "vehicleObstruction.png";
			break;
		case "AnimalPresenceObstruction":
			imagePath += "animal.png";
			break;
		case "GeneralObstruction":
			imagePath += "generalObstruction.png";
			break;
		case "Activities":
			imagePath += "activities.png";
			break;
		case "InfrastructureDamageObstruction":
		case "PoorEnvironmentConditions":
			imagePath += "poorEnvirnmentConditions.png";
			break;
		case "EnvironmentalObstruction":
		case "WeatherRelatedRoadConditions":
			imagePath += "environmentalObstruction.png";
			break;
		case "Accident":
			imagePath += "accident.png";
			break;
		default:
			console.log(incidentType)
			//imagePath = "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png";
			imagePath += "example.png"
			break;
	}
	return imagePath;
}

self.addEventListener('push', function(event) {
  console.log('Received push');
  let notificationTitle = 'Hello';
  const notificationOptions = {
    body: 'Traffic incident body',
    icon: './images/logo-192x192.png',
    badge: './images/badge-72x72.png',
    tag: 'traffic incident push notification',
    data: {
      url: 'http://127.0.0.1:8080/EtrafficMobile/index.xhtml',
    },
  };

  if (event.data) {
	var eventData = JSON.parse(event.data.text());
	//console.log(eventData);
	//console.log(getImagePath(eventData.inType));

    notificationTitle = eventData.title;
    notificationOptions.body = eventData.body;
	notificationOptions.icon = getImagePath(eventData.inType);
  }

  event.waitUntil(
    Promise.all([
      self.registration.showNotification(
        notificationTitle, notificationOptions),
    ])
  );
});

self.addEventListener('notificationclick', function(event) {
  event.notification.close();

  let clickResponsePromise = Promise.resolve();
  if (event.notification.data && event.notification.data.url) {
    clickResponsePromise = clients.openWindow(event.notification.data.url);
  }

  event.waitUntil(
    Promise.all([
      clickResponsePromise,
    ])
  );
});
