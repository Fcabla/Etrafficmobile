
var mymap = null;
var incidents = [];
var markerGroup = null;
var userMarker = null;
var incidentIDMap = new Map();
var incidentsNotified = [];
var defaultIconSize = [40,40];


function loadMap(){
	var osmLayer = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
		maxZoom: 18,
		attribution: '© <a href="http://openstreetmap.org/copyright">OpenStreetMap</a>'
	});
	mymap = L.map('mapid').setView([40.41, -3.70], 7).addLayer(osmLayer);
}

function focusView(){
	console.log("flying");
	if(userMarker == null){
		mymap.flyTo([40.41, -3.70], 7);
	}else{
		mymap.flyTo(userMarker.getLatLng(), 12);
	}
	
}

function loadIncidents(data, textStatus, jqXHR){
	console.log("Status: " + textStatus);
	incidents = data;
	markerGroup = L.layerGroup().addTo(mymap);
	for (inc in incidents){
		//for (eve in incidents[inc]["events"]){
		let event = incidents[inc]["events"][0];	//Load just the first situation record
		let location = event["locationEvent"];
		
		if(location["type"] == ("Point")){
			let locationDetails = location["point"]["point"];
			let marker = L.marker([locationDetails["latitude"], locationDetails["longitude"]],{icon: L.icon({
				iconUrl: getImagePath(event["impact"], event["typeEvent"], true),
				iconSize: defaultIconSize // size of the icon	
			})});
			marker.bindPopup("<img src=\"" + getImagePath(event["impact"], event["typeEvent"], false) + "\" height=\"30px\" width=\"30px\"/>" + event["description"]);
			marker.addTo(markerGroup); 
			incidentIDMap.set(marker._leaflet_id, incidents[inc]["incidentId"]);
			
		}else if(location["type"] == ("Linear")){
			let locationDetailsP = location["segment"]["primaryPoint"];
			//let locationDetailsS = location["segment"]["secondaryPoint"];
			let marker = L.marker([locationDetailsP["latitude"], locationDetailsP["longitude"]],{icon: L.icon({
				iconUrl: getImagePath(event["impact"], event["typeEvent"], true),
				iconSize: defaultIconSize // size of the icon	
			})});
			marker.bindPopup("<img src=\"" + getImagePath(event["impact"], event["typeEvent"], false) + "\" height=\"40px\" width=\"40px\"/>" + event["description"]);
			marker.addTo(markerGroup); 
			incidentIDMap.set(marker._leaflet_id, incidents[inc]["incidentId"]);
		}
	}
}

function getLocation() {
  	if (navigator.geolocation) {
    	navigator.geolocation.getCurrentPosition(console.log);
		console.log("")
  	} else {
    	console.log("not supported")
  	}
}

function onLocationFound(e) {
	//var radius = e.accuracy / 2;

	if(userMarker == null){
		userMarker = L.marker(e.latlng).addTo(mymap);
	}else{
	   	userMarker.setLatLng(e.latlng);
	}
	
	decideNotification();

}

function decideNotification(){
	if(markerGroup != null && incidents.length > 0){
		//GETTING THE NEAREST
		var nearestPoint = L.GeometryUtil.closestLayer(mymap, markerGroup.getLayers(), userMarker.getLatLng());
		var incID = incidentIDMap.get(nearestPoint.layer._leaflet_id);
		
		if(nearestPoint.distance < 500){
			if(!incidentsNotified.includes(incID)){
				sendPushNotification(incID,nearestPoint.distance);
				incidentsNotified.push(incID);
			}
		}
	}
}		

function getImagePath(impact, incidentType, marker){
	var imagePath = "";
	if(marker){
		imagePath = "/EtrafficMobile/images/"+impact+"/";
	}else{
		imagePath = "/EtrafficMobile/images/imagesB/";
	}
	
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
	
function reloadIncidents(){
	console.log("reloading incidents");
	incidents = [];
	mymap.removeLayer(markerGroup);
	markerGroup = null;
	incidentIDMap.clear();
	$.getJSON('TrafficIncidentsServlet',loadIncidents)
}

function init(){
	loadMap();
	$.getJSON('TrafficIncidentsServlet',loadIncidents)
	mymap.on('locationfound', onLocationFound);
	mymap.locate({setView: true, watch: true, maxZoom: 8, enableHighAccuracy: true});
	setInterval(reloadIncidents, 300000);
}

init();

//https://leafletjs.com/examples/mobile/
//https://maptimeboston.github.io/leaflet-intro/