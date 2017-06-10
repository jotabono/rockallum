'use strict';

angular.module('therockbibleApp')
    .factory('SocialSearch', function ($resource) {
        return $resource('api/_search/socials/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
