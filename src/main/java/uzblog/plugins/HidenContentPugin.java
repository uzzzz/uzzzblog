package uzblog.plugins;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import uzblog.core.hook.interceptor.desk.ChannelControllerHook;
import uzblog.modules.blog.data.PostVO;
import uzblog.modules.blog.service.CommentService;
import uzblog.modules.user.data.AccountProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Beldon
 */
@Component
public class HidenContentPugin implements ChannelControllerHook.ChannelControllerInterceptorListener {
    @Autowired
    private CommentService commentService;

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView) throws Exception {
        PostVO ret = (PostVO) modelAndView.getModelMap().get("view");
        if (ret != null && check(ret.getId(), ret.getAuthor().getId())) {
            PostVO post = new PostVO();
            BeanUtils.copyProperties(ret, post);
            post.setContent(replace(post.getContent()));
            modelAndView.getModelMap().put("view", post);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws Exception {

    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {

    }

    private String replace(String content) {
        String c = content.replaceAll("<hide>([\\s\\S]*)</hide>", "隐藏内容，请回复后查看");
        c = c.replaceAll("&lt;hide&gt;([\\s\\S]*)&lt;/hide&gt;", "隐藏内容，请回复后查看");
        return c;
    }

    private boolean check(long id, long userId) {
        Subject subject = SecurityUtils.getSubject();
        Object o = subject.getSession().getAttribute("profile");
        if (o != null) {
            AccountProfile profile = (AccountProfile) o;
            if (profile.getId() == userId) {
                return false;
            }
            List l = commentService.findAllByAuthorIdAndToId(profile.getId(), id);
            if (l.size() > 0) {
                return false;
            }
        }
        return true;
    }
}
