'use strict';

angular.module('therockbibleApp').controller('LabelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Label', 'Band', 'Album', 'User', 'Country',
        function($scope, $stateParams, $uibModalInstance, entity, Label, Band, Album, User, Country) {

        $scope.label = entity;
        $scope.bands = Band.query();
        $scope.albums = Album.query();
        $scope.users = User.query();
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            Label.get({id : id}, function(result) {
                $scope.label = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:labelUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.label.id != null) {
                Label.update($scope.label, onSaveSuccess, onSaveError);
            } else {
                Label.save($scope.label, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
