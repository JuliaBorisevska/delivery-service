define(function() {
    "use strict";
    function Contact (name, birthday, address) {
        var self = this;
        self.name = name;
        self.birthday = birthday;
        self.address = address;
    }

    return Contact;
});