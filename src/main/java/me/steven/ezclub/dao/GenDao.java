package me.steven.ezclub.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public abstract class GenDao<T> {

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	/**
	 * Save an object
	 * @param t
	 */
	public void save(T t) {
		this.mongoTemplate.save(t);
	}
	
	/**
	 * Remove an object
	 * @param t
	 */
	public void delete(T t) {
		this.mongoTemplate.remove(t);
	}
	
	/**
	 * Return document list by the query
	 * @param query
	 */
	public List<T> findList(Query query) {
		return this.mongoTemplate.find(query, this.getEntityClass());
	}
	
	/**
	 * Return document by the query
	 * @param query
	 * @return
	 */
	public T findOne(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());
	}
	
	/**
	 * Return the count of the query
	 * @param query
	 * @return
	 */
	public Long count(Query query) {
		return this.mongoTemplate.count(query, this.getEntityClass());
	}

	/**
	 * Pagination from document
	 * @param query
	 * @param start
	 * @param size
	 * @param url, format -> "a/b/c/"
	 * @return
	 */
	/*public Pagination<T> queryPage(Query query, int start, int size, String url) {
		Pagination<T> page = new Pagination<T>((start / size) + 1, size, this.mongoTemplate.count(query, this.getEntityClass()));
		query.skip(start);
		query.limit(size);
		page.make(mongoTemplate.find(query, this.getEntityClass()), url);
		return page;
	}*/
	
	/**
	 * Count document total page
	 * @param query
	 * @return
	 */
	public Long getPageCount(Query query) {
		return this.mongoTemplate.count(query, this.getEntityClass());
	}
	
	/**
	 * Update document by the query
	 * @param query
	 * @param update
	 */
	public void update(Query query, Update update) {
		this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
	}
	
	/**
	 * Delete document by the query
	 * @param query
	 */
	public void delete(Query query) {
		this.mongoTemplate.remove(query, this.getEntityClass());
	}
	
	/**
	 * Return reflected object's class
	 * @return
	 */
	protected abstract Class<T> getEntityClass();
	
}
