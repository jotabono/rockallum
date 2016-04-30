'use strict';

describe('Controller Tests', function() {

    describe('Artist Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockArtist, MockBand, MockUser, MockCountry, MockSex, MockStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockArtist = jasmine.createSpy('MockArtist');
            MockBand = jasmine.createSpy('MockBand');
            MockUser = jasmine.createSpy('MockUser');
            MockCountry = jasmine.createSpy('MockCountry');
            MockSex = jasmine.createSpy('MockSex');
            MockStatus = jasmine.createSpy('MockStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Artist': MockArtist,
                'Band': MockBand,
                'User': MockUser,
                'Country': MockCountry,
                'Sex': MockSex,
                'Status': MockStatus
            };
            createController = function() {
                $injector.get('$controller')("ArtistDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:artistUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
