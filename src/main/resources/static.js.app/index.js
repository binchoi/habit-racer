var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
    },
    save : function () {
        var data = {
            date: $('#date').val()
            isCompleted: true
            author: 'test_author'
            comment: $('#content').val()
        };

        console.log(date);
        console.log(isCompleted);
        console.log(author);
        console.log(comment);

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('Progress recorded. Keep up the great work!');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();