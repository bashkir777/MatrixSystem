
# MatrixSystem

MatrixSystem is a platform for online schools that helps tutors manage homework and tasks for their students and allows students solve tasks, homework and options of exam created by tutors.


## Deployment

To deploy and start this project:


1. Pull docker image using:

```docker
docker pull bashkir7777/matrix-system
```
2. Create docker volumes (if you have not already):

```docker
  docker volume create matrix-db
  docker volume create matrix-db-images
```
3. Run container using:

```docker
  docker run -d -p 8080:8080 -v matrix-db:/system/db -v matrix-db-images:/system/db/images --name matrix-app bashkir7777/matrix-system
```

4. Open link in browser
```url
http://localhost:8080/app/all-tasks
```
5. Login using precreated user:
```login
login: god
password: god
```
## Appendix

Note that the Miro board embedded here is for demonstration purposes only. To use it as intended, the administrator must have a premium Miro account

