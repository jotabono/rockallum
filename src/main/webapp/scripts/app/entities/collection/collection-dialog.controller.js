'use strict';

angular.module('therockbibleApp').controller('CollectionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Collection', 'Band', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Collection, Band, User) {

        $scope.collection = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Collection.get({id : id}, function(result) {
                $scope.collection = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:collectionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.collection.id != null) {
                Collection.update($scope.collection, onSaveSuccess, onSaveError);
            } else {
                Collection.save($scope.collection, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
