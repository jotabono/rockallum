'use strict';

angular.module('therockbibleApp')
    .controller('CollectionController', function ($scope, $state, Collection, CollectionSearch) {

        $scope.collections = [];
        $scope.loadAll = function() {
            Collection.query(function(result) {
               $scope.collections = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CollectionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.collections = result;
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
            $scope.collection = {
                catched: null,
                id: null
            };
        };
    });
