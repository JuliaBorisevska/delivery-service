/**
 * Created by ValentineS on 08.04.2015.
 */

define([

    "application/service/contactService",
        "application/util/callback",
        "application/model/phone"],
    function (contactService, Callback, Phone) {
    "use strict";

    function ContactDetailsVM() {
        var self = this,
            reply,
            contact = ko.observable(),

            phones = ko.observableArray([]),
            addPhone = function (p) {
                phones.push(p);
            },
            checkedPhones = ko.observableArray([]),

            submit = function(root){
                var record = contact(),
                    success = new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                clean();
                                root.contactListVM.list(root.contactListVM.currentPage(), root.contactListVM.PAGE_SIZE);
                                location.hash = "ctlst";
                            }
                        }, self, {}
                    ),
                    error = new Callback(function(params){
                            reply = params.reply;
                            var message = reply.responseText ? reply.responseText : reply.statusText;
                            alert("Error write to DB");
                        }, self, {}
                    );

                if(record.id) {
                    contactService.update(record, success, error);
                } else {
                    contactService.add(record, success, error);
                }

            },
            search = function(root){
                var record = contact(),
                    success = new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                clean();
                                root.contactListVM.list(root.contactListVM.currentPage, root.contactListVM.PAGE_SIZE);
                                location.hash = "ctlst";
                            }
                        }, self, {}
                    ),
                    error = new Callback(function(params){
                            reply = params.reply;
                            var message = reply.responseText ? reply.responseText : reply.statusText;
                            alert("Error write to DB");
                        }, self, {}
                    );
                    contactService.search(record, success, error);
            },
            setContact = function(c){
                contact(c);
            },
            clean = function(){
                contact(null);
            },
            showModalPhone = function (phone, root) {
                $('#addphone').modal({
                    keyboard: false
                });
                return true;
            };

        return {
            submit: submit,
            search: search,
            setContact: setContact,
            contact: contact,
            phones: phones,
            addPhone: addPhone,
            checkedPhones: checkedPhones,
            showModalPhone: showModalPhone
        }

    }

    return new ContactDetailsVM();
});