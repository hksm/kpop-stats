app.factory('dataFactory', ['$http', function($http) {
	var dataFactory = {};
	
	dataFactory.getArtists = function() {
		return $http.get('/artists');
	};
	
	dataFactory.getArtist = function(id) {
		return $http.get('/artists/' + id);
	};
	
	dataFactory.saveArtist = function(artist) {
		return $http.post('/artists', artist);
	};
	
	dataFactory.deleteArtist = function(id) {
		return $http.delete('/artists/' + id);
	};
	
	dataFactory.getTracks = function() {
		return $http.get('/tracks');
	};
	
	dataFactory.saveTrack = function(track) {
		return $http.post('/tracks', track);
	};
	
	dataFactory.getWeeksList = function() {
		return $http.get('/weeklist');
	};
	
	dataFactory.saveWeeksList = function() {
		return $http.post('/weeklist');
	};
	
	dataFactory.getDownloads = function() {
		return $http.get('/downloads');
	};
	
	dataFactory.saveDownload = function(week) {
		return $http.post('/downloads', week);
	};
	
	dataFactory.logArtistImage = function(artist) {
		return $http.post('/upload/artist', artist, fileName);
	};
	
	return dataFactory;
}]);

app.factory('focus', ['$rootScope', '$timeout', function($rootScope, $timeout) {
	return function(name) {
		$timeout(function() {
			$rootScope.$broadcast('focusOn', name);
		});
	}
}]);

app.service('fileUpload', ['$http', function($http) {
	this.uploadFileToUrl = function(file, name, category, id) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('name', name);
        fd.append('category', category);
        fd.append('id', id);

        $http.post('/upload', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    }
}]);

app.service('lastfmFactory', ['$http', function($http) {
	var lastfmFactory = {};
	
	lastfmFactory.getArtistImage = function(artist) {
		artistName = null;
		
		if(artist.alias === null) {
			artistName = artist.name;
		} else {
			if(artist.alias.indexOf('/') == -1) {
				artistName = artist.alias;
			} else {
				artistName = artist.alias.substring(0, artist.alias.indexOf('/')).trim();
			}
		}

		return $http.get("http://ws.audioscrobbler.com/2.0/?method=artist.getInfo" + 
				"&artist=" + artistName + "&format=json&api_key=6384206c3554160836ad1868f816f7a0");
	};
	
	return lastfmFactory;
}]);