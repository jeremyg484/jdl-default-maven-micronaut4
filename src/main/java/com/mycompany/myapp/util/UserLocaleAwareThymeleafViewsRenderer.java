package com.mycompany.myapp.util;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.core.util.ArgumentUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.locale.HttpLocaleResolver;
import io.micronaut.views.ViewUtils;
import io.micronaut.views.thymeleaf.ThymeleafViewsRenderer;
import io.micronaut.views.thymeleaf.WebContext;
import jakarta.inject.Singleton;
import java.util.Locale;
import java.util.Map;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;

@Singleton
@Replaces(ThymeleafViewsRenderer.class)
public class UserLocaleAwareThymeleafViewsRenderer<T> extends ThymeleafViewsRenderer<T> {

    public static String LOCALE_KEY = "user_locale";

    public UserLocaleAwareThymeleafViewsRenderer(
        AbstractConfigurableTemplateResolver templateResolver,
        TemplateEngine templateEngine,
        ClassPathResourceLoader resourceLoader,
        HttpLocaleResolver httpLocaleResolver
    ) {
        super(templateResolver, templateEngine, resourceLoader, httpLocaleResolver);
    }

    @Override
    @NonNull
    public Writable render(@NonNull String viewName, @Nullable T data, @Nullable HttpRequest<?> request) {
        ArgumentUtils.requireNonNull("viewName", viewName);
        if (request != null) {
            return super.render(viewName, data, request);
        }
        return writer -> {
            Map<String, Object> model = ViewUtils.modelOf(data);
            IContext context = new WebContext(
                null,
                model.containsKey(LOCALE_KEY) ? Locale.forLanguageTag(model.get(LOCALE_KEY).toString()) : Locale.getDefault(),
                model
            );
            render(viewName, context, writer);
        };
    }
}
