'use strict';

angular.module('therockbibleApp')
    .factory('Review', function ($resource, DateUtils) {
        return $resource('api/reviews/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.reviewDate = DateUtils.convertDateTimeFromServer(data.reviewDate);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'reviewsByCurrentBand': {
                method: 'GET',
                isArray: true,
                url: 'api/band/:id/reviews'
            },
            'reviewsByCurrentAlbum': {
                method: 'GET',
                isArray: true,
                url: 'api/album/:id/reviews'
            },
            'addReview': {
                method: 'POST', isArray: false, url: 'api/band/:id/reviews'
            },
            'addReviewAlbum': {
                method: 'POST', isArray: false, url: 'api/album/:id/reviews'
            }
        });
    });
