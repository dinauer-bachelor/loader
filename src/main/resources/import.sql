/* Issuetype */
CREATE VERTEX TYPE issuetype_id;
CREATE PROPERTY issuetype_id.id STRING;

CREATE VERTEX TYPE issuetype;
CREATE PROPERTY issuetype.name STRING;
CREATE PROPERTY issuetype.description STRING;

/* Project */
CREATE VERTEX TYPE project_id;
CREATE PROPERTY project_id.key STRING;
CREATE PROPERTY project_id.count INTEGER;

CREATE VERTEX TYPE project;
CREATE PROPERTY project.key STRING;
CREATE PROPERTY project.name STRING;
CREATE PROPERTY project.description STRING;
CREATE PROPERTY project.inserted_at STRING;

CREATE EDGE TYPE has_project_state;
CREATE EDGE TYPE latest_project_state;

/* Issue */
CREATE VERTEX TYPE issue_id;
CREATE PROPERTY issue_id.key STRING;
CREATE PROPERTY issue_id.count INTEGER;

CREATE VERTEX TYPE issue;
CREATE PROPERTY issue.key STRING;
CREATE PROPERTY issue.project_key STRING;
CREATE PROPERTY issue.summary STRING;
CREATE PROPERTY issue.description STRING;
CREATE PROPERTY issue.inserted_at STRING;
CREATE PROPERTY issue.status STRING;
CREATE PROPERTY issue.issuetype STRING;
CREATE PROPERTY issue.assignee STRING;
CREATE PROPERTY issue.reporter STRING;

CREATE EDGE TYPE has_issue_state;
CREATE EDGE TYPE latest_issue_state;

CREATE EDGE TYPE belongs_to_project;

CREATE EDGE TYPE is_similar_to;
CREATE PROPERTY is_similar_to.similarity DOUBLE;

/* Comment */
CREATE VERTEX TYPE comment_id;
CREATE PROPERTY comment_id.id STRING;

CREATE VERTEX TYPE comment;
CREATE PROPERTY comment.id STRING;
CREATE PROPERTY comment.issue_key STRING;
CREATE PROPERTY comment.project_key STRING;
CREATE PROPERTY comment.text STRING;
CREATE PROPERTY comment.author STRING;

CREATE EDGE TYPE has_comment_state;
CREATE EDGE TYPE latest_comment_state;

CREATE EDGE TYPE belongs_to_issue;