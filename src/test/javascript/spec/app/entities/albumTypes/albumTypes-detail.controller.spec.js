'use strict';

describe('Controller Tests', function() {

    describe('AlbumTypes Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAlbumTypes, MockAlbum;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAlbumTypes = jasmine.createSpy('MockAlbumTypes');
            MockAlbum = jasmine.createSpy('MockAlbum');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AlbumTypes': MockAlbumTypes,
                'Album': MockAlbum
            };
            createController = function() {
                $injector.get('$controller')("AlbumTypesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:albumTypesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
