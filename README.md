# froggerz
A Frogger-esque game for CS201L's group project

Since we're all running on our own localhost, 
if anyone wants to check / play around / start adding
their parts of the project on top of this, run the DatabaseTest
file through your instance of MySQL (since we're all
currently on localhost). Eventually we will only need 
one person to setup the database and we can all
access it from there. Note that *your*
MySQL username/password combo will have to be:
user=root password=root
to work unmodified - If you plan on changing the
values in Login.java, see the KPTODO comment

Update: I cloned the updated repo into a different
workspace and it worked fine. For reference:
https://github.com/collab-uniba/socialcde4eclipse/wiki/How-to-import-a-GitHub-project-into-Eclipse

How to build:
	Linux or bash: Run "./gradlew html:dist"
	Windows: Run "gradlew html:dist"
 
Files to serve:
	Files are located in "html/build/dist"
