'use strict';

angular.module('therockbibleApp')
    .factory('FavouriteAlbumSearch', function ($resource) {
        return $resource('api/_search/favouriteAlbums/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
