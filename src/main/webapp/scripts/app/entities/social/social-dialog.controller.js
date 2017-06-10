'use strict';

angular.module('therockbibleApp').controller('SocialDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Social', 'Artist',
        function($scope, $stateParams, $uibModalInstance, entity, Social, Artist) {

        $scope.social = entity;
        $scope.artists = Artist.query();
        $scope.load = function(id) {
            Social.get({id : id}, function(result) {
                $scope.social = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:socialUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.social.id != null) {
                Social.update($scope.social, onSaveSuccess, onSaveError);
            } else {
                Social.save($scope.social, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
