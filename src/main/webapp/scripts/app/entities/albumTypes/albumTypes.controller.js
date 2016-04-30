'use strict';

angular.module('therockbibleApp')
    .controller('AlbumTypesController', function ($scope, $state, AlbumTypes, AlbumTypesSearch) {

        $scope.albumTypess = [];
        $scope.loadAll = function() {
            AlbumTypes.query(function(result) {
               $scope.albumTypess = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            AlbumTypesSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.albumTypess = result;
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
            $scope.albumTypes = {
                name: null,
                id: null
            };
        };
    });
