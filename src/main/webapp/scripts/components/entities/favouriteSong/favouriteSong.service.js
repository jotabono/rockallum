'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteSong', function ($resource, DateUtils) {
        return $resource('api/favouriteSongs/:id', {}, {
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
