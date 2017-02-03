package gallb.wildfly.users.ejb;

import java.util.List;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gallb.wildfly.users.common.BeanException;
import gallb.wildfly.users.common.IUser;
import model.User;

@Stateless
public class UserBean implements IUser{

	private Logger oLogger = Logger.getLogger(UserBean.class);
	
	@PersistenceContext(unitName = "WildflyUsers")
	private EntityManager oEntityManager;
	
	@Override
	public List<User> getAll() {
		CriteriaBuilder cb = oEntityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> member = criteria.from(User.class);
		criteria.select(member).orderBy(cb.asc(member.get("username")));
		return oEntityManager.createQuery(criteria).getResultList();
		//return (List<User>)oEntityManager.createNamedQuery("User.findAll").getResultList();
	}

	@Override
	public boolean store(String p_value) {
		User tmpUsr = new User();
		tmpUsr.setUsername(p_value);
		oEntityManager.persist(tmpUsr);
		return false;
	}

	@Override
	public List<User> search(String p_searchTxt) {
		CriteriaBuilder cb = oEntityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> member = criteria.from(User.class);

		criteria.select(member).where(cb.like(member.get("username"), "%"+p_searchTxt+"%")).orderBy(cb.asc(member.get("username")));
		List<User> tmpUserList = oEntityManager.createQuery(criteria).getResultList();
		oLogger.info("Users found for searchstring: " + p_searchTxt);
		tmpUserList.forEach(e -> oLogger.info(e.getUsername()));
		return tmpUserList;
	}

	@Override
	public boolean update(String p_id, String p_newTxt) {
		User tmpUsr = oEntityManager.find(User.class, p_id);
		if (tmpUsr == null) {
			oLogger.info("--delete by Id UserBean didn't find user--");
			return false;
		}
		tmpUsr.setUsername(p_newTxt);
		oEntityManager.merge(tmpUsr);
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
	public User getById(String p_id) {
		oLogger.info("--search user by Id UserBean--");
		return oEntityManager.find(User.class, p_id);
	}
}
