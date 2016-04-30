'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteReviewDetailController', function ($scope, $rootScope, $stateParams, entity, FavouriteReview, Band, User) {
        $scope.favouriteReview = entity;
        $scope.load = function (id) {
            FavouriteReview.get({id: id}, function(result) {
                $scope.favouriteReview = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:favouriteReviewUpdate', function(event, result) {
            $scope.favouriteReview = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
