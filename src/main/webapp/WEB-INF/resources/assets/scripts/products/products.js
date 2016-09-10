var productApp = angular.module("app.products", [ "app", 'ngRoute', "ui.bootstrap" ]).config([ "$provide", function($provide) {
	productApp.value = $provide.value;
} ]).run([ "$injector", function($injector) {
	var registerValue = productApp.value;
	productApp.value = function(name, value) {
		try {
			angular.extend($injector.get(name), value);
		} catch (e) {
			registerValue(name, value);
		}
	};
} ]).config([ "$routeProvider", "UrlProvider", function($routeProvider, UrlProvider) {
	$routeProvider.otherwise("/").when("/", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("products/xhr/categories", params);
		},
		controller : "categoriesController"
	}).when("/new", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("products/xhr/new", params);
		},
		controller : "newProductController"
	}).when("/:categoryId", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("products/xhr/categories/:categoryId", params);
		},
		controller : "productsController"
	}).when("/products/:productId", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("products/xhr/:productId", params);
		},
		controller : "viewProductController"
	}).when("/products/:productId/edit", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("products/xhr/:productId/edit", params);
		},
		controller : "editProductController"
	});
} ]).controller("categoriesController", [ "$scope", "$route", "$location", function($scope, $route, $location) {
	$scope.page = {
		set : function(number, size, totalElements) {
			$scope.page.number = number;
			$scope.page.size = size;
			$scope.page.totalElements = totalElements;
		},
		pageChanged : function() {
			$route.updateParams({
				page : $scope.page.number - 1
			});
		}
	};
	$scope.onClickCategory = function(categoryId) {
		$location.path("/" + categoryId).search({});
	};
} ]).controller("productsController", [ "$scope", "$route", "$location", "$http", "category", "toaster", function($scope, $route, $location, $http, category, toaster) {
	$scope.page = {
		set : function(number, size, totalElements) {
			$scope.page.number = number;
			$scope.page.size = size;
			$scope.page.totalElements = totalElements;
		},
		pageChanged : function() {
			$route.updateParams({
				page : $scope.page.number - 1
			});
		}
	};
	$scope.updateCategory = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			$route.reload();
		}, function(error) {
			toaster.pop({
				type : 'error',
				title : 'Category Not updated',
				body : error.data.reason,
			});
		});
	};
	$scope.onClickProduct = function(productId) {
		$location.path("/products/" + productId).search({});
	};
} ]).controller("newProductController", [ "$scope", "$http", "$location", "toaster", function($scope, $http, $location, toaster) {
	$scope.saveProduct = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			$scope.processQueue({productId:resp.data.id}).then(function(){
				$location.path("/products/" + resp.data.id).search({});
			});
		}, function(error) {
			toaster.pop({
				type : 'error',
				title : 'Product not saved',
				body : error.data.reason,
			});
		});
	};
} ]).controller("viewProductController", [ "$scope", "product", "$route", "$location", "$filter", "$http", "toaster", function($scope, product, $route, $location, $filter, $http, toaster) {
	$scope.product = product;
	$scope.deleteProduct = function(id) {
		$http.get("products/xhr/"+id+"/delete").then(function(resp){
			toaster.pop({
				type : 'success',
				title : 'Product Deleted',
			});
			$location.path("/products/" + product.id+1).search({})
		}, function(error){
			toaster.pop({
				type : 'error',
				title : 'Cannot Delete',
				body : error.data.reason,
			});
		});
	}
} ]).controller("editProductController", [ "$scope", "product", "$location", "toaster", "$http", function($scope, product, $location, toaster, $http) {
	$scope.updateProduct = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			$location.path("/products/" + product.id).search({})
		}, function(error) {
			toaster.pop({
				type : 'error',
				title : 'Product Not updated',
				body : error.data.reason,
			});
		});
	};
} ]);
