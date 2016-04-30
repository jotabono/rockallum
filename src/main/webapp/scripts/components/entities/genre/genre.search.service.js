'use strict';

angular.module('therockbibleApp')
    .factory('GenreSearch', function ($resource) {
        return $resource('api/_search/genres/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
