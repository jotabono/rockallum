'use strict';

angular.module('therockbibleApp')
	.controller('InstrumentDeleteController', function($scope, $uibModalInstance, entity, Instrument) {

        $scope.instrument = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Instrument.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
