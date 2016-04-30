'use strict';

angular.module('therockbibleApp').controller('SexDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sex', 'Artist',
        function($scope, $stateParams, $uibModalInstance, entity, Sex, Artist) {

        $scope.sex = entity;
        $scope.artists = Artist.query();
        $scope.load = function(id) {
            Sex.get({id : id}, function(result) {
                $scope.sex = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:sexUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sex.id != null) {
                Sex.update($scope.sex, onSaveSuccess, onSaveError);
            } else {
                Sex.save($scope.sex, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
