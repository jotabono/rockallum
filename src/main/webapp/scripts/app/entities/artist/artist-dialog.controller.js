'use strict';

angular.module('therockbibleApp').controller('ArtistDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artist', 'Band', 'User', 'Country', 'Sex', 'Status',
        function($scope, $stateParams, $uibModalInstance, entity, Artist, Band, User, Country, Sex, Status) {

        $scope.artist = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.countrys = Country.query();
        $scope.sexs = Sex.query();
        $scope.statuss = Status.query();
        $scope.load = function(id) {
            Artist.get({id : id}, function(result) {
                $scope.artist = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:artistUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.artist.id != null) {
                Artist.update($scope.artist, onSaveSuccess, onSaveError);
            } else {
                Artist.save($scope.artist, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
