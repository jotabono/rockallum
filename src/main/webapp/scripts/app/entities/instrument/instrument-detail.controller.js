'use strict';

angular.module('therockbibleApp')
    .controller('InstrumentDetailController', function ($scope, $rootScope, $stateParams, entity, Instrument, Artist) {
        $scope.instrument = entity;
        $scope.load = function (id) {
            Instrument.get({id: id}, function(result) {
                $scope.instrument = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:instrumentUpdate', function(event, result) {
            $scope.instrument = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
