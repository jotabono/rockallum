'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteSongSearch', function ($resource) {
        return $resource('api/_search/favouriteSongs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
