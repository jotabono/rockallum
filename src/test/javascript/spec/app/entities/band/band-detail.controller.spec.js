'use strict';

describe('Controller Tests', function() {

    describe('Band Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBand, MockGenre, MockArtist, MockFavouriteBand, MockFavouriteAlbum, MockFavouriteSong, MockFavouriteLabel, MockFavouriteArtist, MockFavouriteReview, MockCollection, MockUser, MockCountry, MockLabel, MockStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBand = jasmine.createSpy('MockBand');
            MockGenre = jasmine.createSpy('MockGenre');
            MockArtist = jasmine.createSpy('MockArtist');
            MockFavouriteBand = jasmine.createSpy('MockFavouriteBand');
            MockFavouriteAlbum = jasmine.createSpy('MockFavouriteAlbum');
            MockFavouriteSong = jasmine.createSpy('MockFavouriteSong');
            MockFavouriteLabel = jasmine.createSpy('MockFavouriteLabel');
            MockFavouriteArtist = jasmine.createSpy('MockFavouriteArtist');
            MockFavouriteReview = jasmine.createSpy('MockFavouriteReview');
            MockCollection = jasmine.createSpy('MockCollection');
            MockUser = jasmine.createSpy('MockUser');
            MockCountry = jasmine.createSpy('MockCountry');
            MockLabel = jasmine.createSpy('MockLabel');
            MockStatus = jasmine.createSpy('MockStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Band': MockBand,
                'Genre': MockGenre,
                'Artist': MockArtist,
                'FavouriteBand': MockFavouriteBand,
                'FavouriteAlbum': MockFavouriteAlbum,
                'FavouriteSong': MockFavouriteSong,
                'FavouriteLabel': MockFavouriteLabel,
                'FavouriteArtist': MockFavouriteArtist,
                'FavouriteReview': MockFavouriteReview,
                'Collection': MockCollection,
                'User': MockUser,
                'Country': MockCountry,
                'Label': MockLabel,
                'Status': MockStatus
            };
            createController = function() {
                $injector.get('$controller')("BandDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:bandUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
