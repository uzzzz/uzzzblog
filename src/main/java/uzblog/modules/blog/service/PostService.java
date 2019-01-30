/*
+--------------------------------------------------------------------------
|   
|   ========================================
|    
|   
|
+---------------------------------------------------------------------------
*/
package uzblog.modules.blog.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uzblog.modules.blog.data.PostVO;

/**
 * 文章管理
 * 
 * 
 *
 */
public interface PostService {
	/**
	 * 分页查询所有文章
	 * 
	 * @param pageable
	 * @param channelId
	 *            分组Id
	 * @param ord
	 *            排序
	 */
	Page<PostVO> paging(Pageable pageable, int channelId, Set<Integer> excludeChannelIds, String ord);

	Page<PostVO> paging4Admin(Pageable pageable, long id, String title, int channelId);

	/**
	 * 查询个人发布文章
	 * 
	 * @param pageable
	 * @param userId
	 */
	Page<PostVO> pagingByAuthorId(Pageable pageable, long userId);

	List<PostVO> findAllFeatured();

	/**
	 * 根据关键字搜索
	 * 
	 * @param pageable
	 * @param q
	 * @throws Exception
	 */
	Page<PostVO> search(Pageable pageable, String q) throws Exception;

	/**
	 * 搜索 Tag
	 * 
	 * @param pageable
	 * @param tag
	 */
	Page<PostVO> searchByTag(Pageable pageable, String tag);

	/**
	 * 查询最近更新 - 按发布时间排序
	 * 
	 * @param maxResults
	 * @param ignoreUserId
	 * @return
	 */
	List<PostVO> findLatests(int maxResults, long ignoreUserId);

	/**
	 * 查询热门文章 - 按浏览次数排序
	 * 
	 * @param maxResults
	 * @param ignoreUserId
	 * @return
	 */
	List<PostVO> findHots(int maxResults, long ignoreUserId);

	/**
	 * 根据Ids查询
	 * 
	 * @param ids
	 * @return <id, 文章对象>
	 */
	Map<Long, PostVO> findMapByIds(Set<Long> ids);

	/**
	 * 推荐/精华
	 * 
	 * @param id
	 * @param featured
	 *            0: 取消, 1: 加精
	 */
	void updateFeatured(long id, int featured);

	/**
	 * 置顶
	 * 
	 * @param id
	 * @param weight
	 *            0: 取消, 1: 置顶
	 */
	void updateWeight(long id, int weight);

	/**
	 * 自增浏览数
	 * 
	 * @param id
	 */
	void identityViews(long id);

	/**
	 * 自增评论数
	 * 
	 * @param id
	 */
	void identityComments(long id);

	/**
	 * 喜欢文章
	 * 
	 * @param userId
	 * @param postId
	 */
	void favor(long userId, long postId);

	/**
	 * 取消喜欢文章
	 * 
	 * @param userId
	 * @param postId
	 */
	void unfavor(long userId, long postId);

	void resetIndexs();

	List<Long> findAllIds();
}
