/*
+--------------------------------------------------------------------------
|   
|   ========================================
|    
|   
|
+---------------------------------------------------------------------------
*/
package uzblog.modules.user.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uzblog.modules.user.data.AccountProfile;
import uzblog.modules.user.data.UserVO;

/**
 * 
 *
 */
public interface UserService {
	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	AccountProfile login(String username, String password);

	/**
	 * 登录,用于记住登录时获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	AccountProfile getProfileByName(String username);

	/**
	 * 注册
	 * 
	 * @param user
	 */
	UserVO register(UserVO user);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	AccountProfile update(UserVO user);

	/**
	 * 查询单个用户
	 * 
	 * @param id
	 * @return
	 */
	UserVO get(long id);

	UserVO getByUsername(String username);

	/**
	 * 修改头像
	 * 
	 * @param id
	 * @param path
	 * @return
	 */
	AccountProfile updateAvatar(long id, String path);

	/**
	 * 修改密码
	 * 
	 * @param id
	 * @param newPassword
	 */
	void updatePassword(long id, String newPassword);

	/**
	 * 修改密码
	 * 
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 */
	void updatePassword(long id, String oldPassword, String newPassword);

	/**
	 * 修改用户状态
	 * 
	 * @param id
	 * @param status
	 */
	void updateStatus(long id, int status);

	/**
	 * 分页查询
	 * 
	 * @param pageable
	 */
	Page<UserVO> paging(Pageable pageable);

	Page<UserVO> paging(String key, Pageable pageable);

	Map<Long, UserVO> findMapByIds(Set<Long> ids);

	List<UserVO> findHotUserByfans();
}
