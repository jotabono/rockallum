'use strict';

angular.module('therockbibleApp')
    .factory('Band', function ($resource, DateUtils) {
        return $resource('api/bands/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.foundingDate = DateUtils.convertLocaleDateFromServer(data.foundingDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.foundingDate = DateUtils.convertLocaleDateToServer(data.foundingDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.foundingDate = DateUtils.convertLocaleDateToServer(data.foundingDate);
                    return angular.toJson(data);
                }
            }
        });
    });
