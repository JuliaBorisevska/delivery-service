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
                    alert("hi");
                    var record = user(),
                        success = new Callback(function (params) {
                                var reply = params.reply;
                                if (reply.status === "SUCCESS") {
                                    //clean();
                                    root.userlistVM.list(1, root.userlistVM.PAGE_SIZE);
                                    location.hash="lst";
                                }
                            }, self, {}
                        ),
                        error = new Callback(function (params) {
                                reply = params.reply;
                                var message = reply.responseText ? reply.responseText : reply.statusText;
                                alert(message);
                            }, self, {}
                        );

                    if (record.id) {
                        alert("try to update");
                        userService.update(record, success, error);
                    } else {
                        alert("try to add");
                        userService.add(record, success, error);
                    }

                }, setUser = function (c) {
                    user(c);
                }, clean = function () {
                    user(null);
                }, showModal = function (root) {
                    root.rolelistVM.list();
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
                closeModal: closeModal
            }
        }

        return new UserDetailsVM();
    });