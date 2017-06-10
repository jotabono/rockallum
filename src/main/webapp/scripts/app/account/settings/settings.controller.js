'use strict';

angular.module('therockbibleApp')
    .controller('SettingsController', function ($scope, $rootScope, Principal, Auth, Language, $translate) {
        $scope.success = null;
        $scope.error = null;
        Principal.identity().then(function(account) {
            $scope.settingsAccount = copyAccount(account);
        });

/*        $rootScope.$on("BackgroundSettings nuevo", function (e,res){
            var timestamp = new Date().getTime();
            $('.backgroundblurdiv').prop('src', '/uploads/' + res.userPicture + '.jpg?'+ Math.random());
            console.log(res);
        });*/

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity(true).then(function(account) {
                    $scope.settingsAccount = copyAccount(account);
                });
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            }
        }
    });
