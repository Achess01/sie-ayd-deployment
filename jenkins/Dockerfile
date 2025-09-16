# jenkins/Dockerfile
FROM jenkins/jenkins:lts

USER root

# Install Docker CLI
RUN apt-get update && \
    apt-get install -y docker.io && \
    usermod -aG docker jenkins

USER jenkins

# Install plugins
RUN jenkins-plugin-cli --plugins \
    git \
    docker-pipeline \
    pipeline-stage-view \
    github-pullrequest
