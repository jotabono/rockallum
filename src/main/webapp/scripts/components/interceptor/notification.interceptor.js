 'use strict';

angular.module('therockbibleApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-therockbibleApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-therockbibleApp-params')});
                }
                return response;
            }
        };
    });
