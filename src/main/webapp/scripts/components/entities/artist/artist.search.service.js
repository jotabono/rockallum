'use strict';

angular.module('therockbibleApp')
    .factory('ArtistSearch', function ($resource) {
        return $resource('api/_search/artists/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
