package gallb.wildfly.users.ejb;

import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gallb.wildfly.users.common.BeanException;
import gallb.wildfly.users.common.IEntity;
import gallb.wildfly.users.common.IUser;
import model.Role;
import model.User;

@Stateless
public class UserBean implements IUser{

	private Logger oLogger = Logger.getLogger(UserBean.class);
	
	@PersistenceContext(unitName = "WildflyUsers")
	private EntityManager oEntityManager;
	
	@Override
	public List<User> getAll() throws BeanException {
		CriteriaBuilder cb = oEntityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> member = criteria.from(User.class);
		criteria.select(member).orderBy(cb.asc(member.get("username")));
		List<User> usrList = new ArrayList();
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
	public User store(String p_value) throws BeanException {
		List<User> listByName  = this.search(p_value);
		if (!listByName.isEmpty()) {
			oLogger.error("Username allready exists.");
			throw new BeanException("Username allready exists.");
		}
		User tmpUsr = new User();
		tmpUsr.setUsername(p_value);
		try {
			oEntityManager.persist(tmpUsr);
		} catch (EntityExistsException e) {
			oLogger.error(e);
			throw new BeanException("Username allready exists.");
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
	public List<User> search(String p_searchTxt) throws BeanException {
		CriteriaBuilder cb = oEntityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> member = criteria.from(User.class);
		criteria.select(member).where(cb.like(member.get("username"), "%"+p_searchTxt+"%")).orderBy(cb.asc(member.get("username")));
		List<User> tmpUserList = new ArrayList<>();
		try {
			tmpUserList = oEntityManager.createQuery(criteria).getResultList();
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}	
		//oLogger.info("Users found for searchstring: " + p_searchTxt);
		//tmpUserList.forEach(e -> oLogger.info(e.getUsername()));
		return tmpUserList;
	}

	@Override
	public boolean update(String p_id, String p_newTxt) throws BeanException{
		User tmpUsr = oEntityManager.find(User.class, p_id);
		if (tmpUsr == null) {
			oLogger.info("--delete by Id UserBean didn't find user--");
			throw new BeanException("Didn't find entity by id");
		}
		oLogger.info("--update by Id UserBean - user found - call update--");
		try{
			tmpUsr.setUsername(p_newTxt);
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
	public boolean remove(String p_id) throws BeanException{
		User tmpUsr = oEntityManager.find(User.class, p_id); 
		if (tmpUsr == null) {
			oLogger.error("--delete by Id UserBean didn't find user--");
			throw new BeanException("Didn't find entity by id");
		}
		oLogger.info("--delete by Id UserBean - user found - call delete--");
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

	@Override
	public User getById(String p_id) throws BeanException {
		oLogger.info("--search user by Id UserBean--");
		try {
			return oEntityManager.find(User.class, p_id);
		}  catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}	
	}

	@Override
	public boolean addRole(String p_userId, String p_roleId) throws BeanException {
		User tmpUsr = oEntityManager.find(User.class, p_userId); 
		if (tmpUsr == null) {
			oLogger.info("--addRole UserBean didn't find user--");
			throw new BeanException("Didn't find entity by id");
		}
		Role tmpRole = oEntityManager.find(Role.class, p_roleId);
		if (tmpRole == null) {
			oLogger.info("--addRole by Id UserBean didn't find role--");
			throw new BeanException("Didn't find entity by id");
		}
		oLogger.info("--add role UserBean - user found - call update--");
		try{
			List<Role> tmpRoleList= tmpUsr.getRoles();
			tmpRoleList.add(tmpRole);
			tmpUsr.setRoles(tmpRoleList);
			oEntityManager.merge(tmpUsr);	
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}
		return true;
	}

	@Override
	public boolean removeRole(String p_userId, String p_roleId) throws BeanException {
		User tmpUsr = oEntityManager.find(User.class, p_userId); 
		if (tmpUsr == null) {
			oLogger.info("--addRole UserBean didn't find user--");
			throw new BeanException("Didn't find entity by id");
		}
		oLogger.info("--remove role UserBean - user found - call update--");
		List<Role> tmpRoleList= tmpUsr.getRoles();
		oLogger.info("-----------------------" + tmpRoleList.size());
		for (int i = 0; i < tmpRoleList.size(); i++) {
			oLogger.info(p_roleId + " == " + tmpRoleList.get(i).getUuid());
			if (tmpRoleList.get(i).getUuid().equals(p_roleId)) {
				oLogger.info("belepett");
				tmpRoleList.remove(tmpRoleList.get(i));
			}
		}
		tmpUsr.setRoles(tmpRoleList);
		try {
			oLogger.error("************************************IDE ELJUT************************");
			//oEntityManager.merge(tmpUsr);	
			oEntityManager.flush();
		} catch (IllegalArgumentException e) {
			oLogger.error(e);
			throw new BeanException("Illegal argument.");
		} catch (TransactionRequiredException e) {
			oLogger.error(e);
			throw new BeanException("Transaction error.");
		}
		return false;
	}
}
