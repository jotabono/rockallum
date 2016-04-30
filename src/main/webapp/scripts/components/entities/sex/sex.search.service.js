'use strict';

angular.module('therockbibleApp')
    .factory('SexSearch', function ($resource) {
        return $resource('api/_search/sexs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
