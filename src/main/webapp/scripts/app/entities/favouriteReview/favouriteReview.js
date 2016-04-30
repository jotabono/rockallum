'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('favouriteReview', {
                parent: 'entity',
                url: '/favouriteReviews',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteReview.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteReview/favouriteReviews.html',
                        controller: 'FavouriteReviewController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteReview');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('favouriteReview.detail', {
                parent: 'entity',
                url: '/favouriteReview/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteReview.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteReview/favouriteReview-detail.html',
                        controller: 'FavouriteReviewDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteReview');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FavouriteReview', function($stateParams, FavouriteReview) {
                        return FavouriteReview.get({id : $stateParams.id});
                    }]
                }
            })
            .state('favouriteReview.new', {
                parent: 'favouriteReview',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteReview/favouriteReview-dialog.html',
                        controller: 'FavouriteReviewDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    liked: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteReview', null, { reload: true });
                    }, function() {
                        $state.go('favouriteReview');
                    })
                }]
            })
            .state('favouriteReview.edit', {
                parent: 'favouriteReview',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteReview/favouriteReview-dialog.html',
                        controller: 'FavouriteReviewDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FavouriteReview', function(FavouriteReview) {
                                return FavouriteReview.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteReview', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('favouriteReview.delete', {
                parent: 'favouriteReview',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteReview/favouriteReview-delete-dialog.html',
                        controller: 'FavouriteReviewDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FavouriteReview', function(FavouriteReview) {
                                return FavouriteReview.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteReview', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
