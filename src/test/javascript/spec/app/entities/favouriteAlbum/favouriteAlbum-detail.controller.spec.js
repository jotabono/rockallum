'use strict';

describe('Controller Tests', function() {

    describe('FavouriteAlbum Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFavouriteAlbum, MockBand, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFavouriteAlbum = jasmine.createSpy('MockFavouriteAlbum');
            MockBand = jasmine.createSpy('MockBand');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FavouriteAlbum': MockFavouriteAlbum,
                'Band': MockBand,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("FavouriteAlbumDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:favouriteAlbumUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
