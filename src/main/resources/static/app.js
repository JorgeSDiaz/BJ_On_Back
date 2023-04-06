var app = (function(){

    var stompClient = null;

    var PLAYERS = "/topic/players";

    var SERVER = "/BJserver/addplayer";

    var PLAYERSOFGAME = null;

    var connect = function(user){
        console.info('Connecting to WS...');
        var socket = new SockJS('/BJgame');
        stompClient = Stomp.over(socket);
        stompClient.connect({},function(frame){
            console.log('Connected: ' + frame);
            stompClient.subscribe(PLAYERS,function(eventbody){
                var player = JSON.parse(eventbody.body);
                alert(player.name + " se ha unido a la partida");
            })
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/game/v1.0/player",
                data: user,
                contentType: "application/json",
                success: function(response) {
                alert(Object.keys(response).length + " estan en la partida");
                // aquí puedes hacer algo con la respuesta, como asignarla a una variable
                PLAYERSOFGAME = response;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert("Error: " + textStatus + " - " + errorThrown);
                }
            })
       


            
            

        })
        
    }

    

    var disconnect = function(){
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
    }

    return{

        connectGame: function(){
            var name = document.getElementById("name").value;
            console.log(name);
            var user = {
                "name" : name,
                "coins" : 1000,
                "createdAt" : "2023-04-02"
            }
            connect(JSON.stringify(user))
            
        },

        disconnectGame : function(){
            disconnect()
        }
    }


})();