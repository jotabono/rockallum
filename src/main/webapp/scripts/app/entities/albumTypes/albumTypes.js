'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('albumTypes', {
                parent: 'entity',
                url: '/albumTypess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.albumTypes.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/albumTypes/albumTypess.html',
                        controller: 'AlbumTypesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('albumTypes');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('albumTypes.detail', {
                parent: 'entity',
                url: '/albumTypes/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.albumTypes.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/albumTypes/albumTypes-detail.html',
                        controller: 'AlbumTypesDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('albumTypes');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AlbumTypes', function($stateParams, AlbumTypes) {
                        return AlbumTypes.get({id : $stateParams.id});
                    }]
                }
            })
            .state('albumTypes.new', {
                parent: 'albumTypes',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/albumTypes/albumTypes-dialog.html',
                        controller: 'AlbumTypesDialogController',
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
                        $state.go('albumTypes', null, { reload: true });
                    }, function() {
                        $state.go('albumTypes');
                    })
                }]
            })
            .state('albumTypes.edit', {
                parent: 'albumTypes',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/albumTypes/albumTypes-dialog.html',
                        controller: 'AlbumTypesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AlbumTypes', function(AlbumTypes) {
                                return AlbumTypes.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('albumTypes', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('albumTypes.delete', {
                parent: 'albumTypes',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/albumTypes/albumTypes-delete-dialog.html',
                        controller: 'AlbumTypesDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AlbumTypes', function(AlbumTypes) {
                                return AlbumTypes.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('albumTypes', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
