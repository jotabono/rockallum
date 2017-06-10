'use strict';

angular.module('therockbibleApp').controller('InstrumentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Instrument', 'Artist',
        function($scope, $stateParams, $uibModalInstance, entity, Instrument, Artist) {

        $scope.instrument = entity;
        $scope.artists = Artist.query();
        $scope.load = function(id) {
            Instrument.get({id : id}, function(result) {
                $scope.instrument = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:instrumentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.instrument.id != null) {
                Instrument.update($scope.instrument, onSaveSuccess, onSaveError);
            } else {
                Instrument.save($scope.instrument, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
