/**
 * 
 */
package gallb.wildfly.users.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import gallb.wildfly.users.common.BeanException;
import gallb.wildfly.users.common.IRole;
import model.Role;
import model.User;

/**
 * @author gallb
 *
 */
@Stateless
public class RoleBean implements IRole{
	private Logger oLogger = Logger.getLogger(RoleBean.class);
	
	@PersistenceContext(unitName = "WildflyUsers")
	private EntityManager oEntityManager;

	@Override
	public List<Role> getAll() throws BeanException {
		CriteriaBuilder cb = oEntityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteria = cb.createQuery(Role.class);
		Root<Role> member = criteria.from(Role.class);
		criteria.select(member).orderBy(cb.asc(member.get("userrole")));
		List<Role> usrList = new ArrayList();
		try {
			usrList = oEntityManager.createQuery(criteria).getResultList();
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}	
		return usrList;
	}

	@Override
	public List<Role> search(String p_searchTxt) throws BeanException {
		CriteriaBuilder cb = oEntityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteria = cb.createQuery(Role.class);
		Root<Role> member = criteria.from(Role.class);
		criteria.select(member).where(cb.like(member.get("userrole"), "%"+p_searchTxt+"%")).orderBy(cb.asc(member.get("userrole")));
		List<Role> tmpUserList = new ArrayList<>();
		try {
			tmpUserList = oEntityManager.createQuery(criteria).getResultList();
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}	
		return tmpUserList;
	}

	@Override
	public Role getById(String p_id) throws BeanException {
		oLogger.info("--search role by Id RoleBean--");
		try {
			return oEntityManager.find(Role.class, p_id);
		}  catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}	
	}

	@Override
	public Role store(String p_value) throws BeanException {
		Role tmpUsr = new Role();
		tmpUsr.setUserrole(p_value);
		try {
			oEntityManager.persist(tmpUsr);
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}	
		return tmpUsr;	
	}

	@Override
	public boolean update(String p_id, String p_newTxt) throws BeanException {
		Role tmpUsr = oEntityManager.find(Role.class, p_id);
		if (tmpUsr == null) {
			oLogger.info("--delete by Id RoleBean didn't find user--");
			throw new BeanException("Didn't find entity by id");
		}
		oLogger.info("--update by Id RoleBean - role found - call update--");
		try{
			tmpUsr.setUserrole(p_newTxt);
			//oEntityManager.merge(tmpUsr);	
			oEntityManager.flush();
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		} catch (PersistenceException e) {
			oLogger.error(e);
			throw new BeanException("Please verify if given name is not taken.");
		} 
		return true;
	}

	@Override
	public boolean remove(String p_id) throws BeanException {
		Role tmpUsr = oEntityManager.find(Role.class, p_id); 
		if (tmpUsr == null) {
			oLogger.error("--delete by Id RoleBean didn't find Role--");
			throw new BeanException("Didn't find entity by id");
		}
		oLogger.info("--delete by Id Role - Role found - call delete--");
		try {
			oEntityManager.remove(tmpUsr);
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}
		return true;
	}
}
