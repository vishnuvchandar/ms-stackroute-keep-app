package com.stackroute.keepnote.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.repository.CategoryRepository;

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
public class CategoryServiceImpl implements CategoryService {

	/*
	 * Autowiring should be implemented for the CategoryRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	private CategoryRepository categoryRepository;
	/*
	 * This method should be used to save a new category.Call the corresponding
	 * method of Respository interface.
	 */
	public Category createCategory(Category category) throws CategoryNotCreatedException {
		
		Category savedCategory = categoryRepository.insert(category);
		if(savedCategory == null) {
			throw new CategoryNotCreatedException("The category is not created for #id" +  category.getId());
		}
		return savedCategory;
	}

	/*
	 * This method should be used to delete an existing category.Call the
	 * corresponding method of Respository interface.
	 */
	public boolean deleteCategory(String categoryId) throws CategoryDoesNoteExistsException {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if(category.isPresent()) {
		 categoryRepository.delete(category.get());
		} else {
			throw new CategoryDoesNoteExistsException("The category does not exist for #id" +  categoryId);
		}
		return true;
	}

	/*
	 * This method should be used to update a existing category.Call the
	 * corresponding method of Respository interface.
	 */
	public Category updateCategory(Category category, String categoryId) {
		Optional<Category> dbCategory = categoryRepository.findById(categoryId);
		if(dbCategory.isPresent()) {
			categoryRepository.save(category);
		} 
		return category;
	}

	/*
	 * This method should be used to get a category by categoryId.Call the
	 * corresponding method of Respository interface.
	 */
	public Category getCategoryById(String categoryId) throws CategoryNotFoundException {
		Optional<Category> category = null;
		try {
			category = categoryRepository.findById(categoryId);
		} catch (Exception e) {
			throw new CategoryNotFoundException("The category does not exist for #id" +  categoryId);
		}
		if(category.isPresent()) {
			return category.get();
		} else {
			throw new CategoryNotFoundException("The category does not exist for #id" +  categoryId);
		}
	}

	/*
	 * This method should be used to get a category by userId.Call the corresponding
	 * method of Respository interface.
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		return categoryRepository.findAllCategoryByCategoryCreatedBy(userId);
	}

}
