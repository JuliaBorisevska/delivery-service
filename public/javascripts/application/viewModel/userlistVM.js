define(["application/service/userService",
        "application/util/callback",
        "application/model/user"], function(userService, Callback, User) {
    "use strict";

    function UserListVM() {
    	var k;
    	var PAGE_SIZE = 1,
        	SHOW_PAGES = 3,
        	currentPage = ko.observable(1),
        	totalPages = ko.observable(),
        	reply;
        var self = this,
            users = ko.observableArray(),
            numbers = ko.observableArray([]);
        var fill = function(pageNumber){
        	if (pageNumber < numbers[1]){
        		if (pageNumber>SHOW_PAGES-1){
            		numbers([]);
            		for (k=pageNumber-SHOW_PAGES+1; k<=pageNumber; k++){
            			numbers.push(k);
            		}
            	}else{
            		numbers([]);
            		for (k=1; k<=totalPages()<SHOW_PAGES?totalPages():SHOW_PAGES; k++){
            			numbers.push(k);
            		}
            	}
        	} 
        	if (pageNumber > numbers[numbers.length-2]){
        		if (pageNumber<totalPages()-SHOW_PAGES+1){
        			numbers([]);
        			for (k=pageNumber; k<=pageNumber+SHOW_PAGES-1; k++){
        				numbers.push(k);
        			}
        		}else{
        			numbers([]);
            		for (k=totalPages()-SHOW_PAGES+1>0?totalPages()-SHOW_PAGES+1:1; k<=totalPages(); k++){
            			numbers.push(k);
            		}
        		}
        	} 
        };
        var list = function(page, pageSize) {
                userService.list(page, pageSize,
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                users([]);
                                totalPages(reply.data.totalPages);
                                if (numbers.length==0){
                                	numbers([]);
                                	for (k=1; k<=(totalPages()<SHOW_PAGES?totalPages():SHOW_PAGES); k++){
                            			numbers.push(k);
                            		}
                                }
                                for(var i = 0, lth = reply.data.list.length; i < lth; i++) {
                                    var user = reply.data.list[i];
                                    users.push(new User(user.id,user.firstName, user.lastName, user.middleName, user.roleTitle, user.companyTitle, user.login, user.menu));
                                }
                            }
                        }, self, {}),
                    new Callback(function(params){
                    	alert(params.reply.responseJSON.data);
                        }, self, {})
                )
            };
            
          var navigate = function (data,e) {
                var el = e.target;
                switch (el.id){
                	case "next":
                		if (currentPage() < totalPages()) {
                        	currentPage(currentPage() + 1);
                        }
                		break;
                	case "prev":
                		if (currentPage() > 1) {
                			currentPage(currentPage() - 1);
                        }
                		break;
                	case "first":
                		if (currentPage() > 1) {
                			currentPage(1);
                        }
                		break;
                	case "last":
                		if (currentPage() < totalPages()) {
                			currentPage(totalPages());
                        }
                		break;
                	case "block":
                		currentPage(data);
                		break;
                	
                }
                if(currentPage() < 1) {
                    currentPage(1);
                    return;
                }
                fill(currentPage());
                list(currentPage(), PAGE_SIZE);
            };
            
        return {
            users: users,
            numbers: numbers,
            list: list,
            fill: fill,
            navigate: navigate,
            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE,
            SHOW_PAGES: SHOW_PAGES
        }
    }

    return new UserListVM();

});