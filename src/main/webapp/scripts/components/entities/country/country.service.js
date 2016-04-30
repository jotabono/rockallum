'use strict';

angular.module('therockbibleApp')
    .factory('Country', function ($resource, DateUtils) {
        return $resource('api/countrys/:id', {}, {
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
