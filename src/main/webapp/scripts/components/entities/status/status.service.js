'use strict';

angular.module('therockbibleApp')
    .factory('Status', function ($resource, DateUtils) {
        return $resource('api/statuss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
