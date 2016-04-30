'use strict';

describe('Controller Tests', function() {

    describe('FavouriteArtist Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFavouriteArtist, MockBand, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFavouriteArtist = jasmine.createSpy('MockFavouriteArtist');
            MockBand = jasmine.createSpy('MockBand');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FavouriteArtist': MockFavouriteArtist,
                'Band': MockBand,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("FavouriteArtistDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:favouriteArtistUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
