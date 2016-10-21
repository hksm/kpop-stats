var app = angular.module('kpop-stats', ['ngRoute', 'ngAnimate', 'oi.select', 'ui.bootstrap', 
                                        'bootstrap.fileField', 'toastr', 'angular-ladda']);

app.config(function($routeProvider, $httpProvider, laddaProvider) {
	$routeProvider
		.when('/', {
			templateUrl: 'home.html',
			controller: 'home',
			controllerAs: 'vm'
		})
		.when('/login', {
			templateUrl: 'login.html',
			controller: 'navigation',
			controllerAs: 'vm'
		})
		.when('/register', {
			templateUrl: 'register.html',
			controller: 'signupController',
			controllerAs: 'vm'
		})
		.when('/artist', {
			templateUrl: 'artist.html',
			controller: 'artistController',
			controllerAs: 'vm'
		})
		.when('/track', {
			templateUrl: 'track.html',
			controller: 'trackController',
			controllerAs: 'vm'
		})
		.when('/download', {
			templateUrl: 'download.html',
			controller: 'downloadController',
			controllerAs: 'vm'
		})
		.when('/artist/:artistId', {
			templateUrl: 'artist-details.html',
			controller: 'artistDetailsController',
			controllerAs: 'vm'
		})
		.otherwise('/');
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	
	laddaProvider.setOption({
		style: 'slide-left',
		spinnerSize: 25,
		spinnerColor: '#000000'
	});
});

app.controller('home', function($http) {
	var vm = this;
	
});

app.controller('navigation', function($rootScope, $http, $location) {
	var vm = this;
	
	var authenticate = function(credentials, callback) {
		var headers = credentials ? {authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)
		} : {};
		$http.get('user', {headers: headers}).then(function(response) {
			if (response.data.name) {
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
			callback && callback();
		}, function (response) {
			$rootScope.authenticated = false;
			callback && callback();
		});
	};
	
	vm.navbarCollapsed = false;
	
	authenticate();
	vm.credentials = {
		remember: true
	};
	vm.login = function() {
		authenticate(vm.credentials, function() {
			if ($rootScope.authenticated) {
				$location.path("/");
				vm.error = false;
			} else {
				$location.path("/login");
				vm.error = true;
			}
		});
	};
		
	vm.logout = function() {
		$http.post('logout', {}).finally(function() {
			$rootScope.authenticated = false;
			$location.path("/");
		});
	};
});

app.controller('signupController', function($http, $location) {
	var vm = this;
	
	vm.register = function() {
		$http.post('/signup', vm.user)
			.then(function(response) {
				vm.message = response.data;
				$location.path("/");
			}, function(response) {
				vm.message = response.statusText;
			});
	};
});