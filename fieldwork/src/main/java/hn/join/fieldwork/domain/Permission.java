package hn.join.fieldwork.domain;
/**
 * 权限对象
 * @author chenjinlong
 *
 */
public class Permission extends BaseDomain {

	private static final long serialVersionUID = 349768239978976340L;
	/**资源*/
	private String resource;
	/**操作*/
	private String operation;

	public Permission(String resource, String operation) {
		super();
		this.resource = resource;
		this.operation = operation;
	}

	public Permission() {
		super();
	}

	public String toShiroFormat() {
		return resource + ":" + operation;
	}

	public String getResource() {
		return resource;
	}

	public String getOperation() {
		return operation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
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
		Permission other = (Permission) obj;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Permission [resource=").append(resource)
				.append(", operation=").append(operation).append("]");
		return builder.toString();
	}

	public static Permission createByShiroFormat(String shiroPermission) {
		Permission permission = null;
		if (shiroPermission.indexOf(":") != -1) {
			int colonPos = shiroPermission.indexOf(":");
			permission = new Permission(shiroPermission.substring(0, colonPos),
					shiroPermission.substring(colonPos + 1));
		}
		return permission;
	}

}
