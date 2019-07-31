var civicApiKey = "AIzaSyC1H-on_SD36xwzTnxRwhXQcUc9aETBjlM";
var geocodeApiKey = "AIzaSyCBZRt1KkYoI3_4aStVulpcxi2ys4mCoFs";
var geocoder;
var map;

// Expect JSON data (not XML)
$.ajaxSetup({ 
	beforeSend: function(xhr) {
		if (xhr.overrideMimeType) {
		  xhr.overrideMimeType("application/json");
		}
	}
});
// Populate state drop-down list
$.getJSON('data/state-options.json', function(data) {
	var stateList = $("#representatives_state_list");
	// Iterate through state to create options.
	$.each(data.states, function(i, state) {
		stateList.append($("<option />").val(state.ocdId).text(state.name));
	});
});

// On button click to find representatives by state.
$("#representatives_submit").on("click", function(e) {
    var encodedId = encodeURIComponent($("#representatives_state_list").val());
    var url = `https://www.googleapis.com/civicinfo/v2/representatives/${encodedId}?key=${civicApiKey}`;
    console.log(url);

    $.getJSON(url, loadRepresentatives);
});

$("#map_button").on("click", function(e) {
	e.preventDefault();
	markUserLocation();
});

// Load Google map centered at the provided location, mark said location.
function addMapMarker(centerPosition) {
    var marker = new google.maps.Marker({
        position: centerPosition,
        map: map,
        title: 'The map'
    });
}

// Get the user's geolocation and load the Google map.
function markUserLocation() {
	if (!map) {
		map = new google.maps.Map(document.getElementById('map'), {
		    zoom: 4,
		    center: { lat: 39.8333, lng: -98.585 }
		});
	}

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var marker = { lat: position.coords.latitude, lng: position.coords.longitude };
            addMapMarker(marker);
        }, function(positionError) {
        	alert(`Unable to mark position.\n\nError:\n\t${positionError.message}`);
        	// Arbitrary location if the user chooses not to provide geolocation.
	        var marker = { lat: 40.731, lng: -73.935 };
	        addMapMarker(marker);
        });
    }
}

function markAddress(tag, addressString) {
	console.log(`mapping: [tag: ${tag}, address: ${addressString}]`);

	if (!geocoder) {
		geocoder = new google.maps.Geocoder();
	}
	geocoder.geocode( { 'address': addressString}, function(results, status) {
		if (status == 'OK') {
			map.setCenter(results[0].geometry.location);
			var marker = new google.maps.Marker({
			    map: map,
			    position: results[0].geometry.location,
			    label: tag
			});
			document.getElementById("mapsection").scrollIntoView();
		} else {
			alert('Geocode was not successful for the following reason: ' + status);
		}
	});
}

// Get the JSON results of the elected officials for a given state,
// filter/organize the information for cards.
function loadRepresentatives(results) {
	// Clear out any existing profiles
	$("#officials").html("");

	var persons = [];
	for (var i = 0; i < results.offices.length; ++i) {
		results.offices[i].officialIndices.forEach(function(entry) {
			var person = new Object;
			person.title = results.offices[i].name;
			person.name = results.officials[entry].name;
			person.party = results.officials[entry].party;
			if (results.officials[entry].address && results.officials[entry].address.length > 0) {
				var a = results.officials[entry].address[0];
				person.address = `${a.line1} ${a.city}, ${a.state} ${a.zip}`;
			}
			if (results.officials[entry].photoUrl) {
				person.img = results.officials[entry].photoUrl;
			} else {
				person.img = 'images/no-photo.png';
			}

			persons.push(person);
			drawProfile(person);
		});
	}
	persons.forEach(function(person) {
		console.log(person);
	});
}

function drawProfile(profile) {
	$('#officials').append(`<div class="rep-card col-xs-6" onclick="markAddress('${profile.name}', '${profile.address}')"><div class="rep-card-image" style="background-image: url(${profile.img})"></div><div class="rep-card-container"><h4><b>${profile.name}</b></h4><p>${profile.title}</p><p style="font-size:13px">${profile.party}</p></div><span class="tooltiptext">Click to see their office location!</span></div>`);
}

function addressToGeocode(addressString) {

}