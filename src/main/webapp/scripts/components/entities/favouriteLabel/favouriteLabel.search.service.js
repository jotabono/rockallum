'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteLabelSearch', function ($resource) {
        return $resource('api/_search/favouriteLabels/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
