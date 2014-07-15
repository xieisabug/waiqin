package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Permission;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * 权限字符串转换器，SHIRO权限格式与字符串格式之间的互相转换
 * SHIRO权限格式为"resource:operation"的格式
 * 数据库中的保存格式为“resource:operation，resource:operation，resource:operation.....”
 * TypeHandler为mybatis中用于将javabean中的字段与数据库中的字段类型互转的一个类
 * @author chenjinlong
 *
 */
public class PermissionStringTypeHandler implements TypeHandler<Set<Permission>> {

	private final Splitter _splitter = Splitter.on(",").omitEmptyStrings()
			.trimResults();

	private final Joiner _joiner = Joiner.on(",");
	
	@Override
	public void setParameter(PreparedStatement ps, int i,
			Set<Permission> parameter, JdbcType jdbcType) throws SQLException {
		Collection<String> shiroPermissions = Collections2.transform(parameter,
				new Function<Permission, String>() {

					@Override
					public String apply(Permission input) {
						return input.toShiroFormat();
					}

				});
		String sqlParam = _joiner.join(shiroPermissions);
		ps.setString(i, sqlParam);

	}

	@Override
	public Set<Permission> getResult(ResultSet rs, String columnName)
			throws SQLException {
		String s = rs.getString(columnName);
		Iterable<String> permissionsText = _splitter.split(s);
		Iterable<Permission> permissions = Iterables.transform(permissionsText,
				new Function<String, Permission>() {

					@Override
					public Permission apply(String input) {
						return Permission.createByShiroFormat(input);
					}

				});

		return Sets.newHashSet(permissions);
	}

	@Override
	public Set<Permission> getResult(ResultSet rs, int columnIndex)
			throws SQLException {
		String s = rs.getString(columnIndex);
		Iterable<String> permissionsText = _splitter.split(s);
		Iterable<Permission> permissions = Iterables.transform(permissionsText,
				new Function<String, Permission>() {

					@Override
					public Permission apply(String input) {
						return Permission.createByShiroFormat(input);
					}

				});

		return Sets.newHashSet(permissions);
	}

	@Override
	public Set<Permission> getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}
}
