'use strict';

angular.module('therockbibleApp')
	.controller('GenreDeleteController', function($scope, $uibModalInstance, entity, Genre) {

        $scope.genre = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Genre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
