package uzblog.modules.user.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uzblog.base.lang.Consts;
import uzblog.modules.blog.data.PostVO;
import uzblog.modules.blog.service.PostService;
import uzblog.modules.user.dao.NotifyDao;
import uzblog.modules.user.data.NotifyVO;
import uzblog.modules.user.data.UserVO;
import uzblog.modules.user.entity.Notify;
import uzblog.modules.user.service.NotifyService;
import uzblog.modules.user.service.UserService;
import uzblog.modules.utils.BeanMapUtils;

import java.util.*;

/**
 *  on 2015/8/31.
 */
@Service
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private NotifyDao notifyDao;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @Override
    @Transactional(readOnly = true)
    public Page<NotifyVO> findByOwnId(Pageable pageable, long ownId) {
        Page<Notify> page = notifyDao.findAllByOwnIdOrderByIdDesc(pageable, ownId);
        List<NotifyVO> rets = new ArrayList<>();

        Set<Long> postIds = new HashSet<>();
        Set<Long> fromUserIds = new HashSet<>();

        // 筛选
        page.getContent().forEach(po -> {
            NotifyVO no = BeanMapUtils.copy(po);

            rets.add(no);

            if (no.getPostId() > 0) {
                postIds.add(no.getPostId());
            }
            if (no.getFromId() > 0) {
                fromUserIds.add(no.getFromId());
            }

        });

        // 加载
        Map<Long, PostVO> posts = postService.findMapByIds(postIds);
        Map<Long, UserVO> fromUsers = userService.findMapByIds(fromUserIds);

        rets.forEach(n -> {
            if (n.getPostId() > 0) {
                n.setPost(posts.get(n.getPostId()));
            }
            if (n.getFromId() > 0) {
                n.setFrom(fromUsers.get(n.getFromId()));
            }
        });

        return new PageImpl<>(rets, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public void send(NotifyVO notify) {
        if (notify == null || notify.getOwnId() <=0 || notify.getFromId() <= 0) {
            return;
        }

        Notify po = new Notify();
        BeanUtils.copyProperties(notify, po);
        po.setCreated(new Date());

        notifyDao.save(po);
    }

    @Override
    @Transactional(readOnly = true)
    public int unread4Me(long ownId) {
        return notifyDao.countByOwnIdAndStatus(ownId, Consts.UNREAD);
    }

    @Override
    @Transactional
    public void readed4Me(long ownId) {
        notifyDao.updateReadedByOwnId(ownId);
    }
}
