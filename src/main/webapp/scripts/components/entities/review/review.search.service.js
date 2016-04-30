'use strict';

angular.module('therockbibleApp')
    .factory('ReviewSearch', function ($resource) {
        return $resource('api/_search/reviews/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
