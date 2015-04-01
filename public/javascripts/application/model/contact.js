define(function() {
    "use strict";
    function Contact (firstName, /*lastName, middleName,*/ birthday, town) {
        var self = this;
        self.firstName = firstName;
        /*self.lastName = lastName;
        self.middleName = middleName;*/
        self.birthday = birthday;
        self.town = town;
    }

    return Contact;
});