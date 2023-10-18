<#ftl encoding="UTF-8">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your task will end soon</title>
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
        <h2 style="text-align: center">Reminder</h2>
        <p>Hello, ${name}!</p>
        <p>One of your tasks is approaching the deadline. There is only 1 hour left!</p>
        <div>
            <hr>
            <p>Title: ${title}</p>
            <p>Description: ${description}</p>
            <p>Task status is ${status}</p>
        </div>
        <p>More information about the task is available in the application.</p>
        <p>If you have any questions, please contact me in the telegram.</p>
        <p>Sincerely, <br>Kirill. </p> <br>
        <a id="a" href="http://t.me/isthatkirill">Telegram</a>
    </div>
</div>
</body>
</html>