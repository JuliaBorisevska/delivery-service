/**
 * Created by ValentineS on 08.04.2015.
 */

define([
    "application/service/contactService",
    "application/util/callback"],
    function(contactService, Callback) {
    "use strict";

    function ContactDetailsVM() {
        var self = this,
            reply,
            contact = ko.observable(),
            submit = function(root){

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
                            alert("contactDetailsVM error!");
                        }, self, {}
                    );

                if(record.id) {
                    contactService.update(record, success, error);
                } else {
                    contactService.add(record, success, error);
                }

            },
            setContact = function(c){
                contact(c);
                debugger;
            },
            clean = function(){
                contact(null);
            };

        return {
            submit: submit,
            setContact: setContact,
            contact: contact
        }
    }

    return new ContactDetailsVM();
});