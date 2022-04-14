var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
        $('#btn-update').on('click', function () {
            _this.update();
        });
        $('#btn-delete').on('click', function () {
            _this.delete();
        });
        $('#btn-save-race').on('click', function () {
            _this.saveRace();
        });
        $('#btn-encouragement').on('click', function () {
            _this.encouragement();
        });
        $('#btn-redirect-to-join').on('click', function () {
            _this.redirectToJoin();
        });
        $('#btn-join-race').on('click', function () {
            _this.joinRace();
        });
    },
    save : function () {
        var data = {
            date: $('#date').val(),
            userId: $('#userId').val(),
            raceId: $('#raceId').val(),
            isCompleted: true,
            comment: $('#comment').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            error: function (response) {
                main.markErrorFields(response);
            }
        }).done(function() {
            alert('Congratulations 🎉 Keep up the great work!');
            window.location.href = '/race/'+$('#raceId').val();
        });
    },
    update : function () {
        var data = {
            date: $('#date').val(),
            comment: $('#comment').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            error: function (response) {
                main.markErrorFields(response);
            }
        }).done(function() {
            alert('The record has been updated 👍');
            window.location.href = '/race/'+$('#raceId').val();
        });
    },
    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function () {
            alert('Record deleted 🗑️');
            window.location.href ='/race/'+$('#raceId').val();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    saveRace : function () {
        var data = {
            raceName: $('#raceName').val(),
            wager: $('#wager').val(),
            startDate: $('#startDate').val(),
            endDate: $('#endDate').val(),
            fstUserId: $('#fstUserId').val(),
            fstUserHabit: $('#fstUserHabit').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/race',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            error: function (response) {
                main.markErrorFields(response);
            }
        }).done(function() {
            alert('The race has been created.\n\nShare the race id with your competitor and begin racing 🏎️');
            window.location.href = '/';
        });
    },
    redirectToJoin : function () {
        $.ajax({
            type:'GET',
            url: '/api/v1/race/'+(($('#raceId').val()=='') ? '0': $('#raceId').val())+'/check-eligibility/'+
            $('#sndUserId').val(),
            error: function (response) {
                main.markErrorFields(response);
            }
        }).done(function() {
            window.location.href = '/race/join/'+$('#raceId').val();
        })
    },
    joinRace : function () {
        var data = {
            endDate: $('#endDate').val(),
            sndUserId: $('#sndUserId').val(),
            sndUserHabit: $('#sndUserHabit').val()
        }
        var id = $('#raceId').val();
        $.ajax({
            type: 'PUT',
            url: '/api/v1/race/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            error: function (response) {
                main.markErrorFields(response);
            }
        }).done(function() {
            alert('Race joined! 💪 Let\'s get that W 🔥');
            window.location.href = '/race/'+$('#raceId').val();
        });
//        .fail(function (error) {
//                    alert(JSON.stringify(error));
//                });
    },
    markErrorFields : function (response) { // credit: DongUk Lee
        const errorFields = response.responseJSON.errors;

        if (!errorFields) {
            alert(response.response.message);
            return;
        }

        var $field, error;
        for (var i=0, length = errorFields.length; i<length; i++) {
            error = errorFields[i];
            $field = $('#'+error['field']);
            if ($field && $field.length>0) {
                if ($field.parent('.input-group').length) {
                    if (i==0) {
                        $field.parent('.input-group').siblings('.error-message').remove();
                    }
                    $field.parent('.input-group').after('<span class="error-message taxt-small text-danger" style="display:block; margin-top:-10px;">'+error.defaultMessage+'</span>');
                } else {
                    $field.siblings('.error-message').remove(); // remove previous error messages
                    $field.after('<span class="error-message taxt-small text-danger">'+error.defaultMessage+'</span>');
                }
            }
        }
    },
    encouragement : function () {
        alert('Your honesty is respectable 😊 \n\nKeep your head up! 👑');
        window.location.href = '/race/'+$('#raceId').val();
    }

};

main.init();