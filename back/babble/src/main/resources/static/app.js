var stompClient = null;
var roomId = null;
var subscription = null;

function setRoom(roomName) {
  roomId = roomName.text();
  $("#currentRoom").text(roomId);

  if (subscription !== null) {
    subscription.unsubscribe();
    subscription = stompClient.subscribe('/topic/rooms/' + roomId + '/chat',
        function (chat) {
          showChat(JSON.parse(chat.body).user.name + " : " + JSON.parse(
              chat.body).content);
        });
    $("#chats").html("");
  }

}

function setConnected(connected) {

  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#userId").attr("disabled", true);
  } else {
    $("#userId").attr("disabled", false);
  }
}

function randomNum(min, max) {
  var randNum = Math.floor(Math.random() * (max - min + 1)) + min;
  return randNum;
}

function connect() {

  if (!validation()) {
    return
  }

  const sessionId = randomNum(1, 1000);
  console.log(sessionId);

  const socket = new SockJS('/connection', [], {
    sessionId: () => {
      return sessionId
    }
  });

  console.log(sessionId);

  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    setConnected(true);

    subscription = stompClient.subscribe('/topic/rooms/' + roomId + '/chat',
        function (chat) {
          showChat(JSON.parse(chat.body).user.id + " : " + JSON.parse(
              chat.body).content);
        });

    subscription = stompClient.subscribe('/topic/rooms/' + roomId + '/users',
        function (chat) {
          let guestsString = "";
          for (const guest of JSON.parse(chat.body).guests) {
            guestsString += guest.name + ", ";
          }

          showChat(
              '호스트 이름 : ' + JSON.parse(chat.body).host.name + '/ 게스트들의 이름 : '
              + guestsString
          );
        });

    const userId = $("#userId").val();
    stompClient.send("/ws/rooms/" + roomId + "/users", {},
        JSON.stringify({userId, sessionId}));
  });

}

function validation() {

  if ($("#userId").val() === "") {
    alert("이름을 입력해주세요");
    return false;
  }

  if (roomId === null) {
    alert("방을 선택해주세요");
    return false;
  }

  return true;
}

function disconnect() {

  if (stompClient !== null) {
    stompClient.disconnect();
  }

  setConnected(false);
}

function send() {
  const userId = $("#userId").val();
  const content = $("#content").val();
  stompClient.send("/ws/rooms/" + roomId + "/chat", {},
      JSON.stringify({userId, content}));

  $("#content").val("");
}

function showChat(message) {
  $("#chats").append("<tr><td colspan='2'>" + message + "</td></tr>");
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $("#connect").click(function () {
    connect();
  });
  $("#disconnect").click(function () {
    disconnect();
  });
  $("#send").click(function () {
    send();
  });
  $("a[href=\\#]").click(function () {
    setRoom($(this))
  })
});
