define(["application/service/initService",
        "application/util/callback",
        "application/viewModel/loginVM",
        "application/viewModel/contactListVM",
        "application/viewModel/userlistVM"], 
        function (initService, Callback, loginVM, contactListVM, userlistVM) {

    "use strict";

    function ViewModel(){
    	var self = this;
    	var k;
    	var index=-1, sectionIndex;
    	self.sections = [{name: "Вход", id: "lgn"}];
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
        self.contactListVM = contactListVM;
        self.userlistVM = userlistVM;
        self.chosenSectionId = ko.observable(self.sections[0]);
        self.user = ko.observable();
        self.menu = ko.observableArray();
        self.goTo = function(section) {
            switch (section.id){
            case "lst":
            	self.userlistVM.list(self.userlistVM.currentPage(), self.userlistVM.PAGE_SIZE);
            	break;
            case "ctlst":
                self.contactListVM.list(1, /*self.contactListVM.PAGE_SIZE*/ 3);
                break;
            }
            location.hash = section.id;
        };
        
        self.fill = function(vm, pageNumber){
        	if (pageNumber < vm.numbers()[1]){
        		if (pageNumber>vm.SHOW_PAGES-1){
        			vm.numbers([]);
            		for (k=pageNumber-vm.SHOW_PAGES+1; k<=pageNumber; k++){
            			vm.numbers.push(k);
            		}
            	}else{
            		vm.numbers([]);
            		for (k=1; k<=(vm.totalPages()<vm.SHOW_PAGES?vm.totalPages():vm.SHOW_PAGES); k++){
            			vm.numbers.push(k);
            		}
            	}
        	} 
        	if (pageNumber > vm.numbers()[vm.numbers().length-2]){
        		if (pageNumber<vm.totalPages()-vm.SHOW_PAGES+1){
        			vm.numbers([]);
        			for (k=pageNumber; k<=pageNumber+vm.SHOW_PAGES-1; k++){
        				vm.numbers.push(k);
        			}
        		}else{
        			vm.numbers([]);
            		for (k=vm.totalPages()-vm.SHOW_PAGES+1>0?vm.totalPages()-vm.SHOW_PAGES+1:1; k<=vm.totalPages(); k++){
            			vm.numbers.push(k);
            		}
        		}
        	} 
        };
        
        self.navigate = function (vm,data,e) {
            var el = e.target;
            switch (el.id){
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
            if(vm.currentPage() < 1) {
            	vm.currentPage(1);
                return;
            }
            self.fill(vm, vm.currentPage());
            vm.list(vm.currentPage(), vm.PAGE_SIZE);
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
            	for (k=0; k<self.sections.length; k++){
                	if (self.sections[k].id===user.menu[item]){
                		index=k;
                		break;
                	}
                }
            	if (index!=-1){
                	self.menu.push(self.sections[index]);
            	}
            	index=-1;
            }
            if (self.menu().length==0){
        		alert("У данного пользователя нет полномочий!");
        		location.hash = "lgn";
        	}else{
        		 self.chosenSectionId(self.menu()[0]);
                 self.goTo(self.chosenSectionId());
        	}
           
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
    	navigate: self.navigate,
        launch: launch,
        goTo: self.goTo
    }

});