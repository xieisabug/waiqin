/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package hn.join.fieldwork.web.interceptor;

import hn.join.fieldwork.domain.Permission;
import hn.join.fieldwork.domain.Role;
import hn.join.fieldwork.domain.User;
import hn.join.fieldwork.service.UserService;
import hn.join.fieldwork.utils.JsonUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Lists;

/**
 * A Spring MVC interceptor that adds the currentUser into the request as a
 * request attribute before the JSP is rendered. This operation is assumed to be
 * fast because the User should be cached in the Hibernate second-level cache.
 */
@Component
public class CurrentUserInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;

	@Override
	public void postHandle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
		// Add the current user into the request
		User currentUser = userService.getCurrentUser();

		if (currentUser != null) {
			Role userRole = currentUser.getRole();
			httpServletRequest.setAttribute("currentUser", currentUser);
			httpServletRequest.setAttribute("roleName", userRole.getRoleName());
			httpServletRequest.setAttribute("rolePermission",
					JsonUtil.toJson(toPermissionString(userRole.getPermissions())));
		}
	}

	private List<String> toPermissionString(Iterable<Permission> permissions) {
		List<String> permissionList=Lists.newArrayList();
		for (Permission p : permissions) {
			permissionList.add(p.toShiroFormat());
		}
		return permissionList;
	}
}
