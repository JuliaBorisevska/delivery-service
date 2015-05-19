/**
 * Created by antonkw on 11.04.2015.
 */
define([
        "application/service/userService",
        "application/util/callback",
        "application/model/role",
        "application/model/user"],
    function (userService, Callback, Role, User) {
        "use strict";

        function UserDetailsVM() {
            var self = this,

                reply,
                role = ko.observable(new Role()),
                setRole = function (c) {
                    role(c);
                },

                user = ko.observable(new User()),
                submit = function (root) {
                    var record = user(),
                        success = new Callback(function (params) {
                                var reply = params.reply;
                                if (reply.status === "SUCCESS") {
                                    //clean();
                                	root.userlistVM.currentPage(1);
                                    root.userlistVM.numbers([]);
                                    root.userlistVM.list(root.userlistVM.currentPage(), root.userlistVM.PAGE_SIZE);
                                    location.hash="lst";
                                }
                            }, self, {}
                        ),
                        error = new Callback(function (params) {
                                reply = params.reply;
                                var message = reply.responseText ? reply.responseText : reply.statusText;
                                alert("error");
                            }, self, {}
                        );

                    if (record.id) {

                        record.role = role();

                        userService.update(record, success, error);
                    } else {


                        userService.add(record, success, error);
                    }

                }, setUser = function (c) {
                    user(c);
                }, clean = function () {
                    user(null);
                }, showModal = function (root) {
                    root.contactListVM.currentPage(1);
                    root.contactListVM.numbers([]);
                    root.contactListVM.list(root.contactListVM.currentPage(), root.contactListVM.PAGE_SIZE);

                    $('#select-contact').modal({
                        keyboard: false
                    });
                    return true;
                }, closeModal = function () {
                    $('#select-contact').modal('hide');
                    return true;
                };


            return {
                submit: submit,
                setUser: setUser,
                user: user,
                showModal: showModal,
                closeModal: closeModal,
                role: role,
                setRole: setRole

            }
        }

        return new UserDetailsVM();
    });