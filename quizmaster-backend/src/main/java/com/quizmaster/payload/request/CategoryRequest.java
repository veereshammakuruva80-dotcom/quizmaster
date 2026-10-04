package com.quizmaster.payload.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank
    private String name;
    private String slug;
    private String description;
    private String icon = "bi-bookmark";
    private String colorCode = "#4f46e5";
    private Boolean active = true;

    public CategoryRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
