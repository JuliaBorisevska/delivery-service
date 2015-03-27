define(["application/viewModel/loginVM",
        "application/viewModel/listVM"], function (loginVM, listVM) {

    "use strict";

    function ViewModel(){
        var self = this,
            login = {
                name: "Login",
                id: "lgn"
            },
            userlist = {
                name: "Список пользователей",
                id: "lst"
            },
        	orderlist = {
                name: "Список заказов",
                id: "ordlst"
            },
        	contactlist = {
                    name: "Список контактов",
                    id: "ctlst"
            };
        self.roles = [{title: "Менеджер по приему заказов", menu: [contactlist]},
                     {title: "Супервизор", menu: [contactlist, orderlist]},
                     {title: "Администратор", menu: [contactlist]}
                     ];

        self.loginVM = loginVM;
        self.listVM = listVM;
        self.sections = [login, userlist, orderlist, contactlist];
        self.chosenSectionId = ko.observable();
        self.user = ko.observable();
        self.menu = ko.observable();
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
            for(var item in self.roles) {
                if(self.roles.hasOwnProperty(item) && self.roles[item].title === user.role) {
                    self.menu(self.roles[item].menu);
                	self.chosenSectionId(self.roles[item].menu[0]);
                }
            } 
            self.goTo(self.chosenSectionId.id);
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