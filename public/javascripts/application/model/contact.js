define(function() {
    "use strict";

    function Contact (firstName, lastName, middleName, birthday, town, street, house, flat) {
        var self = this;
        self.firstName = firstName;
        self.lastName = lastName;
        self.middleName = middleName;
        self.birthday = birthday;
        self.town = town;
        self.street = street;
        self.house = house;
        self.flat = flat;
    }

    return Contact;
});