# covid-graph-spread 
Grupo 33
Bruno Pereira 82990
Miguel Neto 68625

Covid-graph-spread consists of 2 minor applications. 
  - Covid-graph-spread
   -Covid-query
   

Covid-graph-spread 
Every requirement was fully developed. 

Covid-query
The app is able to extract all metadata from pdf files using CERMINE, however due to the fact that there is no Covid Scientific Discoveries Repository, the files used were the ones available at e-learning, and accessed localy.
Wasnt able to embeed it in the Wordpress website through the shell script because an error was thrown (500 Internal Server Error) that its still yet to be solved.

How to make the apps work in a docker container:
  1. Create a folder
  2. Move Dockerfile and docker-compose.yml files to the folder 
  3. Open a command line terminal
  4. Use the command "docker build -t wordpress-with-java-covid"
  5. Use the command "docker-compose up -d"
  6. Take all the files in the "jars" folder of the git repository, and place them at the cgi-bin folder that was just created
  7. Browse to "http://localhost" to visit the website
  
  
