package pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class RedirectUtil implements Serializable {

    @Inject
    private CacheFormData cacheFormData;

    public String redirectError(String path, Object object, List<String> errors) {
        Long id = System.currentTimeMillis();
        FormData formData = FormData.builder().data(object).errors(errors).build();
        cacheFormData.save(id, formData);
        return String.format("redirect:%s?idCache=%d", path, id);
    }

    public String redirect(String path, FormData formData) {
        Long id = System.currentTimeMillis();
        cacheFormData.save(id, formData);
        return String.format("redirect:%s?idCache=%d", path, id);
    }

    public void injectFormDataToModels(Long id, Models models) {
        if (id != null) {
            Optional<FormData> formData = cacheFormData.get(id);
            if (formData.isPresent()) {
                FormData retrieved = formData.get();
                models.put("data", retrieved.getData());
                models.put("errors", retrieved.getErrors());
                models.put("infos", retrieved.getInfos());
            }
        }
    }
}
