package loader.persistance;

import java.util.Objects;

public class Project
{
    private String key;
    private String name;
    private String description;

    public String getKey() {
        return key;
    }

    public Project setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        if(description == null) {
            return "";
        }
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Project that = (Project) object;
        return Objects.equals(key, that.key) && Objects.equals(name, that.name) && Objects.equals(getDescription(), that.getDescription());
    }

    public static Project fromProject(com.atlassian.jira.rest.client.api.domain.Project project) {
        return new Project().setKey(project.getKey()).setName(project.getName()).setDescription(project.getDescription());
    }
}
