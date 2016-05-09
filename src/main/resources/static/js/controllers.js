app.controller('artistController', ['dataFactory', 'fileUpload', 'lastfmFactory', '$scope', 'focus', 
                                    function(dataFactory, fileUpload, lastfmFactory, $scope, focus) {
	var self = this;
	
	var getArtists = function() {
		dataFactory.getArtists()
			.then(function(response) {
				self.artistList = response.data;
			}, function(response) {
				self.message = response.statusText;
			});
	};
	
	var clearForm = function() {
		self.artist = {};
	};
	
	var loadArtist = function(id) {
		dataFactory.getArtist(id)
			.then(function(response) {
				self.artist = response.data;
			});
	};
	
	self.addArtist = function() {
		self.isProcessing = true;
		dataFactory.saveArtist(self.artist)
			.then(function(response) {
				self.message = response.data;
				getArtists();
				clearForm();
				self.isProcessing = false;
			}, function(response) {
				self.message = response.statusText;
				self.isProcessing = false;
			});
	};
	
	self.editArtist = function(id) {
		loadArtist(id);
		focus('focusMe');
	}
	
	self.deleteArtist = function(id) {
		dataFactory.deleteArtist(id)
			.then(function(response) {
				self.message = response.data;
				getArtists();
			}, function(response) {
				self.message = response.statusText;
			});
	}
	
	self.uploadArtistPic = function(file) {
		fileUpload.uploadFileToUrl(file, "ARTIST", self.artist.id)
			.then(function(response) {
				self.message = response.data;
				getArtists();
			}, function(response) {
				self.message = response.statusText;
			});
	}
	
	self.uploadPicFromLastfm = function() {
		lastfmFactory.getArtistImage(self.artist)
			.then(function(response) {
				obj = response.data.artist.image.filter(function(element) {
					return element.size === "mega";
				});
				uploadArtistPic(obj[0]["#text"]);
			}, function(reponse) {
				console.log(response.statusText);
			});
	}
	
	getArtists();
}]);

app.controller('trackController', ['dataFactory', function(dataFactory) {
	var self = this;
	
	var getArtists = function() {
		dataFactory.getArtists()
			.then(function(response) {
				self.artistList = response.data;
			}, function(response) {
				self.message = response.statusText;
			});
	};
	
	var getTracks = function() {
		dataFactory.getTracks()
			.then(function(response) {
				self.trackList = response.data;
			}, function(response) {
				self.message = response.statusText;
			});
	};
	
	self.addTrack = function() {
		self.isProcessing = true;
		dataFactory.saveTrack(self.track)
			.then(function(response) {
				self.message = response.data;
				getTracks();
				self.isProcessing = false;
			}, function(response) {
				self.message = response.statusText;
				self.isProcessing = false;
			});
	};
	
	getArtists();
	getTracks();
}]);

app.controller('downloadController', ['dataFactory', '$q', function(dataFactory, $q) {
	var self = this;
	
	self.weeks = null;
	
	self.getWeeksFn = function() {
		return getWeeks();
	}
	
	function getWeeks() {
		var deferred = $q.defer();
		
		if (self.weeks !== null) {
			deferred.resolve(self.weeks);
		} else {
			dataFactory.getWeeksList()
				.then(function(response) {
					self.weeks = response.data;
					deferred.resolve(response.data);
				}, function(response) {
					deferred.reject(response);
				});
		}
		return deferred.promise;	
	}
	
	var getDownloads = function() {
		dataFactory.getDownloads()
			.then(function(response) {
				self.downloadList = response.data;
			}, function(response) {
				self.message = response.statusText;
			});
	};
	
	self.updateWeeks = function() {
		dataFactory.saveWeeksList();
	}
	
	self.addDownloads = function() {
		dataFactory.saveDownload(self.week)
			.then(function(response) {
				self.message = response.data;
			}, function(response) {
				self.message = response.statusText;
			});
	}
	
	getDownloads();
}]);

app.controller('artistDetailsController', ['dataFactory', '$routeParams', function(dataFactory, $routeParams) {
	var self = this;
	
	self.artist = {};
	self.mainImageLink = null;
	
	function getArtist(id) {
		dataFactory.getArtist(id)
			.then(function(response) {
				self.artist = response.data
			});
	}
	
	function getMainImage(id) {
		dataFactory.getMainImageLink('artist', id)
			.then(function(response) {
				self.mainImageLink = response.data;
			});
	}
	
	getArtist($routeParams.artistId);
	getMainImage($routeParams.artistId);
}]);