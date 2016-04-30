'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteReviewSearch', function ($resource) {
        return $resource('api/_search/favouriteReviews/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
