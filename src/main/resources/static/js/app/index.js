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
            comment: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('Progress recorded ✅\n\nKeep up the great work 💪');
            window.location.href = '/race/'+$('#raceId').val();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var data = {
            date: $('#date').val(),
            comment: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('The record has been updated 👍');
            window.location.href = '/race/'+$('#raceId').val();
        }).fail(function (error) {
            alert(JSON.stringify(error));
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
            data: JSON.stringify(data)
        }).done(function() {
            alert('Race created! \n\nFind the RACEID next to the race name and share it with your competitor 💪');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    redirectToJoin : function () {
        window.location.href = '/race/join/'+$('#raceId').val();
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
            data: JSON.stringify(data)
        }).done(function() {
            alert('Race joined! 💪 Let\'s get that W 🔥');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    encouragement : function () {
        alert('Your honesty is respectable 😊 \n\nKeep your head up! 👑');
        window.location.href = '/race/'+$('#raceId').val();
    }

};

main.init();