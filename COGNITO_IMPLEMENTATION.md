# AWS Cognito Authentication Implementation

This document describes the implementation of AWS Cognito authentication for the Feature Flag Manager application.

## Implementation Overview

The following components have been implemented to enable AWS Cognito authentication:

1. **Dependencies**: Added Spring Security and OAuth2 dependencies to `build.gradle`.
2. **Configuration**: Added AWS Cognito configuration to `application.properties`.
3. **Security Configuration**: Created `SecurityConfig.java` to configure Spring Security with AWS Cognito.
4. **Authentication Controllers**: Created controllers for login, callback, and authentication testing.
5. **UI Components**: Added HTML pages for login and authentication callback.

## Configuration Details

### AWS Cognito Configuration

The AWS Cognito configuration is set in `application.properties`:

```properties
spring.security.oauth2.client.registration.cognito.client-id=2eno1m49skl28h41v522dljlq8
spring.security.oauth2.client.registration.cognito.client-secret=${COGNITO_CLIENT_SECRET:your-client-secret}
spring.security.oauth2.client.registration.cognito.scope=email,openid,profile
spring.security.oauth2.client.registration.cognito.redirect-uri=http://localhost:3000/callback
spring.security.oauth2.client.provider.cognito.issuer-uri=https://cognito-idp.us-east-1.amazonaws.com/us-east-1_Lw7jlzEjb
spring.security.oauth2.client.provider.cognito.user-name-attribute=username
```

**Important**: Replace `your-client-secret` with your actual client secret or set the `COGNITO_CLIENT_SECRET` environment variable.

### Security Configuration

The security configuration in `SecurityConfig.java` defines:

- Public endpoints that don't require authentication
- Protected endpoints that require authentication
- OAuth2 login configuration
- JWT resource server configuration

## Testing the Implementation

To test the implementation:

1. **Start the application**:
   ```
   ./gradlew bootRun
   ```

2. **Access the login page**:
   - Navigate to `http://localhost:8080/login-page`
   - Click the "Log in with AWS Cognito" button

3. **Test authentication**:
   - Public endpoint: `http://localhost:8080/api/auth-test/public` (accessible without login)
   - Protected endpoint: `http://localhost:8080/api/auth-test/protected` (requires login)
   - User info: `http://localhost:8080/user-info` (shows user details after login)

4. **API Access**:
   - For programmatic API access, obtain an access token from AWS Cognito
   - Include the token in your requests as a Bearer token:
     ```
     curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" http://localhost:8080/api/auth-test/protected
     ```

## Troubleshooting

If you encounter issues:

1. **Check the logs** for error messages
2. **Verify the client secret** is correctly set
3. **Ensure the redirect URI** matches the one configured in your Cognito User Pool
4. **Check CORS settings** if accessing from a different domain

## Additional Resources

- [Spring Security OAuth2 Documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [AWS Cognito Developer Guide](https://docs.aws.amazon.com/cognito/latest/developerguide/what-is-amazon-cognito.html)