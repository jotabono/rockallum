'use strict';

angular.module('therockbibleApp')
    .controller('SexController', function ($scope, $state, Sex, SexSearch) {

        $scope.sexs = [];
        $scope.loadAll = function() {
            Sex.query(function(result) {
               $scope.sexs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SexSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.sexs = result;
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
            $scope.sex = {
                name: null,
                id: null
            };
        };
    });
