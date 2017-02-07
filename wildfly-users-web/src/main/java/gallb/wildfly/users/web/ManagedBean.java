/**
 * 
 */
package gallb.wildfly.users.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import gallb.wildfly.users.common.IUser;
import gallb.wildfly.users.common.BeanException;
import gallb.wildfly.users.common.IRole;
import model.Role;
import model.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
/**
 * @author gallb
 *
 */
@Named("userbean")

//@ApplicationScoped
//@RequestScoped
@SessionScoped
//@ViewScoped
public class ManagedBean implements Serializable{

	/**
	 * 
	 */
	private Logger oLogger = Logger.getLogger(ManagedBean.class);
	private static final long serialVersionUID = -4702598250751689454L;
	private IUser oUserBean = null;
	private IRole oRoleBean = null;
	private List<User> userList =  new ArrayList<>();
	private List<Role> roleList = null;
	private String selectedUser = null;
	private String selectedRole = null;
	private User currentUser = null;
	
	
	
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public void showRoles() {
		currentUser = getById(selectedUser);
		oLogger.info("Current user:   " + currentUser.getUsername() + " " +currentUser.getRoles().get(0));
	}
	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public String getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}

	public void error(String message) {
		oLogger.info("**********************Error CALLED***************************");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", message));
    }
	
	public void info(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }
     
    public void warn(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", message));
    }
     
    public void fatal(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", message));
    }
	
  //  @PostConstruct
    public void onPostConstruct() {
    	System.out.println("*************************************postconstruct");
    	userList = new ArrayList<>();
		try {
			userList = getUserBean().getAll();
		} catch (BeanException e) {
			this.error("Server internal error.");
		}
    }
    
	public List<User> getAll() {
		oLogger.info("--getAllUsers()--");
		userList = new ArrayList<>();
		try {
			oLogger.info("--getAllUsers()--users queried");
			userList = getUserBean().getAll();
		} catch (BeanException e) {
			this.error("Server internal error.");
		}
		return userList;
	}
	
	public List<Role> getAllRoles() {
		oLogger.info("--getAllUsers()--");
		userList = new ArrayList<>();
		try {
			oLogger.info("--getAllUsers()--users queried");
			roleList = getRoleBean().getAll();
		} catch (BeanException e) {
			this.error("Server internal error.");
		}
		return roleList;
	}
	
	private IUser getUserBean() {
		if (oUserBean == null) {
			try {
				InitialContext jndi = new InitialContext();
				oUserBean = (IUser) jndi.lookup(IUser.jndiNAME);
			} catch (NamingException e) {
				this.error("Server internal error.");
			}
		}
		return oUserBean;
	}
	
	private IRole getRoleBean() {
		if (oRoleBean == null) {
			try {
				InitialContext jndi = new InitialContext();
				oRoleBean = (IRole) jndi.lookup(IRole.jndiNAME);
			} catch (NamingException e) {
				this.error("Server internal error.");
			}
		}
		return oRoleBean;
	}

	public boolean store(String p_value) {
		oLogger.info("--store user--");
		oLogger.info("--store param: " + p_value);
		if (p_value.isEmpty()) {
			this.error("Empty field");
			return false;
		}
		if (p_value == "") {
			this.error("Empty field");
			return false;
		}
		try {
			userList.add(getUserBean().store(p_value));
			this.info("Succesfully added: " + p_value);
		} catch (BeanException e) {
			this.error(e.getMessage());
		}
		return false;
	}

	public boolean storeRole(String p_value) {
		oLogger.info("--store role--");
		oLogger.info("--store param: " + p_value);
		if (p_value.isEmpty()) {
			this.error("Empty field");
			return false;
		}
		if (p_value == "") {
			this.error("Empty field");
			return false;
		}
		try {
			roleList.add(getRoleBean().store(p_value));
			this.info("Succesfully added: " + p_value);
		} catch (BeanException e) {
			this.error(e.getMessage());
		}
		return false;
	}

	public List<User> search(String p_searchTxt) {
		oLogger.info("--search user--" + p_searchTxt);
		userList = new ArrayList<>();
		try {
			userList = getUserBean().search(p_searchTxt);
		} catch (BeanException e) {
			this.error(e.getMessage());
		}		
		return userList;
	}
	
	public List<Role> searchRole(String p_searchTxt) {
		oLogger.info("--search Role--" + p_searchTxt);
		roleList = new ArrayList<>();
		try {
			roleList = getRoleBean().search(p_searchTxt);
		} catch (BeanException e) {
			this.error(e.getMessage());
		}		
		return roleList;
	}
	
	public List<User> getUserList() {
		return userList;
	}
	
	public List<Role> getRoleList() {
		return roleList;
	}

	public boolean update(String p_newTxt) {
		oLogger.info("--update user ManagedBean--id:" + selectedUser + "new name: " +p_newTxt);
		if ((selectedUser != null) && (p_newTxt.length() >= 3)) {
			//if ()
			try {
				getUserBean().update(selectedUser, p_newTxt);
				oLogger.info("**********************update succesfull************************************");
				this.info("Update succesfull.");
			} catch (BeanException e) {
				oLogger.error(e);
				this.error(e.getMessage());
			}
		} else {
			this.error("New name too short.");
		}
		return true;
	}
	
	public boolean updateRole(String p_newTxt) {
		oLogger.info("--update role ManagedBean--id:" + selectedRole + "new name: " +p_newTxt);
		if ((selectedRole != null) && (p_newTxt.length() >= 3)) {
			try {
				getRoleBean().update(selectedRole, p_newTxt);
				oLogger.info("**********************update succesfull************************************");
				this.info("Update succesfull.");
			} catch (BeanException e) {
				oLogger.error(e);
				this.error(e.getMessage());
			}
		} else {
			this.error("New name too short.");
		}
		return true;
	}

	public boolean remove() {
		oLogger.info("--remove user by Id ManagedBean--p_id: " + selectedUser);
		if (selectedUser == "") {
			this.error("Empty field");
			return false;
		} else {
			try {
				getUserBean().remove(selectedUser);
				userList = getUserBean().getAll();
				selectedUser = userList.get(0).getUuid(); 
				this.info("Delete succesfull.");
			} catch (BeanException e) {
				oLogger.error(e);
				this.error(e.getMessage());
			}
		}
		return true;
	}
	
	public boolean removeRole(String p_id) {
		oLogger.info("--remove user by Id ManagedBean--p_id: " + selectedUser);
		if (selectedRole == "") {
			this.error("Empty field");
			return false;
		} else {
			try {
				getUserBean().remove(selectedUser);
				userList = getUserBean().getAll();
				selectedUser = userList.get(0).getUuid(); 
				this.info("Delete succesfull.");
			} catch (BeanException e) {
				oLogger.error(e);
				this.error(e.getMessage());
			}
		}
		return true;
	}

	public User getById(String p_id) {
		oLogger.info("--search user by Id ManagedBean--");
		try {
			return getUserBean().getById(p_id);
		}  catch (BeanException e) {
			oLogger.error(e);
			this.error(e.getMessage());
		}
		return null;
	}
	
	public Role getRoleById(String p_id) {
		oLogger.info("--search user by Id ManagedBean--");
		try {
			return getRoleBean().getById(p_id);
		}  catch (BeanException e) {
			oLogger.error(e);
			this.error(e.getMessage());
		}
		return null;
	}
	
	public boolean assignRole(String p_userId, String p_roleId) {
		oLogger.info("--assign role ManagedBean--");
		if (p_userId == "") {
			this.error("Empty field");
			return false;
		}
		if (p_roleId == "") {
			this.error("Empty field");
			return false;
		}
		try {
			getUserBean().addRole(p_userId, p_roleId);
		} catch (BeanException e) {
			oLogger.error(e);
			this.error(e.getMessage());
		}
		return true;
	}
	
	public boolean removeRole(String p_userId, String p_roleId) {
		oLogger.info("--revoke role ManagedBean--");
		if (p_userId == "") {
			this.error("Empty field");
			return false;
		}
		if (p_roleId == "") {
			this.error("Empty field");
			return false;
		}
		try {
			getUserBean().removeRole(p_userId, p_roleId);
		} catch (BeanException e) {
			oLogger.error(e);
			this.error(e.getMessage());
		}
		return true;
	}
	
}
