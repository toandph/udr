package architectgroup.fact.dto;

import architectgroup.fact.util.Role;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class UserDto implements Serializable
{
    protected int id;
    protected String username;
    protected String password;
    protected String email;
    protected Role role;
    protected String name;

    public UserDto()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public String getRoleInString() {
        return role.getDescription();
    }

    public int getRoleInCode() {
        return role.getCode();
    }

    /**
     * Method 'toString'
     *
     * @return String
     */
    @NotNull
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "architectgroup.fact.dto.Users: " );
        ret.append( "userid=" + id );
        ret.append( ", name=" + name);
        ret.append( ", username=" + username );
        ret.append( ", password=" + password );
        ret.append( ", email=" + email );
        ret.append( ", role=" + role );
        return ret.toString();
    }

}
