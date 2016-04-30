'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteArtistSearch', function ($resource) {
        return $resource('api/_search/favouriteArtists/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
