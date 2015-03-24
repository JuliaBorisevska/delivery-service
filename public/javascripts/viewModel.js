define(["application/viewModel/loginVM",
        "application/viewModel/listVM"], function (loginVM, listVM) {

    "use strict";

    function ViewModel(){
        var self = this,
            login = {
                name: "Login",
                id: "lgn"
            },
            list = {
                name: "List",
                id: "lst"
            };

        self.loginVM = loginVM;
        self.listVM = listVM;
        self.sections = [login, list];
        self.chosenSectionId = ko.observable();
        self.user = ko.observable();

        self.goTo = function(sectionId) {
            location.hash = sectionId;
        };

        Sammy(function() {
            this.get('#:section', function() {
                var sectionId = this.params.section;
                for(var ind in self.sections) {
                    if(self.sections.hasOwnProperty(ind) && self.sections[ind].id === sectionId) {
                        self.chosenSectionId(self.sections[ind]);
                    }
                }
            });

            this.get("/", function() {
                location.hash = "lgn";
            });

        }).run();

        $("body").on("authorized", function(evt, user) {
            self.user(user);
            self.goTo("lst");
        });

        $.ajaxSetup({
            global: true,
            error: function(xhr) {
                if (xhr.status == 401) {
                    location.hash = "lgn";
                }
            }
        });
    }

    var launch = function() {
        ko.applyBindings(new ViewModel());
    };

    return {
        launch: launch,
        goTo: self.goTo
    }

});