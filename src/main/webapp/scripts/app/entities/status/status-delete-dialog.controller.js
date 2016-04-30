'use strict';

angular.module('therockbibleApp')
	.controller('StatusDeleteController', function($scope, $uibModalInstance, entity, Status) {

        $scope.status = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Status.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
