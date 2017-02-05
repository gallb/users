package gallb.wildfly.users.common;

import model.Role;


public interface IRole extends IEntity<Role>{
	public static final String jndiNAME="java:global/wildfly-users-ear-0.0.1-SNAPSHOT/wildfly-users-ejb-0.0.1-SNAPSHOT/RoleBean";
}
