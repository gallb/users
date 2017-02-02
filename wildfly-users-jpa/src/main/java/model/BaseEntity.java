/**
 * 
 */
package model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

/**
 * @author gallb
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3333408811149331428L;
	@Id
	private String uuid;

	public String getUuid() {
		if (this.uuid == null) {
			ensureUuid();
		}
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}	
	
	@PrePersist
	public void onPrePersist() {
		ensureUuid();
	}
	
	private void ensureUuid() {
		if (uuid == null) {
			setUuid (UUID.randomUUID().toString());
		}
	}
}
