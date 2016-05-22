app.controller('artistController', ['dataFactory', 'fileUpload', '$scope', 'focus', 
                                    function(dataFactory, fileUpload, $scope, focus) {
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
	
	self.bar = {
			max: 0,
			value: 0
		};
	
	var getWeeks = function() {
		var deferred = $q.defer();
		
		if (self.weeks !== null) {
			deferred.resolve(self.weeks);
		} else {
			dataFactory.getMissingWeeks()
				.then(function(response) {
					self.weeks = response.data;
					deferred.resolve(response.data);
				}, function(response) {
					deferred.reject(response);
				});
		}
		return deferred.promise;	
	}
	
	self.getWeeksFn = function() {
		return getWeeks();
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
	
	var multiDownloads = function() {
		var arr = self.weeks.slice(0, self.weeks.length >= 10 ? 9 : self.weeks.length);
		self.bar.max = arr.length;
		self.bar.value = 0;
		arr.forEach(function(week, i) {
			dataFactory.saveDownload(week)
				.then(function(response) {
					if (response.data === "Downloads added successfully") {
						self.bar.value++;
					}
				});
		});
	}
	
	self.addMultiDownloads = function() {
		if (self.weeks === null) {
			getWeeks().then(function(result) {
				multiDownloads();
			});
		} else {
			multiDownloads();
		}
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
	
	self.updateMainPic = function() {
		dataFactory.saveMainImageLink('artist', $routeParams.artistId)
			.then(function(response) {
				self.message = response.data;
				getMainImage($routeParams.artistId);
			}, function(response) {
				self.message = response.statusText;
			});
	}
	
	getArtist($routeParams.artistId);
	getMainImage($routeParams.artistId);
}]);