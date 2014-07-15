package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Permission;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * User: james
 * Date: 12-10-9
 * Time: 上午1:34
 */
@Component
public class PermissionRepository {

    private Set<String> _shiroPermissions = Sets.newHashSet();

    private Set<Permission> _permissions = Sets.newHashSet();

    /**
     * 添加许可
     * @param permission
     */
    public void addPermission(Permission permission) {
        Assert.notNull(permission);
        _permissions.add(permission);
        _shiroPermissions.add(permission.toShiroFormat());
    }

    public void addPermission(String shiroPermission) {
        Assert.hasText(shiroPermission);
        Permission permission=Permission.createByShiroFormat(shiroPermission);
        if(permission!=null){
            addPermission(permission);
        }

    }




    /**
     * 验证许可
     * @return
     */
    public Set<String> getShiroFormatPermissions(){
        return Sets.newHashSet(_shiroPermissions);
    }

    /**
     * 获取许可
     * @return
     */
    public Set<Permission> getPermissions(){
        return Sets.newHashSet(_permissions);
    }
    
    
    /**
     * 获取许可菜单
     * @return
     */
    public Map<String,Collection<String>> getPermissionTree(){
    	Multimap<String, String> permissionMap = ArrayListMultimap.create();  
    	for(Permission _p:_permissions){
    		permissionMap.put(_p.getResource(), _p.getOperation());
    	}
    	return permissionMap.asMap();
    }

}
