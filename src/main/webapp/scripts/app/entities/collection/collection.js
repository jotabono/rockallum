'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('collection', {
                parent: 'entity',
                url: '/collections',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.collection.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/collection/collections.html',
                        controller: 'CollectionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('collection');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('collection.detail', {
                parent: 'entity',
                url: '/collection/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.collection.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/collection/collection-detail.html',
                        controller: 'CollectionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('collection');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Collection', function($stateParams, Collection) {
                        return Collection.get({id : $stateParams.id});
                    }]
                }
            })
            .state('collection.new', {
                parent: 'collection',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/collection/collection-dialog.html',
                        controller: 'CollectionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    catched: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('collection', null, { reload: true });
                    }, function() {
                        $state.go('collection');
                    })
                }]
            })
            .state('collection.edit', {
                parent: 'collection',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/collection/collection-dialog.html',
                        controller: 'CollectionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Collection', function(Collection) {
                                return Collection.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('collection', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('collection.delete', {
                parent: 'collection',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/collection/collection-delete-dialog.html',
                        controller: 'CollectionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Collection', function(Collection) {
                                return Collection.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('collection', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
