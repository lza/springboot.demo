package org.leon.springboot.demo.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.leon.springboot.demo.db.dao.UserMapper;
import org.leon.springboot.demo.db.model.Permission;
import org.leon.springboot.demo.db.model.Role;
import org.leon.springboot.demo.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Shiro authentication & authorization realm that relies on database as
 * datastore.
 */
@Component
public class DbRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            final AuthenticationToken token)
            throws AuthenticationException {
        final UsernamePasswordToken credentials = (UsernamePasswordToken) token;
        final String email = credentials.getUsername();
        if (null == email) {
            throw new UnknownAccountException("Email not provided");
        }
        final User user = userMapper.findByEmailAndActive(email, true);
        if (null == user) {
            throw new UnknownAccountException("Account does not exist");
        }
        return new SimpleAuthenticationInfo(email, user.getPassword().toCharArray(),
                ByteSource.Util.bytes(email), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            final PrincipalCollection principals) {
        // retrieve role names and permission names
        String email = (String) principals.getPrimaryPrincipal();
        final User user = userMapper.findByEmailAndActive(email, true);
        if (null == user) {
            throw new UnknownAccountException("Account does not exist");
        }
        final int totalRoles = user.getRoles().size();
        final Set<String> roleNames = new LinkedHashSet<>(totalRoles);
        final Set<String> permissionNames = new LinkedHashSet<>();
        if (totalRoles > 0) {
            for (Role role : user.getRoles()) {
                roleNames.add(role.getName());
                for (Permission permission : role.getPermissions()) {
                    permissionNames.add(permission.getName());
                }
            }
        }
        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissionNames);
        return info;
    }

}
