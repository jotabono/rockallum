'use strict';

angular.module('therockbibleApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


