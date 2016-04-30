'use strict';

angular.module('therockbibleApp')
    .factory('CollectionSearch', function ($resource) {
        return $resource('api/_search/collections/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
