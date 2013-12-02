<html>
<head>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <script type="text/javascript">
        $(function() {
            $('#btnPush').on('click', function() {
                $.ajax({
                    url: 'http://localhost:8080/weightWatchersVeryLight/j_spring_security_check',
                    type: 'POST',
                    data: {
                        j_username: 'user',
                        j_password: 'password'
                    },
                    success: function(data, textStatus, jqXHR) {
                        alert('Success!!!');
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert('Fail!');
                    }
                });
            });
        });
    </script>
</head>
<body>
<button id='btnPush'>Push Me!</button>
</body>
</html>