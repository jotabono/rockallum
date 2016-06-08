'use strict';

angular.module('therockbibleApp')
    .factory('Artist', function ($resource, DateUtils) {
        return $resource('api/artists/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.bornIn = DateUtils.convertLocaleDateFromServer(data.bornIn);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.bornIn = DateUtils.convertLocaleDateToServer(data.bornIn);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.bornIn = DateUtils.convertLocaleDateToServer(data.bornIn);
                    return angular.toJson(data);
                }
            }
        });
    });
