import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.user.search.UserSearchService
import com.atlassian.jira.bc.user.search.UserSearchParams
import com.atlassian.jira.bc.group.search.GroupPickerSearchService
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.project.Project
import com.atlassian.jira.security.roles.ProjectRole
import com.atlassian.jira.security.roles.RoleActor
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.security.JiraAuthenticationContext
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;

/**
  @author Daniel Burke ~ dannyburke2012@yahoo.co.uk : https://github.com/dannyburke1
  @version 1.5
  
  This script is used to retrieve all of the users from JIRA (server!) using the Script Runner add-on (https://marketplace.atlassian.com/plugins/com.onresolve.jira.groovy.groovyrunner/cloud/overview)
  It retrieves, all users, their roles, groups and users in them, and all the projects with the users who have roles in them.

**/


def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager.class)
def projectRoles = projectRoleManager.getProjectRoles()
def projectSearchService = ComponentAccessor.getProjectManager()
def projectArray = projectSearchService.getProjectObjects()


//File to be written to, can be CSV or TXT
File file = new File("GIVE ME A LOCAL PATH")

//Gets all of the projects, along with project roles (i.e. "developer" + username and full name, i.e. bed2scp Daniel Burke ENGIT_Developer)
      for(Project projectarray in projectArray){
          for(ProjectRole projectrole in projectRoles){
    	      def actorRole = projectRoleManager.getProjectRoleActors(projectrole, projectarray) 
            def actorRoles = actorRole.getUsers()
            def displayNames = actorRoles.each {it.displayName}
        
              if(actorRoles.size() >= 1){
                 log.error "        " + projectarray +  ", " + "Project Lead: " + projectLead  + " Role : " + "  " + projectarray.getKey() + "_" + projectrole.getName() + "  " + "User with role in project: " + actorRole.getUsers().toArray() + "\n"
              	file.append( "" + ""  + projectarray.getKey() + "_" + projectrole.getName() + " \n "  + "= " + actorRoles + "\n" + "" + "\n" + displayNames +  "\n")
              }
              else if(actorRoles.size() < 1){   
               log.error projectarray.getKey() + "_" + projectrole.getName() +  "no user in role!"
              }
           
        }
      }
   

//PRINT THE ROLES AVAILABLE AND THE GROUPS
 log.error "        " + projectArray + " Project roles: " + projectRoles



//Retrieve all users based on a Query:

def userSearchService = ComponentAccessor.getComponent(UserSearchService.class);
UserSearchParams userSearchParams = (new UserSearchParams.Builder()).allowEmptyQuery(true).includeActive(true).includeInactive(true).maxResults(100000).build()

	log.error "\n"
	log.error "\n"
	log.error "        ------USERS------"
def user = userSearchService.findUsers("", userSearchParams).each{ it.getUser().getDisplayName()
    log.error "\n"
}

*/


//Retrieve all groups and users associated with them
def groupSearchService = ComponentAccessor.getComponent(GroupPickerSearchService);
def groupArray = groupSearchService.findGroups("")
def groupManager = ComponentAccessor.getGroupManager()

  for(Group group in groupArray){
    def groups = group.getName().toString()  
    file.append("" + "" + groups + " " + groupManager.getUsersInGroup(group) + "\n")
  }

