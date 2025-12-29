package service;

import dao.UserDAO;
import model.User;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

public class UserService {
	private UserDAO dao = new UserDAO();
	
	public List<User> fetchUsers() {
		List<User> users = new ArrayList<>();
		try {
			String xmlData = dao.getUsersXML();
			if (xmlData == null) return users;
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes("UTF-8")));
			
			NodeList userList = doc.getElementsByTagName("user");
			for (int i = 0; i < userList.getLength(); i++) {
				Element u = (Element) userList.item(i);
				int id = Integer.parseInt(u.getElementsByTagName("id").item(0).getTextContent());
				String username = u.getElementsByTagName("username").item(0).getTextContent();
				String role = u.getElementsByTagName("role").item(0).getTextContent();
				String pref = u.getElementsByTagName("preference").item(0).getTextContent();
				
				users.add(new User(id, username, role, pref));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	public void addUser(String username, String password, String role, String preference) {
		dao.addUser(username, password, role, preference);
	}
}	
		