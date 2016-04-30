'use strict';

angular.module('therockbibleApp')
    .factory('AlbumTypesSearch', function ($resource) {
        return $resource('api/_search/albumTypess/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
