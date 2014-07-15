package hn.join.fieldwork.domain;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.collect.Lists;
/**
 * 角色对象
 * @author chenjinlong
 *
 */
public class Role extends BaseDomain {
	
	
	
	private static final long serialVersionUID = 5270835859945121741L;
	/**主键*/
	private Integer id;
	/**角色编码*/
	private String roleName;
	/**创建时间*/
	private Date createTime;
	
	@JsonSerialize(using=CustomPermissionSerializer.class)  
	private Set<Permission> permissions;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}
	
	
	public static class CustomPermissionSerializer extends JsonSerializer<Set<Permission>>{

		@Override
		public void serialize(Set<Permission> permissions, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeStartArray();
			for(Permission p:permissions){
				jgen.writeString(p.toShiroFormat());
			}
			jgen.writeEndArray();
			
		}
		
	}
	
	

}
