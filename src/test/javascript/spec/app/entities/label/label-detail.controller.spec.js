'use strict';

describe('Controller Tests', function() {

    describe('Label Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLabel, MockBand, MockAlbum, MockUser, MockCountry;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLabel = jasmine.createSpy('MockLabel');
            MockBand = jasmine.createSpy('MockBand');
            MockAlbum = jasmine.createSpy('MockAlbum');
            MockUser = jasmine.createSpy('MockUser');
            MockCountry = jasmine.createSpy('MockCountry');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Label': MockLabel,
                'Band': MockBand,
                'Album': MockAlbum,
                'User': MockUser,
                'Country': MockCountry
            };
            createController = function() {
                $injector.get('$controller')("LabelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:labelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
