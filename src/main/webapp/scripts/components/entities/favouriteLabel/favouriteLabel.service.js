'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteLabel', function ($resource, DateUtils) {
        return $resource('api/favouriteLabels/:id', {}, {
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
