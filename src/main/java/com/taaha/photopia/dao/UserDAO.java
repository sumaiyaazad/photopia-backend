package com.taaha.photopia.dao;

import java.util.List;

import com.taaha.photopia.entity.User;

public interface UserDAO {

	List<User> findAll();
	
	User findById(int theId);
	
	void save(User theUser);
	
	void deleteById(int theId);

	void deleteByName(String  theName);

	User getUserByName(String theName);

	void insertToken(String theUsername, String theToken);

	User getUserByToken(String theToken);

	void removeToken(String theToken);

	void removeTokenForUser(String theUsername);

	Boolean changePasswordForUser(User theUser, String theNewPassword);

    void registerUser(User theUser);

	boolean verifyUser(String theVerificationCode);

	User getUserByEmail(String theEmail);

	User verifyAccountRecovery(String theVerificationCode);
}
