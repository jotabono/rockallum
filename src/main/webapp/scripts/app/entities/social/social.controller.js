'use strict';

angular.module('therockbibleApp')
    .controller('SocialController', function ($scope, $state, Social, SocialSearch) {

        $scope.socials = [];
        $scope.loadAll = function() {
            Social.query(function(result) {
               $scope.socials = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SocialSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.socials = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.social = {
                url: null,
                official: null,
                merchandising: null,
                tabs: null,
                id: null
            };
        };
    });
