'use strict';

angular.module('therockbibleApp')
    .factory('Band', function ($resource, DateUtils) {
        return $resource('api/bands/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getBandsLiked': {
                method: 'GET',
                isArray: true,
                url: 'api/bands/userliked'
            },
            'getAlbumsLiked': {
                method: 'GET',
                isArray: true,
                url: 'api/albums/userlikedalbums',
            }
        });
    });
