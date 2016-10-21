app.controller('artistController', ['dataFactory', 'fileUpload', '$scope', '$uibModal',
                                    function(dataFactory, fileUpload, $scope, $uibModal) {
	var vm = this;
	
	vm.totalItems = 0;
	vm.pageNumber = 0;
	
	var getArtists = function(page) {
		dataFactory.getArtists(page)
			.then(function(response) {
				vm.artistList = response.data.content;
				vm.totalItems = response.data.totalElements;
				vm.pageNumber = response.data.number+1;
			}, function(response) {
				vm.message = response.statusText;
			});
	};
	
	vm.pageChanged = function() {
		getArtists(vm.pageNumber-1);
	};
	
	vm.remove = function(id) {
		dataFactory.deleteArtist(id)
			.then(function(response) {
				vm.message = response.data;
				getArtists();
			}, function(response) {
				vm.message = response.statusText;
			});
	}
	
	vm.openModal = function(artist) {
		var modalInstance = $uibModal.open({
			templateUrl: 'artist-modal.html',
			controller: 'artistModalController',
			controllerAs: 'vm',
			resolve: {
				resolved: artist || undefined
			}
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

app.controller('artistModalController', ['dataFactory', 'focus', '$uibModal', '$uibModalInstance', 'toastr', 'resolved',
                                         function(dataFactory, focus, $uibModal, $uibModalInstance, toastr, resolved) {
	var vm = this;
	
	vm.artist = resolved || {};
	
	var clearForm = function() {
		vm.artist = {};
	};
	
	var loadArtist = function(id) {
		dataFactory.getArtist(id)
			.then(function(response) {
				vm.artist = response.data;
			});
	};
	
	vm.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	vm.save = function() {
		vm.isProcessing = true;
		dataFactory.saveArtist(vm.artist)
			.then(function(response) {
				toastr.success(response.data);
				vm.isProcessing = false;
				vm.cancel();
			}, function(response) {
				toastr.error(response.statusText);
				vm.isProcessing = false;
			});
	};
}]);

app.controller('trackController', ['dataFactory', '$uibModal', 
                                   function(dataFactory, $uibModal) {
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

app.controller('downloadController', ['dataFactory', '$q', '$uibModal', 
                                      function(dataFactory, $q, $uibModal) {
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

app.controller('artistDetailsController', ['dataFactory', '$routeParams', 
                                           function(dataFactory, $routeParams) {
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