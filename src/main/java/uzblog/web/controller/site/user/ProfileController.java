package uzblog.web.controller.site.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uzblog.base.data.Data;
import uzblog.modules.user.data.AccountProfile;
import uzblog.modules.user.data.UserVO;
import uzblog.modules.user.service.UserService;
import uzblog.web.controller.BaseController;
import uzblog.web.controller.site.Views;

@Controller
@RequestMapping("/user")
public class ProfileController extends BaseController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String view(ModelMap model) {
		AccountProfile profile = getSubject().getProfile();
		UserVO view = userService.get(profile.getId());
		model.put("view", view);
		return view(Views.USER_PROFILE);
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String post(String name, String signature, ModelMap model) {
		Data data;
		AccountProfile profile = getSubject().getProfile();

		try {
			UserVO user = new UserVO();
			user.setId(profile.getId());
			user.setName(name);
			user.setSignature(signature);

			putProfile(userService.update(user));

			// put 最新信息
			UserVO view = userService.get(profile.getId());
			model.put("view", view);

			data = Data.success("操作成功", Data.NOOP);
		} catch (Exception e) {
			data = Data.failure(e.getMessage());
		}
		model.put("data", data);
		return view(Views.USER_PROFILE);
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public String email() {
		return view(Views.USER_EMAIL);
	}

}
