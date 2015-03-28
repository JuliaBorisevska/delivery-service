define(["application/service/initService",
        "application/util/callback",
        "application/viewModel/loginVM",
        "application/viewModel/listVM"], function (initService, Callback, loginVM, listVM) {

    "use strict";

    function ViewModel(){
    	var self = this;
    	self.sections = [{name: "Login", id: "lgn"}];
    	initService.initSections(
                new Callback(function(params){
                    var reply = params.reply;
                    if(reply.status === "SUCCESS") {
                    	self.sections = reply.data;
                    }
                }, this, {}),
                new Callback(function(params){
                    alert(params.reply.responseJSON.data);
                }, this, {})
            );
        self.loginVM = loginVM;
        self.listVM = listVM;
        self.chosenSectionId = ko.observable(self.sections[0]);
        self.user = ko.observable();
        self.menu = ko.observableArray();
        self.goTo = function(section) {
            location.hash = section.id;
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
            this.get(" ", function() {
                location.hash = "lgn";
            });
            this.get("/", function() {
                location.hash = "lgn";
            });

        }).run();

        $("body").on("authorized", function(evt, user) {
            self.user(user);
            self.menu.removeAll();
            for(var item in user.menu) {
            	self.menu.push(self.sections[user.menu[item]]);
            }
            self.chosenSectionId(self.menu()[0]);
            self.goTo(self.chosenSectionId());
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