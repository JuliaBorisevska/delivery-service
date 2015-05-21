define(["application/service/initService",
        "application/util/callback",
        "application/model/template",
        "application/model/user",
        "application/viewModel/loginVM",
        "application/viewModel/contactListVM",
        "application/viewModel/orderlistVM",
        "application/viewModel/userlistVM",
        "application/viewModel/emaillistVM",
        "application/viewModel/contactDetailsVM",
        "application/viewModel/userDetailsVM",
        "application/viewModel/orderDetailsVM",
        "application/viewModel/emailDetailsVM",
        "application/model/Order",
        "application/model/OrderForSearch",
        "application/model/Contact",
        "application/viewModel/rolelistVM",
        "application/model/role"],
    function (initService, Callback, Template, User, loginVM, contactListVM, orderlistVM, userlistVM, emaillistVM, contactDetailsVM, userDetailsVM, orderDetailsVM, emailDetailsVM, Order, OrderForSearch, Contact, rolelistVM, Role) {

        "use strict";

        function ViewModel() {
            var self = this;
            var k;
            var index = -1, sectionIndex;
            self.loginVM = loginVM;
            self.contactListVM = contactListVM;
            self.orderlistVM = orderlistVM;
            self.userlistVM = userlistVM;
            self.emaillistVM = emaillistVM;
            self.contactDetailsVM = contactDetailsVM;
            self.orderDetailsVM = orderDetailsVM;
            self.userDetailsVM = userDetailsVM;
            self.emailDetailsVM = emailDetailsVM;
            self.chosenSectionId = ko.observable();
            self.user = ko.observable();
            self.menu = ko.observableArray();
            self.rolelistVM = rolelistVM;
            self.sections = [];
            var app = Sammy(function () {
                this.get('#:section', function () {
                    var sectionId = this.params.section;
                    for (var ind in self.sections) {
                        if (self.sections.hasOwnProperty(ind) && self.sections[ind].id === sectionId) {
                            self.chosenSectionId(self.sections[ind]);
                            if (sessionStorage)
                                sessionStorage.setItem("current", JSON.stringify(self.sections[ind]));
                        }
                    }
                });
                this.get(" ", function () {
                    location.hash = "lgn";
                });
                this.get("/", function () {
                    location.hash = "lgn";
                });

            });
            initService.initSections(
                new Callback(function (params) {
                    var reply = params.reply;
                    if (reply.status === "SUCCESS") {
                        self.sections = reply.data;
                        if (sessionStorage) {
                            var sect = JSON.parse(sessionStorage.getItem("current"));
                            if (sect) {
                                if (sect.id != "lgn") {
                                    initService.getUser(
                                        new Callback(function (params) {
                                            var reply = params.reply,
                                                data = reply.data;
                                            if (reply.status === "SUCCESS") {
                                                setUser(new User(data.id, data.firstName, data.lastName, data.middleName, data.roleTitle, data.companyTitle, data.login, data.menu));
                                            }
                                        }, this, {}),
                                        new Callback(function (params) {
                                            alert(params.reply.responseJSON.data);
                                            location.hash = "lgn";
                                        }, this, {})
                                    );
                                    self.chosenSectionId(sect);
                                }
                            }
                        }
                        app.run();

                    }
                }, this, {}),
                new Callback(function (params) {
                    alert(params.reply.responseJSON.data);
                }, this, {})
            );
        self.goTo = function(section) {
        	self.orderlistVM.searchOrder(null);
        	self.contactListVM.contactForSearch(null);
        	self.orderDetailsVM.order(null);
            switch (section.id){
            	case "lst":
            		self.userlistVM.selectedRole("");
                    if (userDetailsVM.user() === undefined) {
                        self.userDetailsVM.setUser(new User("", "", "", "", "", "", "", "", "", "", ""));
                    }
                    self.userlistVM.numbers([]);
                    self.userlistVM.currentPage(1);
            		self.userlistVM.list(self.userlistVM.currentPage(), self.userlistVM.PAGE_SIZE);
            		location.hash = section.id;
            		break;
            	case "ctlst":
            		self.contactListVM.currentPage(1);
            		self.contactListVM.numbers([]);
            		self.contactListVM.list(self.contactListVM.currentPage(), self.contactListVM.PAGE_SIZE);
            		location.hash = section.id;
            		break;
            	case "ordlst":
            		self.orderlistVM.getStatusList();
            		location.hash = section.id;
            		break;
            	case "ordsearch":
            		self.orderlistVM.searchOrder(new OrderForSearch("", "", "", "", "", ""));
            		location.hash = section.id;
            		break;
            	case "ordadd":
            		self.orderDetailsVM.getFirstStatus();
            		self.orderDetailsVM.setOrder(new Order("", "", "", "", self.user(), "", "", "","", ""), [],[]);
            		location.hash = section.id;
            		break;
            	case "ctadd":
            		self.contactDetailsVM.setContact(new Contact("", "", "", "", "", "", "", "","", "",""));
            		location.hash = section.id;
            		break;
            	case "ctsearch":
            		self.contactDetailsVM.setContact(new Contact(-1));
            		alert(self.contactDetailsVM.contact().id);
            		location.hash = section.id;
            		break;
                case "useradd":
                    self.userDetailsVM.setUser(new User("", "", "", "", "", "", "", "", "", ""));
                    self.rolelistVM.list();

                    location.hash = section.id;
                    break;
                case "ordchange":
                	if (self.chosenSectionId().id=="ordchange"){
                		self.orderlistVM.getStatusList();
                        location.hash  = "ordlst";
                	}else{
                		self.orderDetailsVM.setOrder(new Order("", "", "", "", self.user(), "", "", "","", ""), [],[]);
                		location.hash=self.chosenSectionId().id;
                	}
            		break;
                case "ctchange":
                	if (self.chosenSectionId().id=="ctchange"){
                		self.contactListVM.currentPage(1);
                		self.contactListVM.numbers([]);
                		self.contactListVM.list(self.contactListVM.currentPage(), self.contactListVM.PAGE_SIZE);
                        location.hash  = "ctlst";
                	}else{
                		location.hash=self.chosenSectionId().id;
                	}
            		break;
                case "email":
                        if (self.chosenSectionId().id == "email") {
                            self.emailDetailsVM.setTemplate(new Template("", ""));
                            self.emaillistVM.list();
                            self.contactListVM.currentPage(1);
                            self.contactListVM.numbers([]);
                            self.contactListVM.list(self.contactListVM.currentPage(), self.contactListVM.PAGE_SIZE);


                            location.hash = section.id;

                        } else {
                            location.hash = self.chosenSectionId().id;
                        }
                        break;
                case "userchange":
                        if (self.chosenSectionId().id == "userchange") {
                            self.userlistVM.selectedRole("");
                            self.userlistVM.numbers([]);
                            self.userlistVM.currentPage(1);
                            self.userlistVM.list(self.userlistVM.currentPage(), self.userlistVM.PAGE_SIZE);
                            self.rolelistVM.list();

                            location.hash = "lst";
                        } else {
                            location.hash = self.chosenSectionId().id;
                        }
                        break;
                default:
                        location.hash = section.id;
                }
            };

            self.fill = function (vm, pageNumber) {
                if (pageNumber < vm.numbers()[1]) {
                    if (pageNumber > vm.SHOW_PAGES - 1) {
                        vm.numbers([]);
                        for (k = pageNumber - vm.SHOW_PAGES + 1; k <= pageNumber; k++) {
                            vm.numbers.push(k);
                        }
                    } else {
                        vm.numbers([]);
                        for (k = 1; k <= (vm.totalPages() < vm.SHOW_PAGES ? vm.totalPages() : vm.SHOW_PAGES); k++) {
                            vm.numbers.push(k);
                        }
                    }
                }
                if (pageNumber > vm.numbers()[vm.numbers().length - 2]) {
                    if (pageNumber < vm.totalPages() - vm.SHOW_PAGES + 1) {
                        vm.numbers([]);
                        for (k = pageNumber; k <= pageNumber + vm.SHOW_PAGES - 1; k++) {
                            vm.numbers.push(k);
                        }
                    } else {
                        vm.numbers([]);
                        for (k = vm.totalPages() - vm.SHOW_PAGES + 1 > 0 ? vm.totalPages() - vm.SHOW_PAGES + 1 : 1; k <= vm.totalPages(); k++) {
                            vm.numbers.push(k);
                        }
                    }
                }
            };

            self.navigate = function (vm, data, e) {
                var el = e.target;
                switch (el.id) {
                    case "next":
                        if (vm.currentPage() < vm.totalPages()) {
                            vm.currentPage(vm.currentPage() + 1);
                        }
                        break;
                    case "prev":
                        if (vm.currentPage() > 1) {
                            vm.currentPage(vm.currentPage() - 1);
                        }
                        break;
                    case "first":
                        if (vm.currentPage() > 1) {
                            vm.currentPage(1);
                        }
                        break;
                    case "last":
                        if (vm.currentPage() < vm.totalPages()) {
                            vm.currentPage(vm.totalPages());
                        }
                        break;
                    case "block":
                        vm.currentPage(data);
                        break;

                }
                if (vm.currentPage() < 1) {
                    vm.currentPage(1);
                    return;
                }
                self.fill(vm, vm.currentPage());
                vm.list(vm.currentPage(), vm.PAGE_SIZE);
            };

            var setUser = function (user) {
                self.user(user);
                self.menu.removeAll();
                for (var item in user.menu) {
                    for (k = 0; k < self.sections.length; k++) {
                        if (self.sections[k].id === user.menu[item]) {
                            index = k;
                            break;
                        }
                    }
                    if (index != -1) {
                        self.menu.push(self.sections[index]);
                    }
                    index = -1;
                }
                if (self.menu().length == 0) {
                    alert("У данного пользователя нет полномочий!");
                    location.hash = "lgn";
                } else {
                    if (sessionStorage) {
                        var data = JSON.parse(sessionStorage.getItem("current"));
                        if (data.id != "lgn") {
                            self.chosenSectionId(data);
                        } else {
                            self.chosenSectionId(self.menu()[0]);
                        }
                    } else {
                        self.chosenSectionId(self.menu()[0]);
                    }
                    self.goTo(self.chosenSectionId());
                }

            };


            $("body").on("authorized", function (evt, user) {
                self.orderlistVM.searchOrder(null);
            	self.contactListVM.contactForSearch(null);
            	setUser(user);
            });

            $.ajaxSetup({
                global: true,
                error: function (xhr) {
                    if (xhr.status == 401) {
                        location.hash = "lgn";
                    }
                }
            });
        }

        var launch = function () {
            ko.applyBindings(new ViewModel());
            ko.validation.init({
            	registerExtenders: true,
                messagesOnModified: true,
                insertMessages: true,
                decorateInputElement: true,
                decorateElementOnModified: true
            }, true);
            ko.validation.locale('ru-RU');
        };

        return {
            navigate: self.navigate,
            launch: launch,
            goTo: self.goTo
        }

    });
