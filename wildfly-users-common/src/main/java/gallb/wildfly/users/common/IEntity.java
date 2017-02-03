package gallb.wildfly.users.common;

import java.util.List;

import model.BaseEntity;
/**
 * 
 * @author gallb
 *
 * Defines persistence operations, for entities.
 *
 * @param <X> Specifies entity type.
 */
public interface IEntity<X extends BaseEntity> {
	/**
	 * 
	 * @return List containing all entities.
	 */
	public List<X> getAll();
	
	/**
	 * 
	 * @param p_searchTxt String to search for
	 * @return List of of entities found, empty list if nothing found.
	 */
	public List<X> search(String p_searchTxt);
	
	/**
	 * 
	 * @param p_id Id of entity
	 * @return Search result entity, null if nothing found.
	 */
	public X getById(String p_id);
	
	/**
	 * 
	 * @param p_value String with new value.
	 * @return True if operation successful, false if not.
	 */
	public boolean store(String p_value);
	
	/**
	 * 
	 * @param p_newTxt String with new value.
	 * @return True if operation successful, false if not.
	 */
	public boolean update(String p_id, String p_newTxt);
	
	/**
	 * 
	 * @param p_id Id of entity that should be removed from persistence.
	 * @return True if operation successful, false if not.
	 * @throws BeanException 
	 */
	public boolean remove(String p_id) throws BeanException;
}
