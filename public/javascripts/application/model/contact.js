define(function() {
    "use strict";

    function Contact(id, firstName, lastName, middleName, birthday, email, town, street, house, flat, companyId, phones, dateMax) {
        var self = this;
        self.id = id;
        self.firstName = ko.observable(firstName).extend({
            required: {
                onlyIf: function() {return self.id != -1;}
              }
            });
        self.lastName = ko.observable(lastName).extend({ required: true, maxLength: 30 });
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