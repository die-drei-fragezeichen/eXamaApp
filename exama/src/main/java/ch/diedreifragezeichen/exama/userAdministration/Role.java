package ch.diedreifragezeichen.exama.userAdministration;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleID;
	
    @Column(name = "role_name")
	private String roleName;

    @Override
    public String toString(){
        return this.roleName;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        Role role = (Role) obj;
        if(this.roleID == role.getRoleID()){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.roleID == null) ? 0 : this.roleID.hashCode());
        return result;
    }

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
		
}
