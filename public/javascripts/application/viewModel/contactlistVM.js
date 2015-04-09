define(["application/service/contactService",
        "application/util/callback",
        "application/model/contact"], function(contactService, Callback, Contact) {
    "use strict";

    function ContactListVM() {
        var self = this,
            contacts = ko.observableArray(),

            PAGE_SIZE = 3,
            SHOW_PAGES = 3,
            currentPage = ko.observable(1),
            totalPages = ko.observable(),
            numbers = ko.observableArray([]),

            reply,
        list = function(page, pageSize) {
            contactService.list(page, pageSize,
                new Callback(function(params){
                        reply = params.reply;
                        if(reply.status === "SUCCESS") {

                            totalPages(reply.data.totalPages);
                            if (numbers().length==0){
                                numbers([]);
                                for (var k=1; k<=(totalPages()<SHOW_PAGES?totalPages():SHOW_PAGES); k++){
                                    numbers.push(k);
                                }
                            }

                            contacts([]);
                            for(var i = 0, lth = reply.data.list.length; i < lth; i++) {
                                var contact = reply.data.list[i];
                                contacts.push(new Contact(contact.id, contact.firstName, contact.lastName, contact.middleName,
                                    contact.birthday, contact.town, contact.street, contact.house, contact.flat,
                                    contact.companyByCompanyId.id));
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
        
        var goToDetails = function(contact, event, root) {

            location.hash = "ctadd";

            contactService.get(contact.id,
                new Callback(function(params){
                        reply = params.reply;
                        if(reply.status === "SUCCESS") {
                            var contact = reply.data;
                            root.contactDetailsVM.setContact(
                                new Contact(contact.id, contact.firstName, contact.lastName, contact.middleName,
                                    contact.birthday, contact.town, contact.street, contact.house, contact.flat,
                                    contact.companyByCompanyId.id)
                            );
                            root.goTo("ctlst");
                        }
                    }, self, {}
                ),
                new Callback(function(params){
                        reply = params.reply;
                        var message = reply.responseText ? reply.responseText : reply.statusText;
                        alert(message);
                    }, self, {}
                )
            );

        };

        return {
            contacts: contacts,
            numbers: numbers,
            list: list,

            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE,
            SHOW_PAGES: SHOW_PAGES,

            goToDetails: goToDetails
        }
    }

    return new ContactListVM();

});