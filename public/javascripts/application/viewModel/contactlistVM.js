define(["application/service/contactService",
        "application/util/callback",
        "application/model/contact"], function(contactService, Callback, Contact) {
    "use strict";

    function ListVM() {
        var self = this,
            contacts = ko.observableArray(),
            reply,
        list = function(page, pageSize) {
            contactService.list(page, pageSize,
                new Callback(function(params){
                        reply = params.reply;
                        if(reply.status === "SUCCESS") {
                            contacts([]);
                            //totalPages(reply.data.totalPages);
                            for(var i = 0, lth = reply.data.list.length; i < lth; i++) {
                                var contact = reply.data.list[i];
                                addContact(contact.firstName, contact.birthDay, contact.town);
                            }
                        }
                    }, self, {}
                ),
                new Callback(function(params){
                        reply = params.reply;
                        var message = reply.responseText ? reply.responseText : reply.statusText;
                        alert(message);
                    }, self, {}
                )
            )
        },
        addContact = function(name, birthday, address) {
            contacts.push(new Contact(name, birthday, address));
        };

        return {
            list: list,
            contacts: contacts
        }
    }

    return new ListVM();

});