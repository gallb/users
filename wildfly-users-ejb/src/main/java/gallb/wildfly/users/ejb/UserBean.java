package gallb.wildfly.users.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import gallb.wildfly.users.common.IUser;
import model.User;
import model.Role;

@Stateless
public class UserBean implements IUser{

	@PersistenceContext(unitName = "WildflyUsers")
	private EntityManager oEntityManager;
	
	@Override
	public List<User> getAllUsers() {
		CriteriaBuilder cb = oEntityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> member = criteria.from(User.class);
		criteria.select(member).orderBy(cb.asc(member.get("username")));
		return oEntityManager.createQuery(criteria).getResultList();
		//return (List<User>)oEntityManager.createNamedQuery("User.findAll").getResultList();
	}

	/*@Override
	public List<Role> getRoles() {
		
		return null;
	}*/

}
