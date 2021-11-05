function signUp() {
    $('#notification').text('');
    if ($('#password').val() !== $('#repeatPassword').val()) {
        $('#notification').text('Password must be equal');
        return;
    }
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/todo/' + 'reg.do',
        data: JSON.stringify({
            username: $('#username').val(),
            password: $('#password').val()
        }),
        dataType: 'json'
    }).done(function(data) {
        if (data.result === true) {
            window.location.href = 'http://localhost:8080/todo/' + 'login.html';
        } else {
            $('#notification').text('User with this login already exists');
            console.log(data);
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function Cancel() {
    window.location.href = 'http://localhost:8080/todo/' + 'login.html';
}