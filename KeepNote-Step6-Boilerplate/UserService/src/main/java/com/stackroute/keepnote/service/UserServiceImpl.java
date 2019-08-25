package com.stackroute.keepnote.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the UserRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	private UserRepository userRepository;
	/*
	 * This method should be used to save a new user.Call the corresponding method
	 * of Respository interface.
	 */

	public User registerUser(User user) throws UserAlreadyExistsException {
		User dbUser = userRepository.insert(user);
		if(dbUser != null) {
			return dbUser;
		} else {
			throw new UserAlreadyExistsException("The user is already available for #id " + user.getUserId() );
		}
	}

	/*
	 * This method should be used to update a existing user.Call the corresponding
	 * method of Respository interface.
	 */

	public User updateUser(String userId,User user) throws UserNotFoundException {
		Optional<User> dbUser = userRepository.findById(userId);
		if(dbUser.isPresent()) {
			userRepository.save(dbUser.get());
		} else {
			throw new UserNotFoundException("The user is not found for #id " + userId );
		}
		return user;
	}

	/*
	 * This method should be used to delete an existing user. Call the corresponding
	 * method of Respository interface.
	 */

	public boolean deleteUser(String userId) throws UserNotFoundException {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			userRepository.delete(user.get());
		} else {
			throw new UserNotFoundException("The user is not found for #id " + userId );
		}
		return true;
	}

	/*
	 * This method should be used to get a user by userId.Call the corresponding
	 * method of Respository interface.
	 */

	public User getUserById(String userId) throws UserNotFoundException {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			return user.get();
		} else {
			throw new UserNotFoundException("The user is not found for #id " + userId );
		}
	}

}
