package gallb.wildfly.users.common;

import java.util.List;

import model.BaseEntity;
import model.User;
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
	 * @throws BeanException 
	 */
	public List<X> getAll() throws BeanException;
	
	/**
	 * 
	 * @param p_searchTxt String to search for
	 * @return List of of entities found, empty list if nothing found.
	 * @throws BeanException 
	 */
	public List<X> search(String p_searchTxt) throws BeanException;
	
	/**
	 * 
	 * @param p_id Id of entity
	 * @return Search result entity, null if nothing found.
	 * @throws BeanException 
	 */
	public X getById(String p_id) throws BeanException;
	
	/**
	 * 
	 * @param p_value String with new value.
	 * @return True if operation successful, false if not.
	 * @throws BeanException 
	 */
	public X store(String p_value) throws BeanException;
	
	/**
	 * 
	 * @param p_newTxt String with new value.
	 * @return True if operation successful, false if not.
	 * @throws BeanException 
	 */
	public boolean update(String p_id, String p_newTxt) throws BeanException;
	
	/**
	 * 
	 * @param p_id Id of entity that should be removed from persistence.
	 * @return True if operation successful, false if not.
	 * @throws BeanException 
	 */
	public boolean remove(String p_id) throws BeanException;
}
