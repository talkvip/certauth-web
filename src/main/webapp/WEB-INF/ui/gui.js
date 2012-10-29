function getContent(contentName,layer){
    $.ajax({
        url: contentName,
        type: "GET"
    }).done(function(content){
        $("#" + layer).html(content);
    });
}

function prepareLoginLogout(menucontainer){
    var menu=$("#" + menucontainer);
    var loginlogout=$("<a id=\"loginlogout\"/>");
    loginlogout.attr("href", "#");
    menu.append($("<div/>").append(loginlogout));
    toggleLoginLogout();
}

function toggleLoginLogout(){
    var loginlogout=$("#loginlogout");
    loginlogout.unbind("click");
    $.ajax({
        url: "loginstatus.html",
        async: false
    }).done(function(st){
        if(st){
            loginlogout.text("Logout");
            loginlogout.click(function(){
                $.ajax({
                    url: "logout.html",
                    type: "GET"
                }).done(function(st){
                    if(st){
                        toggleLoginLogout();
                    }
                });
            }); 
        } else {
            loginlogout.text("Login");
            loginlogout.click(function(){
                showLoginForm("login.html", toggleLoginLogout)
            });     
        }
    });
}

function showLoginForm(loginURL,cb){
    var layer=$("<div/>");
    layer.addClass("dialog");
    layer.width($(window).innerWidth());
    layer.height($(window).innerHeight());
                
    var body=$("<div/>");
    body.addClass("dialog-body");
    body.width('300px');
    body.appendTo(layer);
                
                
    var title=$("<canvas/>");
    title.width(body.innerWidth());
    title.height("75px");
    title_ctx=title[0].getContext("2d");
    var grad=title_ctx.createLinearGradient(0, 0, 0, title.height()*0.75);
    grad.addColorStop(0, "#F1D8A2");
    grad.addColorStop(1, "#C3A27C");
    title_ctx.fillStyle=grad;
    title_ctx.fillRect(0, 0, title.width(), title.height());
    title.appendTo(body);
                
    titletxt=$("<div/>");
    titletxt.addClass("dialog-title");
    titletxt.html("<span>Login Form</span>");
    titletxt.appendTo(body);
                
    var status=$('<div/>');
    body.append(status);
                
    var username=$("<div/>");
    username.append($("<span>Username:</span>")).append($("<input id=\"___loginform_username\" type=\"text\"/>"));
    body.append(username);
    var password=$("<div/>");
    password.append($("<span>Password:</span>")).append($("<input id=\"___loginform_password\" type=\"password\"/>"));
    body.append(password);
    var login_btn_div=$("<div/>");
    var login_btn=$("<input type=\"submit\" value=\"Login\"/>");
    login_btn_div.append(login_btn);
    body.append(login_btn_div);
                
                
    layer.appendTo($("#container"));
    body.height(title.height()+username.height()+password.height()+login_btn_div.height());
                
    login_btn.click(function(){
        var u=$("#___loginform_username").val();
        var p=$("#___loginform_password").val();
        var ld=new function(){};
        ld.username=u;
        ld.password=p;
        $.ajax({
            url: loginURL,
            type: "POST",
            data: JSON.stringify(ld),
            contentType: "application/json; charset=utf-8"
        }).done(function(success){
            if(success){
                layer.remove();
                cb();
            } else {
                status.html('<span class=\"error\">Error at login information</span>');
            }
                        
        });
    });
                
}

           
$(window).resize(function(){
    $(".dialog").width($(window).innerWidth());
    $(".dialog").height($(window).innerHeight());
});

function createTitleGrid(parent,grid){
    var width=$("#" + parent).width();
    var height=$("#" + parent).height();
    $("#" + grid).get(0).setAttribute("width", width);
    $("#" + grid).get(0).setAttribute("height", height );
    var canvas_ctx=$("#" + grid).get(0).getContext("2d");
    var grad=canvas_ctx.createLinearGradient(0, 0, 0, height*0.90);
    grad.addColorStop(0, "#F1D8A2");
    grad.addColorStop(1, "#C3A27C");
    canvas_ctx.fillStyle=grad;
    canvas_ctx.fillRect(0, 0, width, height);
}