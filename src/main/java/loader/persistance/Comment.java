package loader.persistance;

public class Comment
{
    private String id;
    private String issueKey;
    private String projectKey;
    private String text;

    public String getId()
    {
        return id;
    }

    public Comment setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getIssueKey()
    {
        return issueKey;
    }

    public Comment setIssueKey(String issueKey)
    {
        this.issueKey = issueKey;
        return this;
    }

    public String getProjectKey()
    {
        return projectKey;
    }

    public Comment setProjectKey(String projectKey)
    {
        this.projectKey = projectKey;
        return this;
    }

    public String getText()
    {
        return text;
    }

    public Comment setText(String text)
    {
        this.text = text;
        return this;
    }
}
