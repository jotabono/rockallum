'use strict';

angular.module('therockbibleApp').controller('GenreDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Genre', 'Band',
        function($scope, $stateParams, $uibModalInstance, entity, Genre, Band) {

        $scope.genre = entity;
        $scope.bands = Band.query();
        $scope.load = function(id) {
            Genre.get({id : id}, function(result) {
                $scope.genre = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:genreUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.genre.id != null) {
                Genre.update($scope.genre, onSaveSuccess, onSaveError);
            } else {
                Genre.save($scope.genre, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
