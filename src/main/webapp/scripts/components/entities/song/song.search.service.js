'use strict';

angular.module('therockbibleApp')
    .factory('SongSearch', function ($resource) {
        return $resource('api/_search/songs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
