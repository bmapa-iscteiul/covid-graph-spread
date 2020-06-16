# covid-graph-spread 
Grupo 33
Bruno Pereira 82990
Miguel Neto 68625

Covid-graph-spread consists of 2 minor applications. 
  - Covid-graph-spread (Bruno)
  - Covid-query (Miguel)
   

Covid-graph-spread 
The app is able to extract all the tags and files from the repository, however it may be faulty when adding new commits to the repository.

Covid-query
The app is able to extract all metadata from pdf files using CERMINE, however due to the fact that there is no Covid Scientific Discoveries Repository, the files used were the ones available at e-learning, and accessed localy.
Wasnt able to embeed it in the Wordpress website through the shell script because an error was thrown (500 Internal Server Error) that its still yet to be solved.

How to make the apps work in a docker container:
  1. Create a folder
  2. Move Dockerfile and docker-compose.yml files to the folder 
  3. Open a command line terminal
  4. Use the command "docker build -t wordpress-with-java-covid"
  5. Use the command "docker-compose up -d"
  6. Extract the .zip file from the github project
  7. Take all the files in the "jars" folder of the git repository, and place them at the cgi-bin folder that was just created
  8. Browse to "http://localhost" to visit the website
  
  
