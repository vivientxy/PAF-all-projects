package paf.day4workshop.service;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class MessageSubscriber implements MessageListener {

    @Autowired
    MessageService msgSvc;

    @Override
	public void onMessage(Message msg, byte[] pattern) {
		byte[] payload = msg.getBody();

		JsonReader reader = Json.createReader(new ByteArrayInputStream(payload));
		JsonObject jsonObject = reader.readObject();

		String id = jsonObject.getString("id");

        // only process if id = your id
        if (msgSvc.getAppName().equals(id)) {
            // MessageObject message = new MessageObject(id, jsonObject.getString("message"));
            System.out.println(">>> subscribed: " + jsonObject.toString());
        }

	}

}
