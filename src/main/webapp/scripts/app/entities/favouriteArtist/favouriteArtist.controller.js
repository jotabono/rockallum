'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteArtistController', function ($scope, $state, FavouriteArtist, FavouriteArtistSearch) {

        $scope.favouriteArtists = [];
        $scope.loadAll = function() {
            FavouriteArtist.query(function(result) {
               $scope.favouriteArtists = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            FavouriteArtistSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.favouriteArtists = result;
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
            $scope.favouriteArtist = {
                liked: null,
                id: null
            };
        };
    });
