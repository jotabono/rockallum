'use strict';

angular.module('therockbibleApp')
    .controller('NavbarLateralController', function ($scope, $state, $sce, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.trusted = {};
        $scope.getPopoverData = function(s) {
            var html="";
            for (var i=0;i<s.length;i++){
                if(i == s.length-1){
                    html += '<span>'+s[i].name+'.</span>';
                } else {
                    html += '<span>' + s[i].name + ', </span>';
                }
            }

            return $scope.trusted[html] || ($scope.trusted[html] = $sce.trustAsHtml(html));
        }
    });
