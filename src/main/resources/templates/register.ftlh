<#ftl encoding="UTF">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thank you registration, ${name}</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            background-color: #528c91;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: whitesmoke;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #a {
            text-decoration: none;
        }

    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Registration</h1>
        <p>Hello, ${name}!</p>
        <p>You have successfully registered in the application!</p>
        <p>If you have any questions, please contact me in the telegram</p>
        <p>Sincerely, <br>Kirill </p> <br>
        <a id="a" href="http://t.me/isthatkirill">Telegram</a>
    </div>
</div>
</body>
</html>