'use strict';

angular.module('therockbibleApp')
	.controller('SongDeleteController', function($scope, $uibModalInstance, entity, Song) {

        $scope.song = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Song.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
