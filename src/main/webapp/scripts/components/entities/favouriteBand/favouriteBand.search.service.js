'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteBandSearch', function ($resource) {
        return $resource('api/_search/favouriteBands/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
