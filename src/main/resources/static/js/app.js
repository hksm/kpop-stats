var app = angular.module('kpop-stats', ['ngRoute', 'oi.select', 'ui.bootstrap', 'bootstrap.fileField']);

app.config(function($routeProvider, $httpProvider) {
	$routeProvider
		.when('/', {
			templateUrl: 'home.html',
			controller: 'home',
			controllerAs: 'controller'
		})
		.when('/login', {
			templateUrl: 'login.html',
			controller: 'navigation',
			controllerAs: 'controller'
		})
		.when('/register', {
			templateUrl: 'signup.html',
			controller: 'signupController',
			controllerAs: 'controller'
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
});

app.controller('home', function($http) {
	var self = this;
	$http.get('/resource').then(function(response) {
		self.greeting = response.data;
	});
});

app.controller('navigation', function($rootScope, $http, $location) {
	var self = this;
	
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
	
	self.navbarCollapsed = false;
	
	authenticate();
	self.credentials = {
		remember: true
	};
	self.login = function() {
		authenticate(self.credentials, function() {
			if ($rootScope.authenticated) {
				$location.path("/");
				self.error = false;
			} else {
				$location.path("/login");
				self.error = true;
			}
		});
	};
		
	self.logout = function() {
		$http.post('logout', {}).finally(function() {
			$rootScope.authenticated = false;
			$location.path("/");
		});
	};
});

app.controller('signupController', function($http, $location) {
	var self = this;
	
	self.register = function() {
		$http.post('/signup', self.user)
			.then(function(response) {
				self.message = response.data;
				$location.path("/");
			}, function(response) {
				self.message = response.statusText;
			});
	};
});