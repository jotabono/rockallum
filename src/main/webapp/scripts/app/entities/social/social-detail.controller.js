'use strict';

angular.module('therockbibleApp')
    .controller('SocialDetailController', function ($scope, $rootScope, $stateParams, entity, Social, Artist) {
        $scope.social = entity;
        $scope.load = function (id) {
            Social.get({id: id}, function(result) {
                $scope.social = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:socialUpdate', function(event, result) {
            $scope.social = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
