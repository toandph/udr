package architectgroup.fact.dao;

import architectgroup.fact.dto.UserDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserDao
{
    public int insert(UserDto dto);
    public void update(UserDto dto);
    public void delete(int id);

    public UserDto findByUsernameAndPassword(String username, String password);
    public List<UserDto> findAll();
    public UserDto findByUsername(String username);
    public UserDto findById(int id);
    public UserDto findByEmail(String email);
}
