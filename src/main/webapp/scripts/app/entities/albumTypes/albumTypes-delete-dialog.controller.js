'use strict';

angular.module('therockbibleApp')
	.controller('AlbumTypesDeleteController', function($scope, $uibModalInstance, entity, AlbumTypes) {

        $scope.albumTypes = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AlbumTypes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
