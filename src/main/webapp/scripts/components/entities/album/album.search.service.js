'use strict';

angular.module('therockbibleApp')
    .factory('AlbumSearch', function ($resource) {
        return $resource('api/_search/albums/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
