'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('social', {
                parent: 'entity',
                url: '/socials',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.social.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/social/socials.html',
                        controller: 'SocialController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('social');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('social.detail', {
                parent: 'entity',
                url: '/social/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.social.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/social/social-detail.html',
                        controller: 'SocialDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('social');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Social', function($stateParams, Social) {
                        return Social.get({id : $stateParams.id});
                    }]
                }
            })
            .state('social.new', {
                parent: 'social',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/social/social-dialog.html',
                        controller: 'SocialDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    url: null,
                                    official: null,
                                    merchandising: null,
                                    tabs: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('social', null, { reload: true });
                    }, function() {
                        $state.go('social');
                    })
                }]
            })
            .state('social.edit', {
                parent: 'social',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/social/social-dialog.html',
                        controller: 'SocialDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Social', function(Social) {
                                return Social.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('social', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('social.delete', {
                parent: 'social',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/social/social-delete-dialog.html',
                        controller: 'SocialDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Social', function(Social) {
                                return Social.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('social', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
