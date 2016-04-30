'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteSongController', function ($scope, $state, FavouriteSong, FavouriteSongSearch) {

        $scope.favouriteSongs = [];
        $scope.loadAll = function() {
            FavouriteSong.query(function(result) {
               $scope.favouriteSongs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FavouriteSongSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.favouriteSongs = result;
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
            $scope.favouriteSong = {
                liked: null,
                id: null
            };
        };
    });
