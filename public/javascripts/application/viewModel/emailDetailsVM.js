define(["application/util/callback"
    ], function(Callback) {
    "use strict";

    function EmailDetailsVM() {
        var contactsForSending = ko.observableArray();
            

        return {
            contactsForSending: contactsForSending
        }
    }

    return new EmailDetailsVM();
});