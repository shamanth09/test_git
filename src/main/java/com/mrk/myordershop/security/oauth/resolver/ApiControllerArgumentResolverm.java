package com.mrk.myordershop.security.oauth.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.security.oauth.service.OauthUserDetailsService;

public class ApiControllerArgumentResolverm implements HandlerMethodArgumentResolver {

	@Override
	@ReadTransactional
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest request, WebDataBinderFactory webDataBinderFactory) throws Exception {
		if (User.class.isAssignableFrom(parameter.getParameterType())) {
			return OauthUserDetailsService.getCurrentDomainUser();
		} else {
			throw new ClassCastException("Argument type not supported");
		}
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Owner.class);
	}
}
