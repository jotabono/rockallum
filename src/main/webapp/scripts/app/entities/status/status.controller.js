'use strict';

angular.module('therockbibleApp')
    .controller('StatusController', function ($scope, $state, Status, StatusSearch) {

        $scope.statuss = [];
        $scope.loadAll = function() {
            Status.query(function(result) {
               $scope.statuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            StatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.statuss = result;
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
            $scope.status = {
                status: null,
                id: null
            };
        };
    });
