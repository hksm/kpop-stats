app.factory('dataFactory', ['$http', function($http) {
	var dataFactory = {};
	
	dataFactory.getArtists = function(page) {
		return $http.get('/artists' + (page ? ('?page=' + page + '&size=20') : ''));
	};
	
	dataFactory.getArtist = function(id) {
		return $http.get('/artists/' + id);
	};

	dataFactory.findByArtist = function(query) {
		return $http.get('/artists/findby?q=' + query);
	};
	
	dataFactory.saveArtist = function(artist) {
		return $http.post('/artists', artist);
	};
	
	dataFactory.deleteArtist = function(id) {
		return $http.delete('/artists/' + id);
	};
	
	dataFactory.getTracks = function(page) {
		return $http.get('/tracks' + (page ? ('?page=' + page + '&size=20') : ''));
	};
	
	dataFactory.saveTrack = function(track) {
		return $http.post('/tracks', track);
	};

	dataFactory.getGenres = function(page) {
		return $http.get('/genres' + (page ? ('?page=' + page + '&size=20') : ''));
	};
	
	dataFactory.saveGenre = function(genre) {
		return $http.post('/genres', genre);
	};
	
	dataFactory.deleteGenre = function(id) {
		return $http.delete('/genres/' + id);
	};
	
	dataFactory.getMissingWeeks = function() {
		return $http.get('/weeks/missing');
	};
	
	dataFactory.saveWeeksList = function() {
		return $http.post('/weeks/list');
	};
	
	dataFactory.getDownloads = function() {
		return $http.get('/downloads');
	};
	
	dataFactory.saveDownload = function(week) {
		return $http.post('/downloads', week);
	};
	
	dataFactory.getMainImageLink = function(category, especificId) {
		return $http.get('/images/' + category + '/' + especificId + '/last');
	};	
	
	dataFactory.saveMainImageLink = function(category, especificId) {
		var fd = new FormData();
        fd.append('category', category);
        fd.append('id', especificId);
		
        return $http.post('/images', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
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
	this.uploadFileToUrl = function(file, category, id) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('category', category);
        fd.append('id', id);

        $http.post('/upload', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    }
}]);