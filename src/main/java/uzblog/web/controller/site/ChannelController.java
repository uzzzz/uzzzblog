/*
+--------------------------------------------------------------------------
|   
|   ========================================
|    
|   
|
+---------------------------------------------------------------------------
*/
package uzblog.web.controller.site;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import uzblog.base.lang.Consts;
import uzblog.modules.blog.data.PostVO;
import uzblog.modules.blog.entity.Channel;
import uzblog.modules.blog.service.ChannelService;
import uzblog.modules.blog.service.PostCacheableService;
import uzblog.web.controller.BaseController;

/**
 * Channel 主页
 * 
 *
 */
@Controller
public class ChannelController extends BaseController {
	@Autowired
	private ChannelService channelService;
	@Autowired
	private PostCacheableService postService;
	
	@RequestMapping("/channel/{id}")
	public String channel(@PathVariable Integer id, ModelMap model,
			HttpServletRequest request) {
		// init params
		String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
		int pn = ServletRequestUtils.getIntParameter(request, "pn", 1);

		Channel channel = channelService.getById(id);
		// callback params
		model.put("channel", channel);
		model.put("order", order);
		model.put("pn", pn);
		return view(Views.ROUTE_POST_INDEX);
	}

	@RequestMapping("/view/{id}")
	public String view(@PathVariable Long id, ModelMap model) {
		PostVO view = postService.get(id);

		Assert.notNull(view, "该文章已被删除 id : " + id);

		postService.identityViews(id);
		model.put("view", view);
		return view(Views.ROUTE_POST_VIEW);
	}
}
