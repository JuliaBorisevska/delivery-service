define(["application/service/userService",
        "application/util/callback",
        "application/model/user"], function(userService, Callback, User) {
    "use strict";

    function UserListVM() {
        var self = this,
            users = ko.observableArray(),
            PAGE_SIZE = 3,
            currentPage = ko.observable(1),
            totalPages = ko.observable(),
            reply,
            list = function(page, pageSize) {
                userService.list(page, pageSize,
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                users([]);
                                totalPages(reply.data.totalPages);
                                for(var i = 0, lth = reply.data.list.length; i < lth; i++) {
                                    var user = reply.data.list[i];
                                    users.push(new User(user.id,user.firstName, user.lastName, user.middleName, user.roleTitle, user.companyTitle, user.login, user.menu));
                                }
                            }
                        }, self, {}),
                    new Callback(function(params){
                            reply = params.reply;
                            var message = reply.responseText ? reply.responseText : reply.statusText;
                            alert(message);
                        }, self, {})
                )
            };

        return {
            users: users,
            list: list,
            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE
        }
    }

    return new UserListVM();

});