define(["application/service/baseService"], function(baseService) {
    function AuthService(){


        var login = function(user, password, success, error, done) {
            baseService.send(
                "/login",
                "POST",
                {user: user, password: password},
                success,
                error,
                done
            );
        };

        var logout = function(success, error, done){
            baseService.send(
                "/logout",
                "POST",
                {},
                success,
                error,
                done
            );
        };

        var reloadSettings = function (success, error, done) {
            baseService.send(
                "/settings",
                "GET",
                {},
                success,
                error,
                done
            );
        };

        return {
            login: login,
            logout: logout,
            reloadSettings: reloadSettings
        }
    }

    return new AuthService();
});