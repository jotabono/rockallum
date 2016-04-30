'use strict';

angular.module('therockbibleApp').controller('AlbumTypesDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'AlbumTypes', 'Album',
        function($scope, $stateParams, $uibModalInstance, entity, AlbumTypes, Album) {

        $scope.albumTypes = entity;
        $scope.albums = Album.query();
        $scope.load = function(id) {
            AlbumTypes.get({id : id}, function(result) {
                $scope.albumTypes = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:albumTypesUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.albumTypes.id != null) {
                AlbumTypes.update($scope.albumTypes, onSaveSuccess, onSaveError);
            } else {
                AlbumTypes.save($scope.albumTypes, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
