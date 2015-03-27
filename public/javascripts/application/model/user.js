
define(function() {
    "use strict";
    function User (firstName, role, menu) {
        var self = this;
        self.firstName = firstName;
        self.role = role;
        self.menu = menu;
    }

    return User;
});
