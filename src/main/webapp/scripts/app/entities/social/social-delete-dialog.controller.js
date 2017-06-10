'use strict';

angular.module('therockbibleApp')
	.controller('SocialDeleteController', function($scope, $uibModalInstance, entity, Social) {

        $scope.social = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Social.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
