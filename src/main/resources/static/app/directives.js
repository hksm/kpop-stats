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
		link: function(scope, elem) {
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

app.directive('restrictNumber', function() {
	return {
		require: 'ngModel',
		link: function(scope, element, attrs, modelCtrl) {
			modelCtrl.$parsers.push(function (inputValue) {
				var num = parseInt(inputValue, 10);
				var transf;
				if (!num) {
					trans = '';
				} else if (num < 1900) {
					trans = 1900;	
				} else if (num > 2999) {
					trans = 2999;	
				}
				modelCtrl.$setViewValue(transf);
           		modelCtrl.$render();
           		return transf;
				    
			});
		}
	};
});