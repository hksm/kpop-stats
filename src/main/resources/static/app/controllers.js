app.controller('artistController', ['dataFactory', 'fileUpload', '$scope', 'focus', 
                                    function(dataFactory, fileUpload, $scope, focus) {
	var vm = this;
	
	var getArtists = function() {
		dataFactory.getArtists()
			.then(function(response) {
				vm.artistList = response.data;
			}, function(response) {
				vm.message = response.statusText;
			});
	};
	
	var clearForm = function() {
		vm.artist = {};
	};
	
	var loadArtist = function(id) {
		dataFactory.getArtist(id)
			.then(function(response) {
				vm.artist = response.data;
			});
	};
	
	vm.addArtist = function() {
		vm.isProcessing = true;
		dataFactory.saveArtist(vm.artist)
			.then(function(response) {
				vm.message = response.data;
				getArtists();
				clearForm();
				vm.isProcessing = false;
			}, function(response) {
				vm.message = response.statusText;
				vm.isProcessing = false;
			});
	};
	
	vm.editArtist = function(id) {
		loadArtist(id);
		focus('focusMe');
	}
	
	vm.deleteArtist = function(id) {
		dataFactory.deleteArtist(id)
			.then(function(response) {
				vm.message = response.data;
				getArtists();
			}, function(response) {
				vm.message = response.statusText;
			});
	}
	
	vm.uploadArtistPic = function(file) {
		fileUpload.uploadFileToUrl(file, "ARTIST", vm.artist.id)
			.then(function(response) {
				vm.message = response.data;
				getArtists();
			}, function(response) {
				vm.message = response.statusText;
			});
	}
	
	getArtists();
}]);

app.controller('trackController', ['dataFactory', function(dataFactory) {
	var vm = this;
	
	var getArtists = function() {
		dataFactory.getArtists()
			.then(function(response) {
				vm.artistList = response.data;
			}, function(response) {
				vm.message = response.statusText;
			});
	};
	
	var getTracks = function() {
		dataFactory.getTracks()
			.then(function(response) {
				vm.trackList = response.data;
			}, function(response) {
				vm.message = response.statusText;
			});
	};
	
	vm.addTrack = function() {
		vm.isProcessing = true;
		return dataFactory.saveTrack(vm.track)
			.then(function(response) {
				vm.message = response.data;
				getTracks();
				vm.isProcessing = false;
			}, function(response) {
				vm.message = response.statusText;
				vm.isProcessing = false;
			});
	};
	
	getArtists();
	getTracks();
}]);

app.controller('downloadController', ['dataFactory', '$q', function(dataFactory, $q) {
	var vm = this;
	
	vm.weeks = null;
	
	vm.bar = {
			max: 0,
			value: 0
		};
	
	var getWeeks = function() {
		var deferred = $q.defer();
		
		if (vm.weeks !== null) {
			deferred.resolve(vm.weeks);
		} else {
			dataFactory.getMissingWeeks()
				.then(function(response) {
					vm.weeks = response.data;
					deferred.resolve(response.data);
				}, function(response) {
					deferred.reject(response);
				});
		}
		return deferred.promise;	
	}
	
	vm.getWeeksFn = function() {
		return getWeeks();
	}
	
	var getDownloads = function() {
		dataFactory.getDownloads()
			.then(function(response) {
				vm.downloadList = response.data;
			}, function(response) {
				vm.message = response.statusText;
			});
	};
	
	vm.updateWeeks = function() {
		vm.updateWeeksPromisse = dataFactory.saveWeeksList();
	}
	
	vm.addDownloads = function() {
		return dataFactory.saveDownload(vm.week)
			.then(function(response) {
				vm.message = response.data;
			}, function(response) {
				vm.message = response.statusText;
			});
	}
	
	var multiDownloads = function() {
		var arr = vm.weeks.slice(0, vm.weeks.length > 10 ? 10 : vm.weeks.length);
		vm.bar.max = arr.length;
		vm.bar.value = 0;
		vm.bar.failValue = 0;
		arr.forEach(function(week, i) {
			dataFactory.saveDownload(week)
				.then(function(response) {
					if (response.data === "Downloads added successfully") {
						vm.bar.value++;
					} else if (response.data === "Error while adding downloads") {
						vm.bar.failValue++;
					}
				});
		});
	}
	
	vm.addMultiDownloads = function() {
		if (vm.weeks === null) {
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
	var vm = this;
	
	vm.artist = {};
	vm.mainImageLink = null;
	
	function getArtist(id) {
		dataFactory.getArtist(id)
			.then(function(response) {
				vm.artist = response.data
			});
	}
	
	function getMainImage(id) {
		dataFactory.getMainImageLink('artist', id)
			.then(function(response) {
				vm.mainImageLink = response.data;
			});
	}
	
	vm.updateMainPic = function() {
		dataFactory.saveMainImageLink('artist', $routeParams.artistId)
			.then(function(response) {
				vm.message = response.data;
				getMainImage($routeParams.artistId);
			}, function(response) {
				vm.message = response.statusText;
			});
	}
	
	getArtist($routeParams.artistId);
	getMainImage($routeParams.artistId);
}]);