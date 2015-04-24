
define(function() {
    "use strict";
    function User(id, firstName, lastName, middleName, role, company, login, menu, password, contactId) {
        var self = this;
        self.id = id;
        self.firstName = firstName;
        self.lastName = lastName;
        self.middleName = middleName;
        self.company = company;
        self.role = role;
        self.login = login;
        self.menu = menu;
        self.password = password;
        self.contactId = contactId;
    }

    return User;
});
