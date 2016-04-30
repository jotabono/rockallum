'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sex', {
                parent: 'entity',
                url: '/sexs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.sex.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sex/sexs.html',
                        controller: 'SexController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sex');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sex.detail', {
                parent: 'entity',
                url: '/sex/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.sex.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sex/sex-detail.html',
                        controller: 'SexDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sex');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Sex', function($stateParams, Sex) {
                        return Sex.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sex.new', {
                parent: 'sex',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sex/sex-dialog.html',
                        controller: 'SexDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sex', null, { reload: true });
                    }, function() {
                        $state.go('sex');
                    })
                }]
            })
            .state('sex.edit', {
                parent: 'sex',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sex/sex-dialog.html',
                        controller: 'SexDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sex', function(Sex) {
                                return Sex.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sex', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('sex.delete', {
                parent: 'sex',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sex/sex-delete-dialog.html',
                        controller: 'SexDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Sex', function(Sex) {
                                return Sex.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sex', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
