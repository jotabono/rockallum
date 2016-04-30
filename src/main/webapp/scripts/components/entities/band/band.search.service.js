'use strict';

angular.module('therockbibleApp')
    .factory('BandSearch', function ($resource) {
        return $resource('api/_search/bands/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
