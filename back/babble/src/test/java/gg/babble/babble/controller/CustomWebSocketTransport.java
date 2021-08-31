package gg.babble.babble.controller;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.sockjs.client.TransportRequest;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class CustomWebSocketTransport extends WebSocketTransport {

    private String sessionId;

    public CustomWebSocketTransport(final WebSocketClient webSocketClient) {
        super(webSocketClient);
    }

    @Override
    public ListenableFuture<WebSocketSession> connect(TransportRequest request, WebSocketHandler handler) {
        sessionId = request.getSockJsUrlInfo().getSessionId();
        return super.connect(request, handler);
    }

    public String getSessionId() {
        return sessionId;
    }
}
