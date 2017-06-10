'use strict';

angular.module('therockbibleApp')
    .controller('InstrumentController', function ($scope, $state, Instrument, InstrumentSearch) {

        $scope.instruments = [];
        $scope.loadAll = function() {
            Instrument.query(function(result) {
               $scope.instruments = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            InstrumentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.instruments = result;
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
            $scope.instrument = {
                instrument: null,
                id: null
            };
        };
    });
