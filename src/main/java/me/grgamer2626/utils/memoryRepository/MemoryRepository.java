package me.grgamer2626.utils.memoryRepository;

import me.grgamer2626.utils.Identifiable;
import me.grgamer2626.utils.memoryRepository.exceptions.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface MemoryRepository<T extends Identifiable<ID>, ID extends Number> {
	
	/**
	 * Returns the cached repository. The id in this repository must be an
	 * instance of Number.
	 *
	 * @return map with cached repository
	 */
	Map<ID, T> getRepository();
	
	/**
	 * Save entity in the repository if the repository does not contain it yet.
	 * The entity is verified by its id number.
	 *
	 * @param entity entity to save in the repository
	 * @return entity saved in the repository
	 * @throws RepositoryException if the repository already contains the entity.
	 */
	default T save(T entity) throws RepositoryException {
		if(exist(entity.getId())) throw new RepositoryException("The repository already contains the specified entity!");
		
		getRepository().put(entity.getId(), entity);
		return entity;
	}
	
	/**
	 * Returns the entity with specified id from the repository.
	 *
	 * @param id id of the entity to find
	 * @return entity with specified id
	 * @throws IllegalArgumentException if the repository does not contain entity with specified id
	 */
	default T findById(ID id) throws IllegalArgumentException {
		T entity = getRepository().get(id);
		
		if(entity == null) throw new IllegalArgumentException("The repository does not contains entity with specified id");
		
		return entity;
	}
	
	/**
	 * Returns list of all entities in the repository.
	 *
	 * @return list of all entities in the repository
	 */
	default List<T> findAll() {
		return new ArrayList<>(getRepository().values());
	}
	
	/**
	 * Returns true if the repository contains Id.
	 *
	 * @param id id of the entity
	 * @return true if the repository contains ID, otherwise false
	 */
	default boolean exist(ID id) {
		return getRepository().containsKey(id);
	}
	
	/**
	 * Removes entity with specified id from the repository, if the repository contains such id.
	 *
	 * @param id id of entity to remove
	 */
	default void removeById(ID id) {
		if(exist(id)) {
			getRepository().remove(id);
		}
	}
}
