'use strict';

angular.module('therockbibleApp').controller('FavouriteBandDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavouriteBand', 'Band', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, FavouriteBand, Band, User) {

        $scope.favouriteBand = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            FavouriteBand.get({id : id}, function(result) {
                $scope.favouriteBand = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:favouriteBandUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.favouriteBand.id != null) {
                FavouriteBand.update($scope.favouriteBand, onSaveSuccess, onSaveError);
            } else {
                FavouriteBand.save($scope.favouriteBand, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
