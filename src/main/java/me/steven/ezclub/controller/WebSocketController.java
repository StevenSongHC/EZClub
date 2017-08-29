package me.steven.ezclub.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import me.steven.ezclub.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("websocket")
public class WebSocketController {

	@Autowired
	private UserService uService;
	@Autowired
	private SimpMessagingTemplate template;

	@RequestMapping("webchat")
	public String webchat(ModelMap model,
						  HttpSession session,
						  @RequestParam String roomId,
						  @RequestParam String username) {
		session.setAttribute("ROOMID", roomId);	
		session.setAttribute("USERNAME", username);
		model.put("roomId", roomId);
		model.put("username", username);
		return "webchat";
	}

	/*@MessageMapping("webchat.send")
	public void chatSend(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("username", data.get("username"));
		result.put("message", data.get("message"));
		template.convertAndSend("topic/receiver/" + data.get("roomId"), result);
	}*/
	
	@MessageMapping("message.send")
	public void messageSend(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		template.convertAndSend("queue/message/" + data.get("addresseeId"), result);
	}
	
	@MessageMapping("chat.send")
	public void chatSend(Map<String, Object> data) {
		data.put("type", 3);		// new message sent
		template.convertAndSend("topic/edit_activity/" + data.get("activityId"), data);
	}
	
}
