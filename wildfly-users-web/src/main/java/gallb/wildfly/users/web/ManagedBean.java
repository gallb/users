/**
 * 
 */
package gallb.wildfly.users.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import gallb.wildfly.users.common.IUser;
import gallb.wildfly.users.common.ManagedBeanException;
import gallb.wildfly.users.common.BeanException;
import model.User;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
/**
 * @author gallb
 *
 */
@Named("userbean")
@ApplicationScoped
public class ManagedBean implements Serializable{

	/**
	 * 
	 */
	private Logger oLogger = Logger.getLogger(ManagedBean.class);
	private static final long serialVersionUID = -4702598250751689454L;
	private IUser oUserBean = null;
	private List<User> userList = null;
	
	
	public void error(String message) {
		oLogger.info("**********************Error CALLED***************************");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", message));
    }
	//@Override
	public List<User> getAll() {
		oLogger.info("--getAllUsers()--");
		return getUserBean().getAll();
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
		return oUserBean;
	}

	//@Override
	public boolean store(String p_value) {
		oLogger.info("--store user--");
		return getUserBean().store(p_value);
	}

	//@Override
	public List<User> search(String p_searchTxt) {
		oLogger.info("--search user--" + p_searchTxt);
		userList = getUserBean().search(p_searchTxt);
		return userList;
	}
	
	public List<User> getUserList() {
		return userList;
	}

	//@Override
	public boolean update(String p_id, String p_newTxt) {
		oLogger.info("--update user ManagedBean--id:" + p_id + "new name: " +p_newTxt);
		if ((p_id != null) && (p_newTxt != null)) {
			return getUserBean().update(p_id, p_newTxt);
		}
		return false;
	}

	//@Override
	public boolean remove(String p_id) {
		oLogger.info("--remove user by Id ManagedBean--");
		try {
			getUserBean().remove(p_id);
		} catch (BeanException e) {
			oLogger.error(e);
			this.error(e.getMessage());
		}
		return true;
	}

	//@Override
	public User getById(String p_id) {
		oLogger.info("--search user by Id ManagedBean--");
		return getUserBean().getById(p_id);
	}
	
}
