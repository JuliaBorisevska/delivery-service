define(function() {
    "use strict";

    function Contact(id, firstName, lastName, middleName, birthday, email, town, street, house, flat, companyId, phones, dateMax) {
        var self = this;
        self.id = id;
        self.firstName = firstName;
        self.lastName = lastName;
        self.middleName = middleName;
        self.birthday = birthday;
        self.email = email;
        self.town = town;
        self.street = street;
        self.house = house;
        self.flat = flat;
        self.companyId = companyId;
        self.phones = phones;
        self.dateMax = dateMax;
    }

    return Contact;
});