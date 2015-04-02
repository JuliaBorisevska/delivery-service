define(["application/service/contactService",
        "application/util/callback",
        "application/model/contact"], function(contactService, Callback, Contact) {
    "use strict";

    function ContactListVM() {
        var self = this,
            contacts = ko.observableArray(),
            PAGE_SIZE = 3,
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
                                contacts.push(new Contact(contact.firstName, contact.lastName, contact.middleName,
                                    contact.birthday, contact.town, contact.street, contact.house, contact.flat));
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
        };
        
        var goToDetails = function(root) {
                    location.hash = "ctadd";
        };

        return {
            contacts: contacts,
            list: list,
            PAGE_SIZE: PAGE_SIZE,
            goToDetails: goToDetails
        }
    }

    return new ContactListVM();

});