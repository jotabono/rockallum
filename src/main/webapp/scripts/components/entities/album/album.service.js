'use strict';

angular.module('therockbibleApp')
    .factory('Album', function ($resource, DateUtils) {
        return $resource('api/albums/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.releaseDate = DateUtils.convertLocaleDateFromServer(data.releaseDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.releaseDate = DateUtils.convertLocaleDateToServer(data.releaseDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.releaseDate = DateUtils.convertLocaleDateToServer(data.releaseDate);
                    return angular.toJson(data);
                }
            }
        });
    });
