    var request = require('request');
    setInterval (function(){
      request("http://finance.google.com/finance/info?client=ig&q=.DJI", function(error, response, body) {
        body = body.slice(3);
        body = JSON.parse(body);
        console.log(body);
        newPrice(body);
      });
    }, 1000);
    var lastPrice;
    function newPrice(arr) {
      currentPrice = arr[0]["l"];
      var history = document.getElementById("priceHistory");
      if (lastPrice<currentPrice) {
        var newElText = "▲ ";
        var wrap = document.createElement("span");
        wrap.className="up";
      }
      else if (lastPrice==currentPrice) {
        var newElText = "▬ ";
        var wrap = document.createElement("span");
        wrap.className="noChange";
      }
      else {
        var newElText = "▼ ";
        var wrap = document.createElement("span");
        wrap.className="down";
      }
      history.appendChild(wrap);
      var textNode = document.createTextNode(newElText);
      wrap.appendChild(textNode);
      var nodeList = history.getElementsByTagName("SPAN").length;
      if (nodeList==6) {
        history.children[0].remove();
      }
      document.getElementById("price").innerHTML = currentPrice;
      lastPrice=currentPrice;
    }


