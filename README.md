# CarMate

# Small Git Tutorial 

1. Before making changes, open a new local branch using `git checkout -b <branch-name>`
2. If you want to see other remote or local branches, `git branch` will show them,
and you can retrieve more with `git fetch` from the remote repository
3. When you want to update local changes to a remote branch, use `git add .` to include all files
from current directory, then `git commit -m "your message here"`
4. To push to a new remote branch, use `git push -u origin <branch-name>`. This will initialize
a new remote branch with the name you entered. If you make further pushes to this branch,
you can simply do `git push` once it has been set up.
5. If you want to merge your branch with the main, on github a popup will be available for recent pushes
and you can click Create Pull Request. Enter any relevant information, and then create to allow 
team members to review your changes before merging to the main.
6. Once a pull request is reviewed and accepted, merge then delete the remote branch. You are then
welcome to delete your local version of the branch by first moving out of it to the main
with `git checkout main` then `git branch -D <branch-name>`. 
7. To make sure your main is updated after a pull request, use `git pull` to retrieve from the
remote repository.

# Getting Maps API Key Working
1. Find your root level `local.properties` file and put in MAPS_API_KEY="yourkey"
2. Find your `AndroidManifest.xml` and put this code in the application layer of the xml `<meta-data
   android:name="com.google.android.geo.API_KEY"
   android:value="${MAPS_API_KEY}" />`
3. Add maps dependency to app level build.gradle `implementation libs.play.services.maps`
4. 

