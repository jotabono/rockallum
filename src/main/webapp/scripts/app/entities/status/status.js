'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('status', {
                parent: 'entity',
                url: '/statuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.status.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/status/statuss.html',
                        controller: 'StatusController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('status.detail', {
                parent: 'entity',
                url: '/status/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.status.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/status/status-detail.html',
                        controller: 'StatusDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Status', function($stateParams, Status) {
                        return Status.get({id : $stateParams.id});
                    }]
                }
            })
            .state('status.new', {
                parent: 'status',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/status/status-dialog.html',
                        controller: 'StatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('status', null, { reload: true });
                    }, function() {
                        $state.go('status');
                    })
                }]
            })
            .state('status.edit', {
                parent: 'status',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/status/status-dialog.html',
                        controller: 'StatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Status', function(Status) {
                                return Status.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('status', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('status.delete', {
                parent: 'status',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/status/status-delete-dialog.html',
                        controller: 'StatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Status', function(Status) {
                                return Status.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('status', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
