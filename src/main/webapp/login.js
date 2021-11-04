// $(document).ready(function () {
//     $.ajax({
//         type: 'GET',
//         url: 'http://localhost:8080/todo/' + 'login.do',
//         dataType: 'json'
//     }).done(function (data) {
//         $('#username').val(data.username);
//         $('#password').val(data.password);
//     }).fail(function (err) {
//         console.log(err);
//     });
// });

function login() {
    $('#notification').text('');
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/todo/' + 'login.do',
        data: JSON.stringify({
            username: $('#username').val(),
            password: $('#password').val()
        }),
        dataType: 'text'
    }).done(function(data) {
        if (data === '200 OK') {
            window.location.href = 'http://localhost:8080/todo/' + 'index.html';
        } else {
            $('#notification').text('Wrong username or password');
            console.log(data);
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function login1() {
        let username = $('#username');
        let password = $('#password');
        let div = '';
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/todo/' + 'login.do',
            data: {username: username.val(), password: password.val()},
            dataType: 'json'
        }).done(function (data) {
            if (data.result === true) {
                window.location.href = 'http://localhost:8080/todo/index.html';
            } else {
                $('#notification').text('Wrong username or password');
                console.log(data);
            }
        }).fail(function () {
            alert('Ошибка входа');
        });
}

function goToRegistration() {
    window.location.href = 'http://localhost:8080/todo/' + 'reg.html';
}