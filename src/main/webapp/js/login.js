(function () {
    mdui.$('.register-btn')[0].onclick = function () {
        mdui.dialog({
            title: "Notice",
            content: "User registration is not yet available.",
            buttons: [{
                text: "OK"
            }]
        })
    }

    mdui.$('#username')[0].oninput = function () {
        mdui.$("#username-field").removeClass("mdui-textfield-invalid");
        mdui.$("#password-field").removeClass("mdui-textfield-invalid");
        mdui.$("#error-message").hide()
    }

    mdui.$('#password')[0].oninput = function () {
        mdui.$("#username-field").removeClass("mdui-textfield-invalid");
        mdui.$("#password-field").removeClass("mdui-textfield-invalid");
        mdui.$("#error-message").hide();
    }

    mdui.$('#login-btn')[0].onclick = function () {
        var username = mdui.$('#username')[0].value;
        var password = mdui.$('#password')[0].value;
        if (!username || !password) {
            mdui.$("#username-field").addClass("mdui-textfield-invalid");
            mdui.$("#password-field").addClass("mdui-textfield-invalid");
            mdui.$("#error-message").show().html("Username or Password is Empty");
            return;
        }
        mdui.$.ajax({
            method: 'POST',
            url: 'auth',
            dataType: 'json',
            data: 'username=' + username + '&password=' + password,
        }).then(function (res) {
            console.log(res)
            if (res.status === 200) {
                location = "./";
            } else if (res.status === 403) {
                mdui.$("#username-field").addClass("mdui-textfield-invalid");
                mdui.$("#password-field").addClass("mdui-textfield-invalid");
                mdui.$("#error-message").show().html("Username or Password is Incorrect");
            }
        })
    }
})()