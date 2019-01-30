package uzblog.modules.blog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import uzblog.modules.blog.entity.Favor;

/**
 *  on 2015/8/31.
 */
public interface FavorDao extends JpaRepository<Favor, Long>, JpaSpecificationExecutor<Favor> {
    /**
     * 指定查询
     * @param ownId
     * @param postId
     * @return
     */
    Favor findByOwnIdAndPostId(long ownId, long postId);

    Page<Favor> findAllByOwnIdOrderByCreatedDesc(Pageable pageable, long ownId);
}
