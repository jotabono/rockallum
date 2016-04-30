'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteReviewController', function ($scope, $state, FavouriteReview, FavouriteReviewSearch) {

        $scope.favouriteReviews = [];
        $scope.loadAll = function() {
            FavouriteReview.query(function(result) {
               $scope.favouriteReviews = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FavouriteReviewSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.favouriteReviews = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.favouriteReview = {
                liked: null,
                id: null
            };
        };
    });
