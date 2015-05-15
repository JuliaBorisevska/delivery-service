define(["application/service/userService",
    "application/util/callback",
    "application/model/user"], function (userService, Callback, User) {
    "use strict";

    function UserListVM() {
        var k;
        var PAGE_SIZE = 2,
            SHOW_PAGES = 3,
            currentPage = ko.observable(1),
            totalPages = ko.observable(),
            reply;
        var self = this,
            users = ko.observableArray(),
            selectedRole = ko.observable(""),
            numbers = ko.observableArray([]);
        var checkedUsers = ko.observableArray();
        var checkedUserId = ko.observable();
        var list = function (page, pageSize) {
            userService.list(page, pageSize, selectedRole(),
                new Callback(function (params) {
                    reply = params.reply;
                    if (reply.status === "SUCCESS") {
                        users([]);
                        totalPages(reply.data.totalPages);
                        if (numbers().length == 0) {
                            numbers([]);
                            for (k = 1; k <= (totalPages() < SHOW_PAGES ? totalPages() : SHOW_PAGES); k++) {
                                numbers.push(k);
                            }
                        }
                        for (var i = 0, lth = reply.data.list.length; i < lth; i++) {
                            var user = reply.data.list[i];
                            users.push(new User(user.id, user.firstName, user.lastName, user.middleName, user.roleTitle, user.companyTitle, user.login, user.menu, user.password, user.contactId));
                        }
                    }
                }, self, {}),
                new Callback(function (params) {
                    alert(params.reply.responseJSON.data);
                }, self, {})
            )
        };
        var deleteUsers = function () {
            if (checkedUsers().length < 1) {
                alert("Пользователи не выбраны");
                return;
            }
            //alert(checkedUsers());
            userService.remove(checkedUsers(),
                new Callback(function (params) {
                        reply = params.reply;
                        if (reply.status === "SUCCESS") {
                            currentPage(1);
                            numbers([]);
                            list(currentPage(), PAGE_SIZE);
                        }
                    }, self, {}
                ),
                new Callback(function (params) {
                    alert(params.reply.responseJSON.data);
                }, self, {})
            )
            checkedUsers([]);
        };

        var goToUserDetails = function (data, event, root) {
            root.rolelistVM.list();

            root.userDetailsVM.setUser(new User(data.id, data.firstName, data.lastName, data.middleName, data.company, data.role, data.login, data.menu, data.password, data.contactId));
            root.userDetailsVM.user().role = data.role;
            alert(Object.keys(data));
            root.contactListVM.checkedContact = data.contactId;
            alert(root.contactListVM.checkedContact);
            location.hash = "userchange";
        };

        return {
            users: users,
            checkedUsers: checkedUsers,
            checkedUserId: checkedUserId,
            selectedRole: selectedRole,
            numbers: numbers,
            list: list,
            goToUserDetails: goToUserDetails,
            deleteUsers: deleteUsers,
            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE,
            SHOW_PAGES: SHOW_PAGES
        }
    }

    return new UserListVM();

});

