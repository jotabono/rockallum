'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('artist', {
                parent: 'entity',
                url: '/artists',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.artist.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artist/artists.html',
                        controller: 'ArtistController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('artist');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('artist.detail', {
                parent: 'entity',
                url: '/artist/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.artist.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artist/artist-detail.html',
                        controller: 'ArtistDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('artist');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Artist', function($stateParams, Artist) {
                        return Artist.get({id : $stateParams.id});
                    }]
                }
            })
            .state('artist.new', {
                parent: 'artist',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artist/artist-dialog.html',
                        controller: 'ArtistDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    realName: null,
                                    bornIn: null,
                                    age: null,
                                    bio: null,
                                    role: null,
                                    yearsActive: null,
                                    links: null,
                                    picture: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('artist', null, { reload: true });
                    }, function() {
                        $state.go('artist');
                    })
                }]
            })
            .state('artist.edit', {
                parent: 'artist',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artist/artist-dialog.html',
                        controller: 'ArtistDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Artist', function(Artist) {
                                return Artist.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('artist', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('artist.delete', {
                parent: 'artist',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artist/artist-delete-dialog.html',
                        controller: 'ArtistDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Artist', function(Artist) {
                                return Artist.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('artist', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
