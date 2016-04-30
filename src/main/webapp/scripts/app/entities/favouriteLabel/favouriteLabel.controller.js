'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteLabelController', function ($scope, $state, FavouriteLabel, FavouriteLabelSearch) {

        $scope.favouriteLabels = [];
        $scope.loadAll = function() {
            FavouriteLabel.query(function(result) {
               $scope.favouriteLabels = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FavouriteLabelSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.favouriteLabels = result;
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
            $scope.favouriteLabel = {
                liked: null,
                id: null
            };
        };
    });
