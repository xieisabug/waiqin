package hn.join.fieldwork.security;

import hn.join.fieldwork.domain.Permission;
import hn.join.fieldwork.domain.User;
import hn.join.fieldwork.persistence.RoleMapper;
import hn.join.fieldwork.persistence.UserMapper;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
/**
 * 自定义的Shiro Realm
 * @author chenjinlong
 *
 */
@Component("myAuthorizingRealm")
public class MyAuthorizingRealm extends AuthorizingRealm {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	private TransformPermissionAsShiroFunction transformPermissionFunction = new TransformPermissionAsShiroFunction();
	/**
	 * 编程式声明Shiro Realm使用自定义的MyAuthorizingRealm
	 */
	public MyAuthorizingRealm() {
		setName("myAuthorizingRealm"); // This name must match the name
										// in the User class's
										// getPrincipals() method
//		HashedCredentialsMatcher cm = new Sha256CredentialsMatcher();
//		cm.setHashIterations(10);
//		setCredentialsMatcher(cm);
		setCredentialsMatcher(new HashedCredentialsMatcher("md5"));
	}
	/**
	 * 授权实现
	 * 而授权实现则与认证实现非常相似,
	 * </br>在我们自定义的Realm中，重载doGetAuthorizationInfo()方法,
	 * </br>重写获取用户权限的方法即可
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		Long userId = (Long) principals.fromRealm(getName()).iterator().next();
		User user = userMapper.getById(userId);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			info.addStringPermissions(Collections2.transform(user.getRole()
					.getPermissions(), transformPermissionFunction));
			return info;
		} else {
			return null;
		}
	}
	/**
	 * Shiro认证实现,通常步骤为
	 * </br>1、检查提交的进行认证的令牌信息 
	 * </br>2、根据令牌信息从数据源(通常为数据库)中获取用户信息 
	 * </br>3、对用户信息进行匹配验证。 
	 * </br>4、验证通过将返回一个封装了用户信息的AuthenticationInfo实例。 
	 * </br>5、验证失败则抛出AuthenticationException异常信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userMapper.getByUsername(token.getUsername());
		if (user != null) {
			return new SimpleAuthenticationInfo(user.getId(),
					user.getPassword(), getName());
		} else {
			return null;
		}
	}
	/**
	 * 将SHIRO PERMISSION对象转化成“resource:operation”格式字符串
	 * @author chenjinlong
	 *
	 */
	private static class TransformPermissionAsShiroFunction implements
			Function<Permission, String> {

		@Override
		public String apply(Permission input) {
			return input.toShiroFormat();
		}

	}

}
