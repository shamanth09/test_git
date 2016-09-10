var userApp = angular.module("app.users", [ "app", 'ngRoute', "ui.bootstrap", "angular.morris-chart",'ngSanitize', 'ui.select' ]).
directive("myDaterangepicker", [ function($location) {
	return {
		restrict : "A",
		scope: {
			changeDate: "&",
			userRecentList: "&",
			deleteDates : "&"
		},
		link : function(scope, element, attr) {
			function selectedDate(start, end) {
				$(element).find("span").html(start.format('DD-MM-YYYY') + ' to ' + end.format('DD-MM-YYYY'));
				scope.changeDate({from : start, to: end});
				scope.userRecentList({fromDate:start, toDate:end});
			}

			$(element).daterangepicker({ 
				ranges: {
					'Today': [moment(), moment()],
					'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
					'Last 7 Days': [moment().subtract(6, 'days'), moment()],
					'Last 30 Days': [moment().subtract(29, 'days'), moment()],
					'This Month': [moment().startOf('month'), moment().endOf('month')],
					'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
				}
			},  selectedDate);

			$(element).on('cancel.daterangepicker', function(ev, picker) {
				$(element).find("span").html('');
				scope.deleteDates({id :1});
			});
		}
	};
} ]).config([ "$provide", function($provide) {
	userApp.value = $provide.value;
} ]).run([ "$injector", function($injector) {
	var registerValue = userApp.value;
	userApp.value = function(name, value) {
		try {
			angular.extend($injector.get(name), value);
		} catch (e) {
			registerValue(name, value);
		}
	};
} ]).config([ "$routeProvider", "UrlProvider", function($routeProvider, UrlProvider) {

	$routeProvider.otherwise("/").when("/", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("users/xhr/list", params);
		},
		controller : "userController"
	}).when("/users/:userId", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("users/xhr/:userId", params);
		},
		controller : 'userViewController'
	}).when("/users/:userId/edit-user", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("users/xhr/:userId/edit-user", params);
		},
		controller : 'userEditController'
	}).when("/users/:userId/orders", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("users/xhr/:userId/orders", params);
		},
		controller : 'ordersController'
	}).when("/new-user", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("users/xhr/form", params);
		},
		controller : 'newUserController'
	}).when("/notregisterduserlist", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("users/xhr/not-registered-list", params);
		},
		controller : 'notRegistereduserController'
	}).when("/users/:userId/setting", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("users/xhr/:userId/setting", params);
		},
		controller : 'userSettingController'
	}).when("/user-relation/:userId", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("relations/xhr/user-relation/:userId", params);
		},
		controller : 'userRelationController'
	}).when("/relations/:currentUser/user-relation-edit/:id", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("relations/xhr/:currentUser/user-relation-edit/:id", params);
		},
		controller : 'relationEditController'
	}).when("/relations/add-relation/:id/:userRole", {
		templateUrl : function(params) {
			return UrlProvider.buildUrl("relations/xhr/add-relation/:id/:userRole", params);
		},
		controller : 'relationAddController'
	});;
} ]).controller("notRegistereduserController",["$scope","$route", function($scope,$route){
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
			},
	};
}]).controller("userController", [ "$scope", "$route", "$http", "$location", "$log", "$routeParams","$filter", function($scope,  $route, $http, $location, $log, $routeParams, $filter) {
	var param = $routeParams;
	$scope.activeFlags = param.activeFlags || [];
	$scope.filterAccordingToStatus = function(status) {
		var param = $routeParams;
		$scope.activeFlags = param.activeFlags || [];
		if ($scope.activeFlags.indexOf(status) >= 0) {
			$scope.activeFlags.splice($scope.activeFlags.indexOf(status), 1);
		} else {
			$scope.activeFlags.push(status);
		}
		$routeParams.activeFlags = $scope.activeFlags;
		$location.search($routeParams);
	};

	$scope.searchInUserTable = function(q){
		if(q.length >= 1){
			$http.get('search?q='+q+'&fields=userName&fields=userEmail&fields=userMobile').then(function(resp){
				$scope.results = resp.data;
			})
		}
	};

	$scope.userFilter =  [];

	for(var i in $routeParams){
		if(i=='userName'||i=='mobile'||i=='email'){
			if($routeParams[i] instanceof Array){
				var values = $routeParams[i];
				for(var ind in values){
					$scope.userFilter.push({'field':i,'result':values[ind],'label':{'userName':"in User Name",'mobile':"in User Mobile",'email':" in User email"}[i]})
				}
			}else {
				$scope.userFilter.push({'field':i,'result':$routeParams[i],'label':{'userName':"in User Name",'mobile':"in User Mobile",'email':" in User email"}[i]})
			}
		}
	}

	$scope.selectedUser = function(user){
		var queryNameParams = $routeParams[user.field];
		if(typeof queryNameParams == 'string' || typeof queryNameParams == 'object'){
			if(!(queryNameParams instanceof Array)){
				queryNameParams = [queryNameParams];
			}
			queryNameParams.push(user.result);
		}		
		else{
			queryNameParams = user.result;
		}
		$location.search(user.field,queryNameParams);
	}

	$scope.deselectUsers = function(user){
		var queryNameParams = $routeParams[user.field];
		if(typeof queryNameParams == 'string'){
			$location.search(user.field,null);
		}else{
			queryNameParams.splice(queryNameParams.indexOf(user.result), 1);
			$location.search(user.field,queryNameParams);
		}
	}

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
			},
	};
	$scope.filterUserAccordingToDate=function(fromdate,todate){
		var from = fromdate.toString();
		var to = todate.toString();
		$scope.$apply(function(){
			$location.search({'fromDate':from, 'toDate':to});
		});
	};

	$scope.cancelDates = function(id){
		$scope.$apply(function(){
			$location.search('fromDate',null);
			$location.search('toDate', null);
		});
	};

	var param = $routeParams;
	if(param.fromDate!=undefined && param.toDate!=undefined){
		$scope.df= $filter('date')(new Date(param.fromDate),'dd-MM-yyyy');
		$scope.td = $filter('date')(new Date(param.toDate),'dd-MM-yyyy');
		$scope.dates = $scope.df+" to "+$scope.td;
	}
	else{
		$scope.dates = "user filter according to dates";
	}


	$scope.onClickUser = function(userId) {
		$location.path("/users/" + userId).search({});
	};
} ]).controller("newUserController", [ "$scope", "$http", "$filter", "$location", function($scope, $http, $filter, $location) {
	$scope.addUser = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			if (resp.data.id != null)
				$location.path("/users/" + resp.data.id).search({})
		}, function(error) {
		});
	}

} ]).controller("userViewController", [ "$scope", "user", "$http","$httpParamSerializer","$location","toaster", function($scope, user, $http, $httpParamSerializer,$location,toaster) {
	$scope.user = user;
	$scope.meltingAndSealSelected = [];
	$scope.meltingAndSealValuesForTable = [];
	$scope.newMeltingAndSeal = [];
	$scope.disableAddButton = 0;

	if(user.userRole === 'Wholesaler'){
		$http.get('users/xhr/meltingAndSeal/'+user.id).then(function(resp){
			$scope.meltingAndSealValuesForTable = resp.data;
		});
	}
	$scope.editMeltingAndSeal = function (meltingAndSeal) {
		$scope.meltingAndSealSelected = angular.copy(meltingAndSeal);
	};

	$scope.getTemplate = function (meltingAndSeal) {
		if (meltingAndSeal.id == undefined){
			$scope.disableAddButton = 1;
			return 'edit';
		}
		else if (meltingAndSeal.id === $scope.meltingAndSealSelected.id ) return 'edit';
		else return 'display';
	};

	$scope.addMeltAndSeal = function() {
		$scope.newMeltingAndSeal = {
				melting: '',
				seal: '',
		};

		$scope.meltingAndSealValuesForTable.push($scope.newMeltingAndSeal);
	};

	$scope.removeMeltAndSeal = function(index) {
		$scope.disableAddButton = 0;
		$scope.meltingAndSealValuesForTable.splice(index, 1);
	};

	$scope.statuses = ["ACTIVE", "INACTIVE"];
	$scope.meltingAndSealSelected.activeFlag = $scope.statuses[0];

	$scope.updateMeltingAndSeal = function (changeMeltAndSeal) {
		delete changeMeltAndSeal.createTimestamp
		if(changeMeltAndSeal.melting != null && changeMeltAndSeal.seal != null && changeMeltAndSeal.activeFlag != null ){
			$http.post('melting-seal/xhr/update/'+user.id,$httpParamSerializer(changeMeltAndSeal)).then(function(resp){
				$scope.refresh();
				toaster.pop({
					type : 'success',
					title : 'Melting and Seal Saved',
					body : ''
				});
			});
			$scope.meltingAndSealValuesForTable[changeMeltAndSeal.id] = angular.copy($scope.meltingAndSealSelected);
		}
		$scope.disableAddButton = 0;
		$scope.reset();
	};

	$scope.reset = function () {
		$scope.meltingAndSealSelected = {};
	};

	$scope.refresh = function () {
		$http.get('users/xhr/meltingAndSeal/'+user.id).then(function(resp){
			$scope.meltingAndSealValuesForTable = resp.data;
			$scope.disableAddButton = 0;
		})
	};

	if(user.userRole === 'Retailer'){
		$http.get('order-summary/xhr/statuswise',{params :{activeId:user.id}}).then(function(resp){
			var chart=[];
			for(var i = 0;i<resp.data.length;i++){
				var item = resp.data[i].totalOrderCount;
				var fromdate = resp.data[i].fromDate;
				var formatDate =moment(fromdate).format('D MMM YY HH:MM');
				chart.push({y: formatDate, a: item});
			}
			$scope.charData = chart;
		})
	}
	if(user.userRole === 'Wholesaler'){
		var chart=[];
		$http.get('ws-order-summary/xhr/statuswise',{params :{activeId:user.id}}).then(function(resp1){
			$http.get('order-summary/xhr/statuswise',{params :{activeId:user.id}}).then(function(resp2){
				for(var i = 0;i<resp1.data.length;i++){
					var item = resp1.data[i].totalOrderCount;
					var item2 = resp2.data[i].totalOrderCount;
					var fromdate = resp1.data[i].fromDate;
					var fromdate2 = resp2.data[i].fromDate;
					var formatDate = moment(fromdate).format('D MMM YY HH:MM');
					var formatDate2 =moment(fromdate2).format('D MMM YY HH:MM');
					if(formatDate === formatDate2)
						chart.push({y: formatDate, a: item, b:item2});
				}
				$scope.charData = chart;
			})
		})
	}
	if(user.userRole === 'Supplier'){
		$http.get('ws-order-summary/xhr/statuswise',{params :{activeId:user.id}}).then(function(resp){
			var chart=[];
			for(var i = 0;i<resp.data.length;i++){
				var item = resp.data[i].totalOrderCount;
				var fromdate = resp.data[i].fromDate;
				var formatDate =moment(fromdate).format('D MMM YY HH:MM');
				chart.push({y: formatDate, a: item});
			}
			$scope.charData = chart;
		})
	}


	$scope.reportChart=function(start,end){

		var fromDate = start.toString();
		var toDate = end.toString();
		if(user.userRole === 'Retailer'){
			$http.get('order-summary/xhr/statuswise',{params :{activeId:user.id,from:fromDate,to:toDate}}).then(function(resp){
				var chart=[];
				for(var i = 0;i<resp.data.length;i++){
					var item = resp.data[i].totalOrderCount;
					var fromdate = resp.data[i].fromDate;
					var formatDate =moment(fromdate).format('D MMM YY HH:MM');
					chart.push({y: formatDate, a: item});
				}
				$scope.charData = chart;
			})
		}
		if(user.userRole === 'Wholesaler'){
			var chart=[];
			$http.get('ws-order-summary/xhr/statuswise',{params :{activeId:user.id,from:fromDate,to:toDate}}).then(function(resp1){
				$http.get('order-summary/xhr/statuswise',{params :{activeId:user.id,from:fromDate,to:toDate}}).then(function(resp2){
					for(var i = 0;i<resp1.data.length;i++){
						var item = resp1.data[i].totalOrderCount;
						var item2 = resp2.data[i].totalOrderCount;
						var fromdate = resp1.data[i].fromDate;
						var fromdate2 = resp2.data[i].fromDate;
						var formatDate = moment(fromdate).format('D MMM YY HH:MM');
						var formatDate2 =moment(fromdate2).format('D MMM YY HH:MM');
						if(formatDate === formatDate2)
							chart.push({y: formatDate, a: item, b:item2});
					}
					$scope.charData = chart;
				})
			})
		}
		if(user.userRole === 'Supplier'){
			$http.get('ws-order-summary/xhr/statuswise',{params :{activeId:user.id,from:fromDate,to:toDate}}).then(function(resp){
				var chart=[];
				for(var i = 0;i<resp.data.length;i++){
					var item = resp.data[i].totalOrderCount;
					var fromdate = resp.data[i].fromDate;
					var formatDate =moment(fromdate).format('D MMM YY HH:MM');
					chart.push({y: formatDate, a: item});
				}
				$scope.charData = chart;
			})
		}
	}

	$scope.userRelations = function(userId){
		$location.path("/user-relation/" + userId).search({});
	}

	$scope.userDevices = [];
	$http.get('users/xhr/user-devices-list/'+user.id).then(function(resp){
		$scope.userDevices = resp.data;
	});
	$scope.deleteDevice = function(device){
		$http.get('users/xhr/delete-user-device/'+device.id).then(function(resp){
			var index = -1;		
			var userArr = eval( $scope.userDevices);
			for( var i = 0; i < userArr.length; i++ ) {
				if( userArr[i].id === device.id ) {
					index = i;
					break;
				}
			}
			if( index === -1 ) {
				alert( "Something gone wrong" );
			}
			$scope.userDevices.splice(index, 1);

			toaster.pop({
				type : 'success',
				title : 'Device deleted',
				body : '',
			});

		});
	}


} ]).controller("userEditController", [ "$scope", "user", "$location", "$http", "$filter", "toaster", function($scope, user, $location, $http, $filter, toaster) {
	$scope.formateDate = function(datestr) {
		return $filter("date")(new Date(datestr), "mediumDate");
	};
	$scope.updateUser = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			$location.path("/users/" + user.id).search({})
			toaster.pop({
				type : 'success',
				title : 'User updated successfully',
				body : '',
			});
		}, function(error) {
		});
	}
} ]).controller("userSettingController", [ "$scope", "$route", "$http", "$filter", "toaster","user", function($scope, $route, $http, $filter, toaster,user) {
	$scope.updatePassword = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			toaster.pop({
				type : 'success',
				title : 'Password changed',
				body : ' '
			});
		}, function(error) {
			toaster.pop({
				type : 'error',
				title : 'Password change failed',
				body : error.data.reason,
			});
		});
	}
	$scope.updateRole = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			toaster.pop({
				type : 'success',
				title : 'Role changed',
				body : ' ',
			});
		}, function(error) {
			toaster.pop({
				type : 'error',
				title : 'Role change failed',
				body : error.data.reason,
			});
		});
	}
	$scope.changeStatus = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp) {
			toaster.pop({
				type : 'success',
				title : 'Status changed',
				body : ' ',
			});
		}, function(error) {
			toaster.pop({
				type : 'error',
				title : 'Status change failed',
				body : error.data.reason,
			});
		});
	}
	$scope.deleteUserToken = function(tknId){
		$http.get('users/xhr/deleteUserToken',{params :{tokenId:tknId}}).then(function(resp) {
			$route.reload();
			toaster.pop({
				type : 'success',
				title : 'Token Deleted successfully',
				body : '',
			});
		});
	}
} ]).controller("userRelationController", [ "$scope","user", "$location", "$http", "$filter", "toaster", function($scope, user, $location, $http, $filter, toaster) {
	$scope.relationClicked = function(relatedUserId){
		$location.path("/relations/"+user.id+"/user-relation-edit/" + relatedUserId).search({});
	}

	$scope.addRelation=function(userRole){
		$location.path("/relations/add-relation/" + user.id+ "/"+userRole).search({});
	};
} ]).controller("relationEditController", [ "$scope", "$location", "$http", "$filter", "toaster", function($scope, $location, $http, $filter, toaster) {
	$scope.updateRelation = function(formData, actionUrl) {
		$http.post(actionUrl, formData).then(function(resp){
			$location.path("/user-relation/" + resp.data.id).search({});
			toaster.pop({
				type : 'success',
				title : 'Relation status updated',
				body : '',
			});
		})
	}
} ]).controller("relationAddController", [ "$scope", "$location", "$http", "$filter", "toaster", function($scope, $location, $http, $filter, toaster) {
	$scope.addRelationToCurrentUser = function(formData, actionUrl){
		$http.post(actionUrl, formData).then(function(resp){
			$location.path("/user-relation/" + resp.data.id).search({});
			toaster.pop({
				type : 'success',
				title : 'Relation requested',
				body : ' ',
			});
		})
	}
} ]);
