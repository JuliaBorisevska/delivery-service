define(["application/service/authService",
    "application/model/user",
    "application/util/callback"
    ], function(authService, User, Callback) {
    "use strict";

    function LoginVM() {
        var login = ko.observable(),
            password = ko.observable(),
            loginButton = function() {
                authService.login(login,
                    password,
                    new Callback(function(params){
                        var reply = params.reply,
                            data = reply.data;
                        if(reply.status === "SUCCESS") {
                            $("body").trigger("authorized", new User(data.firstName, data.roleTitle, data.menu));
                        }
                    }, this, {}),
                    new Callback(function(params){
                        alert(params.reply.responseJSON.data);
                    }, this, {})
                );
            },
            logoutButton = function(root) {
                authService.logout(
                    new Callback(function(){
                        login("");
                        password(""); 
                        location.hash = "lgn";
                    }, this, {}),
                    new Callback(function(params) {
                        var message = params.responseText ? params.responseText : params.statusText;
                        alert(message);
                    }, this, {})
                );
            };

        return {
            login: login,
            password: password,
            loginButton: loginButton,
            logoutButton: logoutButton
        }
    }

    return new LoginVM();
});