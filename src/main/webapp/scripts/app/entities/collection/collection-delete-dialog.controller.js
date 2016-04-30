'use strict';

angular.module('therockbibleApp')
	.controller('CollectionDeleteController', function($scope, $uibModalInstance, entity, Collection) {

        $scope.collection = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Collection.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
