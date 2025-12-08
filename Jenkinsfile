pipeline {
    agent { label 'build' }

	environment {
       GITHUB_CRED=credentials('github-access-token')
    }

	options {
		disableConcurrentBuilds()
		timeout(time: 30, unit: 'MINUTES') 
		buildDiscarder(logRotator(numToKeepStr: '14'))
	}

	stages {
		stage('setup-github') {
			steps {
                sh '''
                   ### git user info !! important to reuse pod. 
                   git config --global user.email "mirinus@gmail.com"
                   git config --global user.name "cj kim"
				   git config --global url.https://${GITHUB_CRED_USR}:${GITHUB_CRED_PWD}@github.com/cnapcloud/.insteadOf https://github.com/cnapcloud/
				'''
			}	
        }
		

		stage("build") {
			steps {
			   sh '''	
                  make build
                '''        			       			
		    }
	    }

	    
	   stage('push image') {	
	        steps {
                sh '''
                     make docker-build
			    ''' 	 
	  	    }
	    }
	    
	    stage('update-overlay') {	
	        when {
                expression {
                    return  env.BRANCH_NAME == 'main'
                }
            }
		    steps {
                sh '''
			       make update-overlay
			    ''' 	 
	   	   }
	    }

	}	

}	