package ibf2023.paf.day25.services;

import java.io.ByteArrayInputStream;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import ibf2023.paf.day25.Constant;
import ibf2023.paf.day25.models.MessageObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class MessageSubscriber implements MessageListener {

    @Override
	public void onMessage(Message msg, byte[] pattern) {
		byte[] payload = msg.getBody();

		JsonReader reader = Json.createReader(new ByteArrayInputStream(payload));
		JsonObject jsonObject = reader.readObject();

		String id = jsonObject.getString("id");

        // only process if id = your id
        if (Constant.MY_ID.equals(id)) {
            // MessageObject message = new MessageObject(id, jsonObject.getString("message"));
            System.out.println(">>> subscribed: " + jsonObject.toString());
        }
        // else, ignore msg
		else {
			System.out.println(">>> not subscribed: " + jsonObject.toString());
		}
	}
}
