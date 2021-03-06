define(["application/service/contactService",
    "application/util/callback",
    "application/model/contact"], function (contactService, Callback, Contact) {
    "use strict";

    function ContactListVM() {
        var self = this,
            contacts = ko.observableArray(),
            contactForSearch = ko.observable(null),
            PAGE_SIZE = 3,
            SHOW_PAGES = 3,
            currentPage = ko.observable(1),
            totalPages = ko.observable(),
            checkedContact = ko.observable(),
            numbers = ko.observableArray([]),
            reply;
        var checkedContacts = ko.observableArray();
        var list = function (page, pageSize) {
        	var success = new Callback(function (params) {
                reply = params.reply;
                if (reply.status === "SUCCESS") {

                    totalPages(reply.data.totalPages);
                    if (numbers().length == 0) {
                        numbers([]);
                        for (var k = 1; k <= (totalPages() < SHOW_PAGES ? totalPages() : SHOW_PAGES); k++) {
                            numbers.push(k);
                        }
                    }
                    contacts([]);
                    for (var i = 0, lth = reply.data.list.length; i < lth; i++) {
                        var contact = reply.data.list[i];
                        contacts.push(new Contact(contact.id, contact.firstName, contact.lastName, contact.middleName,
                            contact.birthday, contact.email, contact.town, contact.street, contact.house, contact.flat,
                            contact.companyByCompanyId.id));
                    }
                }
            }, self, {}
        	);
            var error = new Callback(function(params){
         	   if(contactForSearch()!=null) {
                    location.hash="ctsearch";
                }
         	  alert(params.reply.responseJSON.data);
         	  //alert("ctlst something wrong...");
               }, self, {} 
            );
            if(contactForSearch()!=null) {
            	contactService.search(page, pageSize, contactForSearch(), success, error);
            	location.hash="ctlst";
            }else{
            	contactService.list(page, pageSize, success, error);
            }
        };

        var goToDetails = function (contact, event, root) {

            contactService.get(contact.id, new Callback(function (params) {
                        reply = params.reply;
                        if (reply.status === "SUCCESS") {
                            var contact = reply.data;
                            root.contactDetailsVM.setContact(
                                new Contact(contact.id, contact.firstName, contact.lastName, contact.middleName,
                                    contact.birthday, contact.email, contact.town, contact.street, contact.house, contact.flat,
                                    contact.companyByCompanyId.id)
                            );
                            location.hash = "ctchange";
                        }
                    }, self, {}
                ),
                new Callback(function (params) {
                        reply = params.reply;
                        var message = reply.responseText ? reply.responseText : reply.statusText;
                        alert("get contact error! contactlistVM");
                    }, self, {}
                )
            );

        };

        var deleteContacts = function () {
            if (checkedContacts().length < 1) {
                alert("Контакты не выбраны");
                return;
            }
            var checkedContactsIds = [];
            for (var i = 0, l = checkedContacts().length; i < l; i++) {
                checkedContactsIds.push(checkedContacts()[i].id);
            }
            contactService.remove(checkedContactsIds,
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
            );
            checkedContacts([]);
        };

        var goToMailSending = function (data, event, root) {
            root.emailDetailsVM.contactsForSending([]);

            for (var i = 0, l = checkedContacts().length; i < l; i++) {
                if (checkedContacts()[i].email) {
                    root.emailDetailsVM.contactsForSending.push(checkedContacts()[i]);
                }
            }
            if (root.emailDetailsVM.contactsForSending().length < 1) {
                alert("Контакты не выбраны или у них отсутствует email");
                return;
            }
            root.emaillistVM.list();
            checkedContacts([]);

            location.hash = "email";
        };

        var setContactForSearch = function(c){
        	contactForSearch(c);
        };
        
        return {
            contacts: contacts,
            numbers: numbers,
            list: list,
            deleteContacts: deleteContacts,
            goToMailSending: goToMailSending,
            checkedContacts: checkedContacts,
            checkedContact: checkedContact,
            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE,
            SHOW_PAGES: SHOW_PAGES,
            contactForSearch: contactForSearch,
            setContactForSearch: setContactForSearch,
            goToDetails: goToDetails
        }
    }

    return new ContactListVM();

});