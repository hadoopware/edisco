    var request = require('request');
  
    const serverUrl = "http://localhost:9000/v1/translate"

    $(() => {
      
      $('#translateForm').bind('input propertychange', function() {
        const text = this.value
        $('#translated').text(text);
      })
    
      $('#translateForm').focus() // focus input box

      $("#translateForm").keydown(function(e){
        // Enter was pressed without shift key
        if (e.keyCode == 13 && !e.shiftKey)
        {
            const text = this.value
            // prevent default behavior
            e.preventDefault();

            console.log('Tranlsating: '+text);

            // (async () => {
            //   const response = await fetch(serverUrl+'/'+text);
            //   const j = await response.json(); //extract JSON from the http response
            //   console.log('GET: '+j.translated);
            // })();

            (async () => {
              var translateRequest = {
                "text" : text
              }
              console.log('Fetching: POST('+ serverUrl +')'+': '+text)
              const response = await fetch(serverUrl, {
                method: 'POST',
                body: JSON.stringify(translateRequest), // string or object
                headers:{
                  'Content-Type': 'application/json'
                }
              });
              console.log('Fetching: POST('+ serverUrl +')'+': '+text+': r='+response.status);
              switch (response.status) {
                case 200: 
                  const j = await response.json(); //extract JSON from the http response
                  $('#translated').text(j.translated);
                  break;
                default:
                  $('#translated').text("error: "+response.status+": "+response.statusText);
              }
              
            })();

            $('#translated').text("tranlsating...");
        }
      });
    })

    

