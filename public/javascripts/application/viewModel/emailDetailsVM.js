define([
        "application/service/emailService",
        "application/util/callback"],
    function (emailService, Callback) {
        "use strict";
        function EmailDetailsVM() {
            var self = this,
                reply,
                contactsForSending = ko.observableArray(),
                template = ko.observable(),
                getDisabledState = ko.observable(null),
                getEnabledState = ko.observable('disabled'),
                getreadonlyState = ko.observable('readonly'),
                enable = function () {
                    if (getDisabledState()) {
                        getDisabledState(undefined);
                        getEnabledState('disabled');
                        getreadonlyState('readonly');

                    }
                    else {
                        getDisabledState('disabled');
                        getEnabledState(undefined);
                        getreadonlyState(undefined);

                    }
                },
                setTemplate = function (c) {
                    template(c);
                },
                reloadTemplates = function () {
                    emailService.reloadTemplates(
                        new Callback(function () {
                        }, self, {}),
                        new Callback(function (params) {
                            alert(params.reply.responseJSON.data);
                        }, self, {})
                    )
                },
                submit = function (root) {
                    var record = template(),
                        success = new Callback(function (params) {
                                var reply = params.reply;
                                if (reply.status === "SUCCESS") {
                                    //clean();
                                    root.contactListVM.currentPage(1);
                                    root.contactListVM.numbers([]);
                                    root.contactListVM.list(root.contactListVM.currentPage(), root.contactListVM.PAGE_SIZE);
                                    location.hash = "ctlst";
                                }
                            }, self, {}
                        ),
                        error = new Callback(function (params) {
                                reply = params.reply;
                                var message = reply.responseText ? reply.responseText : reply.statusText;
                                alert(message);
                            }, self, {}
                        );

                    if (record) {
                        for (var i = 0, lth = contactsForSending().length; i < lth; i++) {
                            var contact = contactsForSending()[i];
                            delete contact.street;
                            delete contact.house;
                            delete contact.town;
                            delete contact.birthday;
                            delete contact.flat;
                            delete contact.companyId;
                            delete contact.id;
                        }
                        record.adresses = ko.toJSON(contactsForSending);
                        //record.html = '';
                        emailService.sending(record, success, error);
                    } else {
                        //
                    }
                };
            return {
                template: template,
                submit: submit,
                setTemplate: setTemplate,
                contactsForSending: contactsForSending,
                getDisabledState: getDisabledState,
                enable: enable,
                getreadonlyState: getreadonlyState,
                getEnabledState: getEnabledState,
                reloadTemplates: reloadTemplates
            }
        }


        return new EmailDetailsVM();
    });


