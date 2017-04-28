package org.leon.springboot.demo.controller.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.leon.springboot.demo.Utils;
import org.leon.springboot.demo.controller.rest.params.BaseResult;
import org.leon.springboot.demo.controller.rest.params.LoginParams;
import org.leon.springboot.demo.db.dao.PermissionMapper;
import org.leon.springboot.demo.db.model.Permission;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.User;
import org.leon.springboot.demo.db.service.RoleService;
import org.leon.springboot.demo.db.service.UserService;
import org.leon.springboot.demo.shiro.ShiroPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO add description
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DefaultPasswordService passwordService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private TwoFactorAuthController twoFactorAuthController;

    @PostMapping("/login")
    public BaseResult<String> login(@RequestBody LoginParams loginParams) {
        BaseResult<String> result = new BaseResult<String>();

        try {
            UsernamePasswordToken credentials = new UsernamePasswordToken();
            String email = loginParams.getEmail();
            credentials.setUsername(email);
            credentials.setPassword(loginParams.getPassword().toCharArray());
            credentials.setRememberMe(loginParams.getRememberMe());

            logger.info("Authenticating {}", email);
            final Subject subject = SecurityUtils.getSubject();
            subject.login(credentials);

            // set attribute that will allow session querying
            subject.getSession().setAttribute("isOTPverified", false);

            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e);
        }

        return result;
    }

    @GetMapping("/logout")
    public BaseResult<String> logout(){
        BaseResult<String> result = new BaseResult<String>();

        try {
            final Subject subject = SecurityUtils.getSubject();
            subject.logout();

            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e);
        }

        return result;
    }

    @GetMapping("/getAllPermissions")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetAllPermissions)
    public BaseResult<List<Permission>> getAllPermissions(){
        BaseResult<List<Permission>> result = new BaseResult<List<Permission>>();
        try {
            List<Permission> permissions = permissionMapper.getAll();
            result.setData(permissions);
            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:{}", e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getPermission/{permissionId}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetPermission)
    public BaseResult<Permission> getPermission(@PathVariable("permissionId")Permission permission){
        BaseResult<Permission> result = new BaseResult<Permission>();

        try {
            if(null == permission){
                result.setStatus("400");
                result.setMsg("permission not exist");
                return result;
            }
            result.setStatus("200");
            result.setData(permission);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getAllRoles")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetAllRoles)
    public BaseResult<List<Role>> getAllRoles(){
        BaseResult<List<Role>> result = new BaseResult<List<Role>>();
        try {
            List<Role> roles = roleService.getRoles();
            result.setData(roles);
            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:{}", e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getRole/{roleId}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetRole)
    public BaseResult<Role> getRole(@PathVariable("roleId")Role role){
        BaseResult<Role> result = new BaseResult<Role>();

        try {
            if(null == role){
                result.setStatus("400");
                result.setMsg("role not exist");
                return result;
            }
            result.setStatus("200");
            result.setData(role);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getMyPermissions")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetMyPermissions)
    public BaseResult<List<Role>> getMyPermissions(){
        BaseResult<List<Role>> result = new BaseResult<List<Role>>();
        try {
            final Subject subject = SecurityUtils.getSubject();
            String email = (String) subject.getPrincipal();
            User user = userService.findByEmailAndActive(email, true);
            List<Role> roles = user.getRoles();
            result.setData(roles);
            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:{}", e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @PostMapping("/addRole")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserAddRole)
    public BaseResult<Role> addRole(@RequestBody Role role){
        BaseResult<Role> result = new BaseResult<Role>();
        try {
            String name = role.getName();
            String description = role.getDescription();
            if(null == name){
                result.setStatus("400");
                result.setMsg("role name could not be null");
                return result;
            }
            Role tempRole = roleService.findByName(name);
            if (tempRole != null){
                result.setStatus("400");
                result.setMsg("role with the same name already exist:" + name);
                return result;
            }

            tempRole = new Role();
            tempRole.setName(name);
            tempRole.setDescription(description);
            roleService.insert(tempRole);
            tempRole = roleService.findById(tempRole.getId());

            result.setStatus("200");
            result.setData(tempRole);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @PostMapping("/updateRole")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserUpdateRole)
    public BaseResult<Role> updateRole(@RequestBody Role role){
        BaseResult<Role> result = new BaseResult<Role>();

        try {
            if(null == role){
                result.setStatus("400");
                result.setMsg("role not exist");
                return result;
            }
            String name = role.getName();
            String description = role.getDescription();

            Role tempRole = new Role();
            tempRole.setId(role.getId());
            tempRole.setName(name);
            tempRole.setDescription(description);
            roleService.update(tempRole);
        } catch (Exception e){
            logger.debug("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/deleteRole/{roleId}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserDeleteRole)
    public BaseResult<String> deleteRole(@PathVariable("roleId")Role role){
        BaseResult<String> result = new BaseResult<String>();

        try {
            if(null == role){
                result.setStatus("400");
                result.setMsg("role not exist");
                return result;
            }
            roleService.delete(role);
            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/grantPermission/{roleId}/{permissionId}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGrantPermission)
    public BaseResult<Role> grantPermission(@PathVariable("roleId")Role role,@PathVariable("permissionId")Permission permission){
        BaseResult<Role> result = new BaseResult<Role>();
        try {
            if(null == permission){
                result.setStatus("400");
                result.setMsg("permission not exist");
                return result;
            }
            if(null == role){
                result.setStatus("400");
                result.setMsg("role not exist");
                return result;
            }

            List<Permission> permissions = role.getPermissions();
            permissions.add(permission);

            Role tempRole = new Role();
            tempRole.setId(role.getId());
            tempRole.setPermissions(permissions);
            roleService.update(tempRole);
            tempRole = roleService.findById(role.getId());

            result.setStatus("200");
            result.setData(tempRole);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/revokePermission/{roleId}/{permissionId}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserRevokePermission)
    public BaseResult<Role> revokePermission(@PathVariable("roleId")Role role,@PathVariable("permissionId")Permission permission){
        BaseResult<Role> result = new BaseResult<Role>();
        try {
            if(null == permission){
                result.setStatus("400");
                result.setMsg("permission not exist");
                return result;
            }
            if(null == role){
                result.setStatus("400");
                result.setMsg("role not exist");
                return result;
            }
            boolean found = false;
            List<Permission> permissions = role.getPermissions();
            for (Permission p:permissions){
                if(p.getId().longValue() == permission.getId().longValue()){
                    permissions.remove(p);
                    found = true;
                    break;
                }
            }
            if(!found){
                result.setStatus("400");
                result.setMsg("permission not exist for role:" + permission.getId() + "->" + role.getId());
                return result;
            }

            Role tempRole = new Role();
            tempRole.setId(role.getId());
            tempRole.setPermissions(permissions);
            roleService.update(tempRole);
            tempRole = roleService.findById(role.getId());

            result.setStatus("200");
            result.setData(tempRole);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @PostMapping("/addUser")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserAddUser)
    public BaseResult<User> addUser(@RequestBody User user){
        BaseResult<User> result = new BaseResult<User>();

        try {
            String email = user.getEmail();
            String name = user.getName();
            String password = user.getPassword();
            if(null == email || null == password){
                result.setStatus("400");
                result.setMsg("email & password could not be null:" + email + "," + password);
                return result;
            }
            User tempUser = userService.findByEmailAndActive(email, true);
            if(tempUser != null){
                result.setStatus("400");
                result.setMsg("user with the same email already exist:" + email);
                return result;
            }

            tempUser = new User();
            tempUser.setActive(true);
            tempUser.setEmail(email);
            tempUser.setName(name);
            tempUser.setPassword(passwordService.encryptPassword(password));
            userService.insert(tempUser);
            tempUser = userService.findByEmailAndActive(email, true);
            if(tempUser.getTwoFactorAuthEnabled()){
                twoFactorAuthController.init(tempUser,false);
            }
            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @PostMapping("/updateUser")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserUpdateUser)
    public BaseResult<User> updateUser(@RequestBody User user){
        BaseResult<User> result = new BaseResult<User>();

        try {
            if(null == user){
                result.setStatus("400");
                result.setMsg("user not exist");
                return result;
            }
            String name = user.getName();
            String password = user.getPassword();
            if(password != null && password.length() > 0){
                password = passwordService.encryptPassword(password);
            }
            User tempUser = new User();
            tempUser.setId(user.getId());
            tempUser.setName(name);
            tempUser.setPassword(password);
            userService.update(tempUser);
            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/deleteUser/{email}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserDeleteUser)
    public BaseResult<String> deleteUser(@PathVariable("email")User user){
        BaseResult<String> result = new BaseResult<String>();

        try {
            if(null == user){
                result.setStatus("400");
                result.setMsg("user not exist");
                return result;
            }
            User tempUser = new User();
            tempUser.setId(user.getId());
            tempUser.setActive(false);
            userService.update(tempUser);
            result.setStatus("200");
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/grantRole/{roleId}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGrantRole)
    public BaseResult<User> grantRole(@PathVariable("roleId") Role role){
        BaseResult<User> result = new BaseResult<User>();
        try {
            if(null == role){
                result.setStatus("400");
                result.setMsg("role not exist");
                return result;
            }
            final Subject subject = SecurityUtils.getSubject();
            String email = (String) subject.getPrincipal();
            User user = userService.findByEmailAndActive(email, true);
            List<Role> roles = user.getRoles();
            roles.add(role);

            User tempUser = new User();
            tempUser.setId(user.getId());
            tempUser.setRoles(roles);
            userService.update(tempUser);
            tempUser = userService.findByEmailAndActive(email, true);

            result.setStatus("200");
            result.setData(tempUser);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/revokeRole/{roleId}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserRevokeRole)
    public BaseResult<String> revokeRole(@PathVariable("roleId") Role role){
        BaseResult<String> result = new BaseResult<String>();

        try {
            if(null == role){
                result.setStatus("400");
                result.setMsg("role not exist");
                return result;
            }
            final Subject subject = SecurityUtils.getSubject();
            String email = (String) subject.getPrincipal();
            User user = userService.findByEmailAndActive(email, true);
            boolean found = false;
            List<Role> roles = user.getRoles();
            for (Role r:roles){
                if(r.getId() == role.getId()){
                    roles.remove(r);
                    found = true;
                    break;
                }
            }
            if(!found){
                result.setStatus("400");
                result.setMsg("role not exist for current user:" + role.getId());
            }else {
                User tempUser = new User();
                tempUser.setId(user.getId());
                tempUser.setRoles(roles);

                result.setStatus("200");
            }
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getAllUsers")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetAllUsers)
    public BaseResult<List<User>> getAllUsers(){
        BaseResult<List<User>> result = new BaseResult<List<User>>();

        try {
            List<User> users = userService.getAll();
            result.setStatus("200");
            result.setData(users);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getUser/{email}")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetUser)
    public BaseResult<User> getUser(@PathVariable("email")User user){
        BaseResult<User> result = new BaseResult<User>();

        try {
            if(null == user){
                result.setStatus("400");
                result.setMsg("user not exist");
                return result;
            }
            result.setStatus("200");
            result.setData(user);
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e.getMessage());
        }

        return result;
    }

    @GetMapping("/getMyInfo")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.UserGetMyInfo)
    public BaseResult<User> getMyInfo(){
        BaseResult<User> result = new BaseResult<User>();

        try {
            final Subject subject = SecurityUtils.getSubject();
            String email = (String) subject.getPrincipal();
            User user = userService.findByEmailAndActive(email, true);

            result.setStatus("200");
            result.setData(user);
        } catch (Exception e){
            result.setStatus("500");
            result.setMsg("exception:" + e);
        }

        return result;
    }

    @GetMapping("/init")
    public void initScenario() {
        logger.info("Initializing scenario..");
        // clean-up users, roles and permissions
        userService.deleteAll();
        roleService.deleteAll();
        permissionMapper.deleteAll();
        // define permissions
        List<Permission> permissions = new ArrayList<Permission>();
        ShiroPermissions shiroPermissions = new ShiroPermissions();
        try {
            Map<String, Object> fieldNamesAndValues = Utils.getFieldNamesAndValues(shiroPermissions, false);
            for (String key:fieldNamesAndValues.keySet()){
                String value = (String) fieldNamesAndValues.get(key);
                logger.debug("key:{}->value:{}", key, value);
                Permission permission = new Permission();
                permission.setName(value);
                permission.setDescription(value);
                permissions.add(permission);
            }
        } catch (Exception e) {
            logger.error("exception:{}", e);
        }
        for (Permission permission:permissions){
            permissionMapper.insert(permission);
        }
        // define roles
        Role roleSuperAdmin = new Role();
        roleSuperAdmin.setName("superAdmin");
        roleSuperAdmin.setDescription("superAdmin");
        roleSuperAdmin.setPermissions(permissions);
        roleService.insert(roleSuperAdmin);

        // define user
        String password = passwordService.encryptPassword("admin");
        User userSuperAdmin = new User();
        userSuperAdmin.setActive(true);
        userSuperAdmin.setEmail("admin@cgtn.com");
        userSuperAdmin.setName("admin");
        userSuperAdmin.setPassword(password);
        List<Role> roles = new ArrayList<Role>();
        roles.add(roleSuperAdmin);
        userSuperAdmin.setRoles(roles);
        userService.insert(userSuperAdmin);
        twoFactorAuthController.init(userSuperAdmin, true);

        logger.info("Scenario initiated.");
    }

}
