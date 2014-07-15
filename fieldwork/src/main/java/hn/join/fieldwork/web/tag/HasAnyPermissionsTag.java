package hn.join.fieldwork.web.tag;

import org.apache.shiro.web.tags.PermissionTag;

public class HasAnyPermissionsTag extends PermissionTag {

	/**
	 * Shiro 标签扩展，用于判断具有任意权限的情况
	 */
	private static final long serialVersionUID = 5206691036963410577L;
	
	private static final String PERMISSIONS_DELIMETER = ",";

	@Override
	protected boolean showTagBody(String permissions) {
		String[] permissionArray = permissions.split(PERMISSIONS_DELIMETER);
		for (String p : permissionArray) {
			boolean isPermitted = getSubject() != null
					&& getSubject().isPermitted(p);
			if (isPermitted)
				return true;
		}
		return false;
	}

}
