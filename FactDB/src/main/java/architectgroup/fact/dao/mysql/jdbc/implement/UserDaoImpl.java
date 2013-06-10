package architectgroup.fact.dao.mysql.jdbc.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import architectgroup.fact.Const;
import architectgroup.fact.dao.UserDao;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dto.UserDto;
import architectgroup.fact.util.EnumUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserDaoImpl extends BaseDao implements UserDao {
    private static final Logger _logger = Logger.getLogger(UserDaoImpl.class);
    private Connection _conn = null;

    /**
     * {@inheritDoc}
     */
    public int insert(UserDto user) {
        PreparedStatement userInsertStatement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES (NULL, ?, ?, ?, ?, ?)", Const.METADATA_DATABASE_NAME, Const.TABLE_USER_NAME);
                userInsertStatement = _conn.prepareStatement(sql);
                userInsertStatement.setString(1, user.getUsername());
                userInsertStatement.setString(2, user.getPassword());
                userInsertStatement.setString(3, user.getName());
                userInsertStatement.setString(4, user.getEmail());
                userInsertStatement.setInt(5, user.getRoleInCode());
                userInsertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(userInsertStatement);
            DbUtils.closeQuietly(_conn);
        }

        return user.getId();
    }


    /**
     * {@inheritDoc}
     */
    public void update(UserDto user) {
        PreparedStatement userUpdateStatement = null;
        try {
            _conn = this.getConnection();
            userUpdateStatement = _conn.prepareStatement("UPDATE `" + Const.METADATA_DATABASE_NAME + "`.`" + Const.TABLE_USER_NAME + "` SET username = ?, password = ?, name = ?, email = ?, role = ? WHERE id = ?");
            userUpdateStatement.setString(1, user.getUsername());
            userUpdateStatement.setString(2, user.getPassword());
            userUpdateStatement.setString(3, user.getName());
            userUpdateStatement.setString(4, user.getEmail());
            userUpdateStatement.setInt(5, user.getRoleInCode());
            userUpdateStatement.setInt(6, user.getId());
            userUpdateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(userUpdateStatement);
            DbUtils.closeQuietly(_conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete(int userId)
    {
        PreparedStatement userDeleteStatement = null;
        try {
            _conn = this.getConnection();
            userDeleteStatement = _conn.prepareStatement("DELETE FROM  `" + Const.METADATA_DATABASE_NAME + "`.`" + Const.TABLE_USER_NAME+"` WHERE (id = ?)");
            userDeleteStatement.setInt(1, userId);
            userDeleteStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(userDeleteStatement);
            DbUtils.closeQuietly(_conn);
        }
    }

    /**
     * Convert the result set to the DTO
     * @param rs Result set
     * @return DTO mapped from the result set
     * @throws SQLException
     */
    public UserDto mapRow(@NotNull ResultSet rs) throws SQLException
    {
        UserDto user = new UserDto();
        user.setId(rs.getInt(1));
        user.setUsername(rs.getString(2));
        user.setPassword(rs.getString(3));
        user.setName(rs.getString(4));
        user.setEmail(rs.getString(5));
        user.setRole(EnumUtil.roleFromInt(rs.getInt(6)));
        if (rs.wasNull()) {
            user = null;
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public UserDto findByUsername(String username) {
        UserDto userDto = null;
        PreparedStatement userSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE username = ?", Const.METADATA_DATABASE_NAME, Const.TABLE_USER_NAME);
                userSelectStatement = _conn.prepareStatement(sql);
                userSelectStatement.setString(1, username);
                rs = userSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        userDto = mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, userSelectStatement, rs);
        }

        return userDto;
    }

    /**
     * {@inheritDoc}
     */
    public UserDto findById(int id) {
        UserDto userDto = null;
        PreparedStatement userSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE id = ?", Const.METADATA_DATABASE_NAME, Const.TABLE_USER_NAME);
                userSelectStatement = _conn.prepareStatement(sql);
                userSelectStatement.setInt(1, id);
                rs = userSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        userDto = mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, userSelectStatement, rs);
        }

        return userDto;
    }

    /**
     * {@inheritDoc}
     */
    public UserDto findByEmail(String email) {
        UserDto userDto = null;
        PreparedStatement userSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE email = ?", Const.METADATA_DATABASE_NAME, Const.TABLE_USER_NAME);
                userSelectStatement = _conn.prepareStatement(sql);
                userSelectStatement.setString(1, email);
                rs = userSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        userDto = mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, userSelectStatement, rs);
        }

        return userDto;
    }

    /**
     * {@inheritDoc}
     */
    public List<UserDto> findAll() {
        List<UserDto> userList = new ArrayList<UserDto>();
        PreparedStatement userSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s`", Const.METADATA_DATABASE_NAME, Const.TABLE_USER_NAME);
                userSelectStatement = _conn.prepareStatement(sql);
                rs = userSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        UserDto userDto = mapRow(rs);
                        userList.add(userDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, userSelectStatement, rs);
        }

        return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    public UserDto findByUsernameAndPassword(String username, String password) {
        UserDto userDto = null;
        PreparedStatement userSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE username = ? AND password = ?", Const.METADATA_DATABASE_NAME, Const.TABLE_USER_NAME);
                userSelectStatement = _conn.prepareStatement(sql);
                userSelectStatement.setString(1, username);
                userSelectStatement.setString(2, password);
                rs = userSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        userDto = mapRow(rs);
                        userDto.setPassword("");    // Security issue, we removed the password that sent to presentation layer
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, userSelectStatement, rs);
        }

        return userDto;
    }
}
