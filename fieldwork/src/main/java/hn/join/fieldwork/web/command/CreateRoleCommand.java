package hn.join.fieldwork.web.command;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

public class CreateRoleCommand {

	private String roleName;

	private String permissions;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public Set<String> getPermissionSet() {
		if (!StringUtils.isEmpty(permissions)) {
			return Sets.newHashSet(StringUtils.split(permissions, ","));
		}
		return Collections.emptySet();
	}

}
