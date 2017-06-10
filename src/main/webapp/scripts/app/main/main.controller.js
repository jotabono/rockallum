'use strict';

angular.module('therockbibleApp')
    .controller('MainController', function ($scope, $state, $sce, $http, Principal, Band, BandSearch, ParseLinks, FavouriteBand) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.results = [];
        $scope.query = "";

        $scope.search = function (query) {
            $http({
                method: 'GET',
                url: '/api/_search/'+query
            }).then(function successCallback(response) {
                $scope.results = response.data;
            }, function errorCallback(response) {

            });
        }


        $scope.bands = [];
        $scope.predicate = 'id';
        $scope.reverse = false;
        $scope.page = 1;
        $scope.loadAll = function() {
            Band.getBandsLiked({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.bands = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.band = {
                name: null,
                location: null,
                latitude: null,
                longitude: null,
                foundingDate: null,
                yearsActive: null,
                lyricalThemes: null,
                independent: null,
                picture: null,
                links: null,
                logo: null,
                bio: null,
                id: null
            };
        };

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

        $scope.like = function(id){
            FavouriteBand.addLike({id: id},{}, successLike);
        }

        var successLike = function(result) {
            for (var k = 0; k < $scope.bands.length; k++) {
                if ($scope.bands[k].band.id == result.band.id) {
                    $scope.bands[k].liked = result.liked;
                }
            }
        }
    });
