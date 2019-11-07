#GitHub Issue Collator

A coding challenge that generates a report of issues for a set of given GitHub owner/repo



##Description

Create a program and tests that generates a report about the the Issues belonging to a list of GitHub repositories ordered by creation time, and information about the day when most Issues were created.   

Input File:

Just a command line app that takes a list of repo names and builds a summary using the github api.

  
Input:  

A List of 1 to n Strings with Github repositories references with the format "owner/repository"
Output:

The String representation of a Json dictionary with the following content:

* "issues": List containing all the Issues related to all the repositories provided. The list should be ordered by the Issue "created_at" field (From oldest to newest) Each entry of the list will be a dictionary with basic Issue information: "id", "state", "title", "repository" and "created_at" fields. Issue entry example: { "id": 1, "state": "open", "title": "Found a bug", "repository": "owner1/repository1", "created_at": "2011-04-22T13:33:48Z" }

* "top_day": Dictionary with the information of the day when most Issues were created. It will contain the day and the number of Issues that were created on each repository this day If there are more than one "top_day", the latest one should be used. example: { "day": "2011-04-22", "occurrences": { "owner1/repository1": 8, "owner2/repository2": 0, "owner3/repository3": 2 } }

 

Output example:
```
     {
      "issues": [
        {
          "id": 38,
          "state": "open",
          "title": "Found a bug",
          "repository": "owner1/repository1",
          "created_at": "2011-04-22T13:33:48Z"
        },
        {
          "id": 23,
          "state": "open",
          "title": "Found a bug 2",
          "repository": "owner1/repository1",
          "created_at": "2011-04-22T18:24:32Z"
        },
        {
          "id": 24,
          "state": "closed",
          "title": "Feature request",
          "repository": "owner2/repository2",
          "created_at": "2011-05-08T09:15:20Z"
        }
      ],
      "top_day": {
        "day": "2011-04-22",
        "occurrences": {
          "owner1/repository1": 2,
          "owner2/repository2": 0
        }
      }
    }
```
 

You can create the classes and methods you consider necessary. You can use any library you need. Good modularization, error control and code style will be taken into account. Memory usage and execution time will be taken into account. 

##Prerequisite
* Java - version 8 or higher
* Maven build tool

To install gradle: https://maven.apache.org/install.html


##Building
From inside the root directory, build using gradle:

`mvn clean install`


##Running

Note: The app must be built before it can be run.

To run the application, navigate inside the root directory where the compressed file was expanded. 

`java -jar target\github-issue-collator-1.0-SNAPSHOT-spring-boot.jar {followed by a comma seperated line of repos}`

Example

`java -jar target\github-issue-collator-1.0-SNAPSHOT-spring-boot.jar shadowsocks/shadowsocks-android,octocat/Hello-World,vmg/redcarpet,iptv-org/iptv`

## Assumptions

* Processing continues even if one repository encounters an error.
* The same repo can be included multiple times.