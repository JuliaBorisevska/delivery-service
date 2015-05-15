define(["application/service/baseService"], function(baseService) {
    function InitService(){


        var initSections = function(success, error, done) {
            baseService.send(
                "/init",
                "GET",
                {},
                success,
                error,
                done
            );
        };
        
        var getUser = function(success, error, done) {
            baseService.send(
                "/user",
                "GET",
                {},
                success,
                error,
                done
            );
        };

        return {
        	initSections: initSections,
        	getUser: getUser
        }
    }

    return new InitService();
});