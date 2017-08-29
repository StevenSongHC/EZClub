package me.steven.ezclub.interceptor;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.steven.ezclub.entity.User;
import me.steven.ezclub.service.ActivityService;
import me.steven.ezclub.service.ClubService;
import me.steven.ezclub.service.CollegeService;
import me.steven.ezclub.service.MessageService;
import me.steven.ezclub.service.UserService;
import me.steven.ezclub.util.CookieUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public class BaseInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService uService;
	@Autowired
	private ClubService cService;
	@Autowired
	private MessageService msgService;
	@Autowired
	private ActivityService actService;
	@Autowired
	private CollegeService clgService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof ResourceHttpRequestHandler) {
			return true;
		}
		/////////////////////////////
		//////   AUTO LOGIN   ///////
		/////////////////////////////
		// if didn't detective a login user, then check the user cookie to auto login for user
		if ((User) request.getSession().getAttribute("USER_SESSION") == null) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(CookieUtil.USER_COOKIE)) {
						String cookieValue = URLDecoder.decode(cookie.getValue(), "utf-8");			// decode cookie value
						
						User user = uService.getUserByEmail(cookieValue.split(",")[1]);				// get user by email
						if (user != null) {
							String password = cookieValue.split(",")[2];
							
							cookie.setValue("");													// remove the old cookie first
							cookie.setMaxAge(0);
							cookie.setPath("/");
							response.addCookie(cookie);
							
							if (password.equals(user.getPassword())) {
								request.getSession().setAttribute("USER_SESSION", user);			// add user session
								response.addCookie(CookieUtil.generateUserCookie(user));			// add new user cookie
							}
							if (request.getSession().getAttribute("USER_SESSION") != null)  		// don't know why the heck does it run 4 times
								break;
						}
						else {
							// clear the wrong cookie
							System.out.println("clear cookie");
							cookie.setValue("");
							cookie.setMaxAge(0);
							cookie.setPath("/");
							response.addCookie(cookie);
						}
					}	
				}
			}
		}

		User currentUser = (User) request.getSession().getAttribute("USER_SESSION");
		if (currentUser != null) {
			request.setAttribute("unreadMessageCount", msgService.getUnreadMessageListByAddresseeUserId(currentUser.getId()).size());
			request.setAttribute("unreadActivityCount", actService.getUnreadActivityListByUserId(currentUser.getId()).size());
			if (currentUser.getIsAdmin() == 2779) {
				request.setAttribute("newCollegeCount", clgService.getUncheckedCollegeList().size());
				request.setAttribute("newClubCount", cService.getUncheckedClubList().size());
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
