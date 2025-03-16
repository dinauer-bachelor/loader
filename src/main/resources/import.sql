/* Issuetype */
CREATE VERTEX TYPE issuetype_id;
CREATE PROPERTY issuetype_id.id STRING;

CREATE VERTEX TYPE issuetype;
CREATE PROPERTY issuetype.name STRING;
CREATE PROPERTY issuetype.description STRING;

/* Project */
CREATE VERTEX TYPE project_id;
CREATE PROPERTY project_id.id STRING;
CREATE PROPERTY project_id.count INTEGER;

CREATE VERTEX TYPE project;
CREATE PROPERTY project.id STRING;
CREATE PROPERTY project.key STRING;
CREATE PROPERTY project.name STRING;
CREATE PROPERTY project.description STRING;
CREATE PROPERTY project.inserted_at STRING;

CREATE EDGE TYPE has_project_state;
CREATE EDGE TYPE latest_project_state;

/* Issue */
CREATE VERTEX TYPE issue_id;
CREATE PROPERTY issue_id.id STRING;

CREATE VERTEX TYPE issue;
CREATE PROPERTY issue.summary STRING;
CREATE PROPERTY issue.description STRING;

CREATE EDGE TYPE has_issue_state;
CREATE EDGE TYPE latest_issue_state;

/* Comment */
CREATE VERTEX TYPE comment_id;
CREATE PROPERTY comment_id.id STRING;

CREATE VERTEX TYPE comment;
CREATE PROPERTY comment.text STRING;