'use strict';

angular.module('therockbibleApp')
	.controller('SexDeleteController', function($scope, $uibModalInstance, entity, Sex) {

        $scope.sex = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Sex.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
