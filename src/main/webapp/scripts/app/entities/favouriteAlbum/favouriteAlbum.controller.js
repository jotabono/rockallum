'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteAlbumController', function ($scope, $state, FavouriteAlbum, FavouriteAlbumSearch) {

        $scope.favouriteAlbums = [];
        $scope.loadAll = function() {
            FavouriteAlbum.query(function(result) {
               $scope.favouriteAlbums = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FavouriteAlbumSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.favouriteAlbums = result;
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
            $scope.favouriteAlbum = {
                liked: null,
                id: null
            };
        };
    });
