package architectgroup.fact.access;

import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dao.*;
import architectgroup.fact.dto.ProjectDto;
import architectgroup.fact.dto.UserDto;
import architectgroup.fact.util.EnumUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/6/13
 * Time: 11:37 AM
 */
public class UserAccess {
    private FactAccessFactory factAccess;
    private DaoFactory _factory;
    private DatabaseDao _database;
    private UserDao _userDao;
    private ProjectDao _projectDao;

    public UserAccess(FactAccessFactory factAccess) {
        this.factAccess = factAccess;
        _factory = factAccess.getFactory();
        _database = _factory.getDatabaseDao();
        _projectDao = _factory.getProjectDao();
        _userDao = _factory.getUserDao();
    }

    /**
     * This function is used to check the username and password.
     * @param username the input username
     * @param password the input password
     * @return the User Object Dto
     */
    @Nullable
    public UserDto userAuthenticate(String username, String password) {
        UserDao userDao = _factory.getUserDao();
        UserDto user = userDao.findByUsernameAndPassword(username, password);
        if (user != null) {
            return user;
        }
        return null;
    }

    /**
     * Return the anonymous user
     * @return userDto object
     */
    public UserDto getAnonymousUser() {
        UserDao userDao = _factory.getUserDao();
        UserDto userDto = userDao.findByUsername("anonymous");
        return userDto;
    }

    /**
     * Get the list of all the user dto
     * @return list of dto
     */
    public List<UserDto> getUserList() {
        List<UserDto> userList = _userDao.findAll();
        return userList;
    }

    /**
     *
     * @param username
     * @return
     */
    public int deleteUser(int id) {
        UserDto user = _userDao.findById(id);
        List<ProjectDto> list = _projectDao.findByUser(user.getUsername());
        if (list.size() > 0) {
            return -1;
        } else {
            _userDao.delete(id);
            return 0;
        }
    }

    /**
     * Insert user to database
     * @param username
     * @param password
     * @param name
     * @param email
     * @param role
     * @return
     */
    public int addUser(String username, String password, String name, String email, int role) {
        UserDto newUser = new UserDto();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setName("No name");
        newUser.setRole(EnumUtil.roleFromInt(role));
        _userDao.insert(newUser);
        return 0;
    }

    /**
     * Insert a user to database
     * @return
     */
    public int updateUser(int id, String username, String password, String name, String email, int role) {
        UserDto userDto = _userDao.findById(id);
        userDto.setUsername(username);
        userDto.setEmail(email);
        userDto.setName(name);
        userDto.setRole(EnumUtil.roleFromInt(role));
        userDto.setPassword(password);
        _userDao.update(userDto);
        return 0;
    }

    /**
     *
     * @param username
     * @return
     */
    public UserDto getUserDetail(String username) {
        UserDto userDto = _userDao.findByUsername(username);
        return userDto;
    }

    /**
     * This function is used to check the username and password.
     * @param username the input username
     * @return the User Object Dto
     */
    public boolean checkUserExists(String username) {
        UserDao userDao = _factory.getUserDao();
        UserDto user = userDao.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }

    /**
     * This function is used to check the username and password.
     * @param email the input username
     * @return the User Object Dto
     */
    public UserDto checkEmailExists(String email) {
        UserDao userDao = _factory.getUserDao();
        UserDto user = userDao.findByEmail(email);
        return user;
    }
}
