
var http = require('http');
var express = require('express');
var app = express();
var server = http.createServer(app);


app.get('/', function (req, res) {
  res.send('MiniGame Server...');
});

var port = process.env.PORT||5000;

server.listen(port, function() {
  console.log('Listening on ' + server.address().port);
});