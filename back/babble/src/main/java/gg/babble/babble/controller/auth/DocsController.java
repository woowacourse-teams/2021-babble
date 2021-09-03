package gg.babble.babble.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    private static final String API_DOCS_PATH = "docs/api-docs.html";

    @GetMapping("/")
    public String getApiDocs() {
        return API_DOCS_PATH;
    }
}
