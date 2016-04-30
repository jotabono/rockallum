'use strict';

angular.module('therockbibleApp')
    .factory('StatusSearch', function ($resource) {
        return $resource('api/_search/statuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
