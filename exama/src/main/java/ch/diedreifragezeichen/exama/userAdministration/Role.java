package ch.diedreifragezeichen.exama.userAdministration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

    public static enum definedRoles {
        
        SYSTEMADMIN("Systemadministrator"),
        ADMIN("Administrator"),
        REFERENCESTUDENT("Referenzschüler"),
        STUDENT("Schüler");
        
        private final String displayName;
        
        definedRoles(String displayName) {
            this.displayName = displayName;
        }
    
        public String getDisplayName() {
            return displayName;
        }
    }

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleID;
	
    @Column(name = "role_name")
	private String roleName;

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
		
}
