app.directive('focusOn', function() {
	return function(scope, elem, attr) {
		scope.$on('focusOn', function(e, name) {
			if(name === attr.focusOn) {
				elem[0].focus();
			}
		});
	};
});

app.directive('bsActiveLink', ['$location', function ($location) {
	return {
		restrict: 'A',
		replace: false,
		link: function (scope, elem) {
			scope.$on("$routeChangeSuccess", function () {
				var hrefs = ['/#' + $location.path(),
				             '#' + $location.path(), //html5: false
				             $location.path()]; //html5: true
				angular.forEach(elem.find('a'), function (a) {
					a = angular.element(a);
					if (-1 !== hrefs.indexOf(a.attr('href'))) {
						a.parent().addClass('active');
					} else {
						a.parent().removeClass('active');   
					}
	            });     
	        });
	    }
	}
}]);