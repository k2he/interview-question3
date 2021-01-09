package com.example.demo.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class LocaleResolver extends AcceptHeaderLocaleResolver {

	private static final List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("es"));

	/*
	 * Pass Accept-Language: fr from French 
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String language = request.getHeader("Accept-Language");
		if (language == null || language.isEmpty()) {
			return Locale.getDefault();
		}
		List<Locale.LanguageRange> list = Locale.LanguageRange.parse(language);
		Locale locale = Locale.lookup(list, LOCALES);
		return locale;
	}

}
