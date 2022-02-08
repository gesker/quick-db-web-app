FROM docker.io/openjdk:17-alpine
COPY target/quick-bootable.jar /quick-bootable.jar
CMD ["java", "-jar", "/quick-bootable.jar", "-b=0.0.0.0"]

# Tag and push to YOUR docker repository
# Add that address and tag to the K8s.yaml file if you use kubernetes