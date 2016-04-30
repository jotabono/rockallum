'use strict';

angular.module('therockbibleApp')
    .factory('Genre', function ($resource, DateUtils) {
        return $resource('api/genres/:id', {}, {
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
