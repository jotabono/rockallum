'use strict';

angular.module('therockbibleApp')
	.controller('BandDeleteController', function($scope, $uibModalInstance, entity, Band) {

        $scope.band = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Band.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
