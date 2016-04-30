'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteReview', function ($resource, DateUtils) {
        return $resource('api/favouriteReviews/:id', {}, {
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
