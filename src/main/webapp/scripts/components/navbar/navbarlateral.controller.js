'use strict';

angular.module('therockbibleApp')
    .controller('NavbarLateralController', function ($scope, $state, $sce, $rootScope, Principal, Auth, User, Upload, Language) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        var unsubscribe = $rootScope.$on('therockbibleApp:userUpdate', function(event, result) {
            $scope.users = User.query();
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

        $scope.save = function () {
            Auth.updateAccount($scope.account).then(function() {
                $('.fotoprofile').attr('src', '/uploads/'+$scope.account.userPicture+'.jpg');
                $('.fotoprofile2').attr('src', '/uploads/'+$scope.account.userPicture+'.jpg');
                $('.backgroundblurdiv').attr('src', '/uploads/'+$scope.account.userPicture+'.jpg');
                $scope.error = null;
                $scope.success = 'OK';
                Language.getCurrent().then(function(current) {
                    if ($scope.account.langKey !== current) {
                        $translate.use($scope.account.langKey);
                    }
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };

        $scope.$watch('profilepic', function(){
            $scope.uploadPic($scope.profilepic);
            if($scope.profilepic != undefined){
                $rootScope.$broadcast("BackgroundSettings nuevo",$scope.account);
            }
        });

        $scope.uploadPic = function(file) {
            var namepic = $scope.account.firstName+$scope.account.login;
            Upload.upload({
                url: 'api/upload',
                data: {file: file, 'name': $scope.account.firstName+$scope.account.login},
            }).then(function (response) {
                    $scope.account.userPicture = namepic;
                    $scope.save();
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            });
        }
    });
