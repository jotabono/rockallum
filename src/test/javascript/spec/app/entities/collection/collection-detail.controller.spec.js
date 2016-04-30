'use strict';

describe('Controller Tests', function() {

    describe('Collection Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCollection, MockBand, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCollection = jasmine.createSpy('MockCollection');
            MockBand = jasmine.createSpy('MockBand');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Collection': MockCollection,
                'Band': MockBand,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("CollectionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:collectionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
