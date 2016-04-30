'use strict';

angular.module('therockbibleApp')
    .controller('CountryDetailController', function ($scope, $rootScope, $stateParams, entity, Country, Band, Label, Artist) {
        $scope.country = entity;
        $scope.load = function (id) {
            Country.get({id: id}, function(result) {
                $scope.country = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:countryUpdate', function(event, result) {
            $scope.country = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
