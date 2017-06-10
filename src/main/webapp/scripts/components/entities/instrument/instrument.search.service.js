'use strict';

angular.module('therockbibleApp')
    .factory('InstrumentSearch', function ($resource) {
        return $resource('api/_search/instruments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
