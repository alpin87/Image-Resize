<h3>스프링부트 thumbnail 라이브러리와 AWS S3저장소를 이용해 이미지 리사이징 프로젝트 </h3>



![image](https://github.com/user-attachments/assets/f59969dc-2099-4d2e-ad92-41bba071acb9)


# 프로젝트 구조

```bash
src/
├── main/
   ├── java/ 
   │   └── com/ 
   │       └── example/ 
   │           └── imageresizer/ 
   │               ├── ImageResizerApplication.java 
   │               ├── config/ 
   │               │   ├── AwsConfig.java 
   │               │   
   │               ├── controller/ 
   │               │   └── ImageController.java 
   │               ├── service/ 
   │               │   ├── ImageService.java 
   │               │   └── S3Service.java 
   │               ├── domain/ 
   │               │   └── Image.java 
   │               ├── dto/ 
   │               │   ├── request/ 
   │               │   │   └── ImageUploadRequest.java 
   │               │   └── response/ 
   │               │       └── ImageUploadResponse.java
   │               ├── repository/
   │               │       └── ImageREpository.java 
   │               └── exception/ 
   │                   ├── CustomException.java 
   │                   └── GlobalExceptionHandler.java 
   └── resources/ 
       └── application.yml
```
<hr>
<h2>결과</h2>
<br>
HTTP - POST
<br>
Body - from-data
<br>

<h3>요청 파라미터 설정</h3>
POSTMAN 키-값 설정:

| Key                | Value                           | Type    |
|--------------------|---------------------------------|---------|
| **file**           | Actual image file               | File  |
| **targetWidth**     | 800                             | Integer |
| **targetHeight**    | 600                             | Integer |
| **maintainAspectRatio** | true                       | Boolean |
| **quality**        | 0.8                             | Float   |


![postman 성공](https://github.com/user-attachments/assets/a65b83a2-0c44-4c08-a19f-6074fa1d3142)
![성공시 서버로그](https://github.com/user-attachments/assets/a9724752-5385-4be5-86b5-0521071c010b)

<h2>AWS S3저장소</h2>
<h3>원본 이미지 저장소</h3>

![image](https://github.com/user-attachments/assets/4e4dae14-e504-465d-87b2-e53f41fe2f44)



<h3>리사이징된 이미지 저장소</h3>

![image](https://github.com/user-attachments/assets/8e5f555b-3110-41e9-a84d-13b5b753d27d)

<h3>원본 이미지에서 리사이징된 이미지와 33.2%정도 용량감소됨</h3>











