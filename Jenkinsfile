def dockeruser = "coments99"
def imagename = "wordpress-with-java-covid"
def container = "wordpress-with-java-covid"

node {

stage('Git Checkout') {
    git 'https://github.com/bmapa-iscteiul/covid-graph-spread.git'
    }
    
stage('Build Docker Image'){
     powershell "docker build -f Dockerfile -t  ${imagename} ."
    }
    
    
stage('Docker compose'){
     powershell "Docker-compose up -d"
    }
    
   
   
stage ('Running Container to test built Docker Image'){
    powershell "docker run -dit -p 80:8080 --name ${container}  ${imagename}"
    }

   
}
