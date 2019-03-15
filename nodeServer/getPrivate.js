//根据地址密码获取地址的私钥

var express = require('express');
var keythereum = require('keythereum');
var app = express();
var bodyParser = require('body-parser');
app.use(bodyParser.json({ type: 'application/json' }));
app.use(bodyParser.urlencoded({ extended: false }));

app.post('/getPrivateKey',function(req,res){

    var body = req.body;
    var addr=body.addr;
    var password=body.password;
    //节点存放的数据目录 /root/eth-node-private/data
    var fromkey = keythereum.importFromFile(addr, "/root/eth-node-private/data");
    //recover输出为buffer类型的私钥
    var privateKey = keythereum.recover(password, fromkey);
    var key=privateKey.toString('hex');
    console.log(key);
    res.send(key)
});
app.listen(8001, function () {
    console.log("启动服务 http://localhost:8001 ")
})