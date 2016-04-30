'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteBandController', function ($scope, $state, FavouriteBand, FavouriteBandSearch) {

        $scope.favouriteBands = [];
        $scope.loadAll = function() {
            FavouriteBand.query(function(result) {
               $scope.favouriteBands = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FavouriteBandSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.favouriteBands = result;
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
            $scope.favouriteBand = {
                liked: null,
                id: null
            };
        };
    });
