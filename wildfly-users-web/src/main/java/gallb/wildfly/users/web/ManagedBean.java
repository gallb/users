/**
 * 
 */
package gallb.wildfly.users.web;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import gallb.wildfly.users.common.IUser;
import model.User;

/**
 * @author gallb
 *
 */
@Named("userbean")
@ApplicationScoped
public class ManagedBean implements Serializable, IUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4702598250751689454L;
	private IUser oUserBean = null;
	
	
	@Override
	public List<User> getAllUsers() {
		return getUserBean().getAllUsers();
	}
	
	private IUser getUserBean() {
		if (oUserBean == null) {
			try {
				InitialContext jndi = new InitialContext();
				oUserBean = (IUser) jndi.lookup(IUser.jndiNAME);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("getUserBean*****************************************");
		return oUserBean;
	}
	
}
