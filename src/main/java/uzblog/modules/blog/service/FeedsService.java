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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uzblog.modules.blog.data.FeedsVO;

/**
 * 
 *
 */
public interface FeedsService {
	/**
	 * 添加动态, 同时会分发给粉丝
	 *
	 * @param feeds
	 * @return
	 */
	int add(FeedsVO feeds);

	/**
	 * 删除动态，取消关注时，删除之前此人的动态
	 *
	 * @param ownId
	 * @param authorId
	 * @return
	 */
	int deleteByAuthorId(long ownId, long authorId);

	Page<FeedsVO> findUserFeeds(Pageable pageable, long ownId);

	/**
	 * 删除文章时触发动态删除
	 *
	 * @param postId
	 */
	void deleteByTarget(long postId);
}
