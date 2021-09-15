# TODO List

### Task
* Long id;
* String text;
* Boolean isComplete;
* Date createDate;
* Date updateDate;

### Methods
* findAll: [GET] /task
* findById: [GET] /task/{id} 
* add: [POST] /task 
* save: [PUT] /task/{id}
* delete: [DELETE] /task/{id}


### Swagger
http://localhost:8090/swagger-ui.html

mvn clean package

### Docker
* Для создания docker образа: 
 `docker build -t todo-backend . `
    
* Для запуска docker образа:
 `docker run --net=bridge -p 8090:8090 todo-backend`