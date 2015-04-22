/**
 * Created by antonkw on 11.04.2015.
 */
define([
        "application/service/userService",
        "application/util/callback"],
    function (userService, Callback) {
        "use strict";

        function UserDetailsVM() {
            var self = this,

                reply,

                user = ko.observable(),
                submit = function (root) {
                    var record = user(),
                        success = new Callback(function (params) {
                                var reply = params.reply;
                                if (reply.status === "SUCCESS") {
                                    clean();
                                    alert("ke9999");
                                    root.userlistVM.list(1, root.userlistVM.PAGE_SIZE);
                                    root.goTo("ctlst");
                                }
                            }, self, {}
                        ),
                        error = new Callback(function (params) {
                                reply = params.reply;
                                var message = reply.responseText ? reply.responseText : reply.statusText;
                                alert(message);
                            }, self, {}
                        );

                    //if (record.id) {
                    //    userService.update(record, success, error);
                    //} else {
                    //    userService.add(record, success, error);
                    //}

                }, setUser = function (c) {
                    user(c);
                }, clean = function () {
                    user(null);
                }, showModal = function () {
                    userService.showModal();
                }, closeModal = function () {
                    userService.closeModal();
                }, availableRoles = userService.availableRoles;


            return {
                submit: submit,
                setUser: setUser,
                user: user,
                showModal: showModal,
                closeModal: closeModal,
                availableRoles: availableRoles
            }
        }

        return new UserDetailsVM();
    });