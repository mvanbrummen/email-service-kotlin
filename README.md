# Email Service

Email service built with SpringBoot and Kotlin. Uses Mailgun and SendGrid as email providers as primary and fallback, with the Chain of Command pattern.

See the original Java version [here](https://github.com/mvanbrummen/email-service)

------------------------------------------------------------------------------------------

#### Send Email

<details>
 <summary><code>POST</code> <code><b>/email/send</b></code></summary>

##### Parameters

#### Email Send Request

> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | from      |  required | Person   | Must be 'michaelvanbrummen@icloud.com' as I had to register a 'from email' in the providers  |
> | subject      |  required | String   | Email subject |
> | content      |  required | String  | Plain text email content  |
> | to      |  required |   Person[] |  List of recipients  |
> | cc      |  optional |   Person[] |  List of CC recipients  |
> | bcc      |  optional |   Person[] | List of BCC recipients  |

#### Person
> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | email      |  required |   String | Valid email address  |
> | name      |  optional |   String |  Name of the person |
##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `202`         |         | None                                |
> | `400`         | `application/json`                |   `{"field_name":"validation error message"}`                          |
> | `502`         | `application/json`         |        `{"error": "Service is currently unavailable. Please try again later."}`                                                         |

##### Example cURL

> ```javascript
>  curl -X POST -H "Content-Type: application/json" --data @email-request.json http://localhost:8080/email/send
> ```

</details>

------------------------------------------------------------------------------------------

## Getting Started

Build the app
```
mvn clean install 
```

Run the app
``` 
export MAILGUN_API_KEY=$MAILGUN_API_KEY
export SENDGRID_API_KEY=$SENDGRID_API_KEY
mvn spring-boot:run
```