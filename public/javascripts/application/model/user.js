
define(function() {
    "use strict";
    function User(id, firstName, lastName, middleName, role, company, login, menu, password) {
        var self = this;
        self.id = id;
        self.firstName = firstName;
        self.lastName = lastName;
        self.middleName = middleName;
        self.company = company;
        self.role = role;
        self.login = login;
        self.password = password;
        self.menu = menu;
    }

    return User;
});
