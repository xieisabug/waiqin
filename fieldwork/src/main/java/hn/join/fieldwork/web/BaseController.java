package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Permission;
import hn.join.fieldwork.service.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * 控制器基类
 * @author chenjinlong
 *
 */
public class BaseController {

    @Autowired
    protected PermissionRepository permissionRepository;

    protected void addPermission(Permission permission){
        permissionRepository.addPermission(permission);
    }
    /**
     * 添加权限
     * @param shiroFormat
     */
    public void addPermission(String shiroFormat){
        permissionRepository.addPermission(shiroFormat);
    }

}
