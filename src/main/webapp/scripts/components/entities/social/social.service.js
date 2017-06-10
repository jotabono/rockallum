'use strict';

angular.module('therockbibleApp')
    .factory('Social', function ($resource, DateUtils) {
        return $resource('api/socials/:id', {}, {
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
