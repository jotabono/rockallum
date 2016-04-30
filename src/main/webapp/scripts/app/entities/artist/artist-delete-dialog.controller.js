'use strict';

angular.module('therockbibleApp')
	.controller('ArtistDeleteController', function($scope, $uibModalInstance, entity, Artist) {

        $scope.artist = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Artist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
