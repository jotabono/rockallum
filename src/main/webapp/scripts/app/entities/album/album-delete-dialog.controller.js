'use strict';

angular.module('therockbibleApp')
	.controller('AlbumDeleteController', function($scope, $uibModalInstance, entity, Album) {

        $scope.album = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Album.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
