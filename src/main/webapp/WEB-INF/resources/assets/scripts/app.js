var app = angular.module("app", [ 'toaster', 'ngAnimate' ]);
app.config([ "$httpProvider", function($httpProvider) {
	$httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
} ]);
$.ajaxSetup({
    beforeSend: function(jqXHR, settings) {
        if (settings.dataType === 'script') {
            settings.url = settings.url.replace(/\??&?_=[0-9]+/, '');
        }
    }
});
app.run([ "$rootScope", "$filter", function($rootScope, $filter) {
	$rootScope.formateDate = function(datestr) {
		return $filter('date')(new Date(datestr));
	};
} ]).provider("Url", [ function() {
	var build = function buildUrl(url, params) {
        var buildParam = "";
        if(url.split("?").length>1){
            var originalParam = {};
            url.split("?")[1].split("&").forEach(function(pair){
            	if(pair.split("=").length>1)
                originalParam[pair.split("=")[0]]=decodeURIComponent(pair.split("=")[1]);
            });
            params= angular.extend(originalParam,params);
            url = url.split("?")[0];
        }
        for ( var paramName in params) {
            if (url.indexOf(":" + paramName) !== -1) {
                url = url.replace(":" + paramName, params[paramName]);
            } else {
                if (params[paramName] instanceof Array) {
                    var arrParam = "";
                    for ( var i in params[paramName]) {
                        buildParam += !buildParam.startsWith("?") ? "?" : !buildParam.endsWith("&") ? "&" : "";
                        buildParam += paramName + "=" + encodeURIComponent(params[paramName][i]);
                    }
                } else {
                    buildParam += !buildParam.startsWith("?") ? "?" : !buildParam.endsWith("&") ? "&" : "";
                    buildParam += paramName + "=" + encodeURIComponent(params[paramName]);
                }
            }
        }
        return url + buildParam;
    };
	return {
		"buildUrl" : function(url, params) {
			params = params ? angular.copy(params) : {};
			params._ = Math.random();
			return build(url, params);
		},
		$get : [ function() {
			return {
				"buildUrl" : function(url, params) {
					params = params ? angular.copy(params) : {};
					params._ = Math.random();
					return build(url, params);
				},
				"build" : build
			};
		} ]
	};
} ]).directive("select2", [ function() {
	return {
		restrict : "A",
		link : function($scope, element, attr) {
			$(element).select2();
			if(attr.value){
				$(window).load(function () {
					console.log("am loading.....");
					element.val(attr.value).trigger("change");
				});
				setTimeout(function(){ element.val(attr.value).trigger("change"); }, 1000);
			}

		}
	};
} ]).directive("dropzone", [ "$q", "Url", function($q, Url) {
	Dropzone.autoDiscover = false;
	return {
		restrict : "A",
		link : function($scope, element, attr) {
			var config = attr;
			if (attr.headers) {
				var headers = JSON.parse(attr.headers.replace(/\'/g, '"'));
				config.headers = headers;
			}
			config.maxFiles = 1;
			config.init = function() {
				var me = this;
				if (attr.onProcessQueue) {
					$scope[attr.onProcessQueue] = function(param) {
						param && (me.options.url = Url.build(me.options.url, param));
						var defer = $q.defer();
						if (me.getActiveFiles().length < 1)
							defer.resolve();
						me.on('success', function(file, responseBody, hxml) {
							defer.resolve(responseBody);
						});
						me.on('error', function(file, responseBody, hxml) {
							defer.reject(responseBody);
						});
						me.processQueue();
						return defer.promise;
					};
				}
				$scope[attr.pr] && $scope[attr.dropzone](this);
				this.on("addedfile", function(event) {
					if (this.files[1] != null) {
						this.removeFile(this.files[0]);
					}
				});
				if (attr.imageUrl) {
					var mockFile = {
						name : attr.title,
						size : 12345
					};
					this.files[0] = mockFile;
					this.options.addedfile.call(this, mockFile);
					this.options.thumbnail.call(this, mockFile, attr.imageUrl);
					this.emit("complete", mockFile);
				}
			};
			$(element).dropzone(config);
		}
	};
} ]).directive("validateForm", [ function() {
	function validateServer(url, param, element) {
		$(element).parent().removeClass("unique-complete");
		$(element).parent().addClass("unique-prog");
		var isExsit = false;
		$.ajax({
			type : "GET",
			url : url,
			data : param,
			async : false,
			success : function(msg) {
				isExsit = false;
			},
			error : function() {
				isExsit = true;
			}
		});
		window.setTimeout(function() {
			$(element).parent().removeClass("unique-prog");
			$(element).parent().addClass("unique-complete");
		}, 100);
		return isExsit;
	}
	$.validator.addMethod("unique-email", function(value, element) {
		var param = {};
		param.email = value;
		if ($(element).attr("activeId")) {
			param.activeId = $(element).attr("activeId");
		}
		return validateServer($(element).attr("unique-email"), param, element);
	}, "This UserEmail already registered");

	$.validator.addMethod("unique-mobile", function(value, element) {
		var param = {};
		param.mobile = value;
		if ($(element).attr("activeId")) {
			param.activeId = $(element).attr("activeId");
		}
		return validateServer($(element).attr("unique-mobile"), param, element);
	}, "This Mobile number already registered");
	return {
		restrict : "A",
		link : function($scope, element, attr) {
			$(element).find("[unique-email],[unique-mobile]").parent().addClass("unique-complete");
			var config = {
				ignore : '.ignore',
				errorElement : 'div',
				errorClass : 'text-danger',
				errorPlacement : function(error, element) {
					var $formGroup = element.closest('.form-group');
					error.appendTo($formGroup);
				},
				highlight : function(element) {
					var $formGroup = $(element).closest('.form-group');
					$formGroup.addClass('has-error');
				},
				unhighlight : function(element) {
					var $formGroup = $(element).closest('.form-group');
					$formGroup.removeClass('has-error');
				},
				showErrors : function() {
					var $form = $(this.currentForm), $panel = $form.closest('.panel'), errors = this.numberOfInvalids();

					this.defaultShowErrors();

					if (errors) {
						// change panel state
						$panel.removeClass('panel-default');
						$panel.addClass('panel-danger');

						// disable submit button
						$form.find('[type="submit"]').attr('disabled', true);

						// remove existing error summary
						$panel.find('.panel-title > small').remove();
						// Display a summary of invalid fields as a subtitle
						$panel.find('.panel-title').append(' <small>' + errors + ' field(s) are invalid</small>');
					} else {
						// change panel state
						$panel.removeClass('panel-danger');
						$panel.addClass('panel-default');

						// enable submit button
						$form.find('[type="submit"]').attr('disabled', false);

						// remove error summary
						$panel.find('.panel-title > small').remove();
					}
				},

			};
			if (attr.submitHandler && $scope[attr.submitHandler]) {
				config.submitHandler = function(form) {
					console.log("in configuration");
					$scope[attr.submitHandler]($(form).serialize(), $(form).attr("action"));
				};
			}
			var form = $(element).validate(config);
			$(element).find('select[select2], .select2').on('change', function() {
				form.element($(this));
			});
		}
	};
} ]);
angular.bootstrap(document.getElementsByTagName("body"), [ 'app' ]);
