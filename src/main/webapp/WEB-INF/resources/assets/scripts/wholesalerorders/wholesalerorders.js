var orderApp = angular.module("app.orders", [ "app", 'ngRoute', "ui.bootstrap",'ngSanitize', 'ui.select']).config([ "$provide", function($provide) {
	orderApp.value = $provide.value;
} ]).run([ "$injector", function($injector) {
	var registerValue = orderApp.value;
	orderApp.value = function(name, value) {
		try {
			angular.extend($injector.get(name), value);
		} catch (e) {
			registerValue(name, value);
		}
	};
} ]).config([ "$routeProvider", "UrlProvider", "activeId", function($routeProvider, UrlProvider, activeId) {
	$routeProvider.otherwise("/").when("/", {
		templateUrl : function(params) {
			params.activeId = activeId;
			return UrlProvider.buildUrl("wholesalerorders/xhr/list", params);
		},
		controller : "orderController"
	})
	$routeProvider.when("/wholesalerorders/:worderId", {
		templateUrl : function(params) {
			params.activeId = activeId;
			return UrlProvider.buildUrl("wholesalerorders/xhr/:worderId", params);
		},
		controller : 'orderviewController'
	});
	$routeProvider.when("/wholesalerorders/:worderId/edit", {
		templateUrl : function(params) {
			params.activeId = activeId;
			return UrlProvider.buildUrl("wholesalerorders/xhr/:worderId/edit", params);
		},
		controller : 'orderEditController'
	});
} ]).controller("orderController", [ "$scope", "$route", "$location", "$http", "activeId", "$routeParams","$filter", function($scope, $route, $location, $http, activeId, $routeParams,$filter) {
	var param = $routeParams;
	$scope.orderFilter =  [];
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
	$scope.onClickUser = function(worderId) {
		$location.path("/wholesalerorders/" + worderId).search({});
	};

	if(param.fromDate!=undefined && param.toDate!=undefined){
		$scope.f=$filter('date')(new Date(param.fromDate),'dd-MM-yyyy');
		$scope.t=$filter('date')(new Date(param.toDate),'dd-MM-yyyy');
		$scope.dates=$scope.f+" to "+$scope.t;
	}
	else
	{
		$scope.dates="select dates";
	}

	$scope.filterUserAccordingToDate=function(fromdate,todate){
		var from = fromdate.toString();
		var to = todate.toString();
		$scope.$apply(function(){
			$location.search({fromDate:from, toDate:to});
		});
	};

	$scope.orderStatus = param.orderStatus || [];
	$scope.filter = function(type) {
		var param = $routeParams;
		$scope.orderStatus = param.orderStatus || [];
		param.activeId = undefined;
		if ($scope.orderStatus.indexOf(type) >= 0) {
			$scope.orderStatus.splice($scope.orderStatus.indexOf(type), 1);
		} else {
			$scope.orderStatus.push(type);
		}
		$routeParams.orderStatus = $scope.orderStatus;
		$location.search($routeParams);
	}

	$scope.cancelDates = function(id){
		$scope.$apply(function(){
			$location.search('fromDate',null);
			$location.search('toDate', null);
		});
	};

	$scope.searchInOrder = function(q){
		if(q.length >= 1){
			$http.get('search?q='+q+'&fields=supplierName&fields=supplierMobileNo&fields=orderNo&fields=productName').then(function(resp){
				$scope.results = resp.data;
			})
		}
	};


	for(var i in $routeParams){
		if(i=='supplierName'||i=='supplierMobileNo' || i=='orderNo' || i=='productName' ){
			if($routeParams[i] instanceof Array){
				var values = $routeParams[i];
				for(var ind in values){
					$scope.orderFilter.push({'field':i,'result':values[ind],'label':{'supplierName':"in wholsalers",'supplierMobileNo':"in wholsalers",'productName':" in Product Name",'orderNo':"in wholsalers"}[i]})
				}
			}else {
				$scope.orderFilter.push({'field':i,'result':$routeParams[i],'label':{'supplierName':"in wholsalers",'supplierMobileNo':"in wholsalers",'productName':" in Product Name",'orderNo':"in wholsalers"}[i]})
			}
		}
	}

	$scope.selectedOrders = function(order){
		var queryNameParams = $routeParams[order.field];
		if(typeof queryNameParams == 'string' || typeof queryNameParams == 'object'){
			if(!(queryNameParams instanceof Array)){
				queryNameParams = [queryNameParams];
			}
			queryNameParams.push(order.result);
		}		
		else{
			queryNameParams = order.result;
		}
		$location.search(order.field,queryNameParams);	
	}

	$scope.deselectOrders = function(order){
		var queryNameParams = $routeParams[order.field];
		if(typeof queryNameParams == 'string'){
			$location.search(order.field,null);
		}else{
			queryNameParams.splice(queryNameParams.indexOf(order.result), 1);
			console.log('queryNameParams = ',queryNameParams)
			$location.search(order.field,queryNameParams);
		}
	};
} ]).controller("orderviewController", [ "$scope", "$route", "$location", "activeId","$http", function($scope, $route, $location, activeId,$http) {
	$scope.isCollapsed = false;
	$scope.activeId = activeId;
	var id=$('#productId').val();
	var url=contextUrl+'v1/admin/products/xhr/'+id+'/product';
	$http.get(url).then(function(resp) {
		changeStateOfProduct(resp.data.measurements); 
	}, function(error) {
	});
	function changeStateOfProduct(list)
	{
		if(list.length > 0){
			$scope.measurments=list.map(function(o){return o.measurement});
		}
		else
		{
		$scope.measurments=" ";
		}
	}
} ]).controller("orderEditController", [ "$scope", "$route", "$location", "activeId","$http","contextUrl", function($scope, $route, $location, activeId,$http, contextUrl) {
	$scope.activeId = activeId;
	function changeStateOfProduct(list)
	{
		if(list.length > 0){
			$scope.measurments=list.map(function(o){return o.measurement});
		}
	}
	/* for initial category*/
	var id=$('#category').find('select[select2]').val();
	$http.get(contextUrl+'v1/admin/products/xhr/categories/'+id+'/products').then(function(resp) {
		$scope.products=resp.data;
		var id=$('#product').find('select[select2], .select2').val();
		resp.data.forEach(function(currentValue,index){
			if(id==currentValue.id)
				changeStateOfProduct(currentValue.measurements); 
		});
	}, function(error) {
	});

	/*for changing category*/
	$('#category').find('select[select2], .select2').on('change', function() {
		var id=$(this).val();
		var url=contextUrl+'v1/admin/products/xhr/categories/'+id+'/products';
		$http.get(url).then(function(resp) {
			$("#productId").select2("val", "");
			$scope.products=resp.data;
		}, function(error) {
		});

	}); 

	$('#product').find('select[select2], .select2').on('change', function() {
		var id=$(this).val();
		if(id!=null){
			var url=contextUrl+'v1/admin/products/xhr/'+id+'/product';
			$http.get(url).then(function(resp) {
				changeStateOfProduct(resp.data.measurements); 
			}, function(error) {
			});
		}
	}); 

	$('[data-input="daterangepicker"]').daterangepicker({
		autoUpdateInput: false,
		minDate:moment(),
		locale: {
			cancelLabel: 'Clear',
			format: 'MM/DD/YYYY h:mm A'
		}

	})
	.on('apply.daterangepicker', function(ev, picker) {
		$(this).val(picker.startDate);
	}).on('cancel.daterangepicker', function(ev, picker) {
		$(this).val('');
	});

	$scope.updateOrder = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			console.log("--------------coming back");
			$location.path("/wholesalerorders/" + resp.data.id).search({})
		}, function(error) {
			console.log("--------------coming not back");
		});
	}
} ]).directive("myDaterangepicker", [ function($location) {
	return {
		restrict : "A",
		scope: {
			changeDate: "&",
			userRecentList: "&",
			deleteDates : "&"
		},
		link : function(scope, element, attr) {
			function cb(start, end) {
				$(element).find("span").html(start.format('DD-MM-YYYY') + ' - ' + end.format('DD-MM-YYYY'));
				scope.changeDate({from : start, to: end});
				scope.userRecentList({fromDate:start, toDate:end});
			}
//			cb(moment().subtract(14, 'days'), moment());

			$(element).daterangepicker({
				ranges: {
					'Today': [moment(), moment()],
					'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
					'Last 7 Days': [moment().subtract(6, 'days'), moment()],
					'Last 30 Days': [moment().subtract(29, 'days'), moment()],
					'This Month': [moment().startOf('month'), moment().endOf('month')],
					'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
				}
			}, cb);
			$(element).on('cancel.daterangepicker', function(ev, picker) {
				$(element).find("span").html('');
				scope.deleteDates({id :1});
			});
		}
	};
} ]);
