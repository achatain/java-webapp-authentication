java-webapp-authentication
==========================
[![Build Status](https://travis-ci.org/achatain/java-webapp-authentication.svg?branch=master)](https://travis-ci.org/achatain/java-webapp-authentication)
[![Coverage Status](https://coveralls.io/repos/github/achatain/java-webapp-authentication/badge.svg?branch=master)](https://coveralls.io/github/achatain/java-webapp-authentication?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.achatain/java-webapp-authentication/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.achatain/java-webapp-authentication/)

https://github.com/achatain/java-webapp-authentication

#What is it?
JWA (**J**ava **W**ebapp **A**uthentication) assists in leveraging [Google Sign-In](https://developers.google.com/identity/sign-in/web/) for backend server applications built with Java. Despite being in its early days, JWA provides you with a bunch of robust and easy to use features:
 - authentication filter
 - sign-in and sign-out servlets
 - session management service
 - etc.
 
#How do I integrate it in my backend app?
 1. Add the dependency in your pom file
 
 ```xml
<dependency>
  <groupId>com.github.achatain</groupId>
  <artifactId>java-webapp-authentication</artifactId>
  <version>1.1.0</version>
</dependency>
 ```
 
 2. Install the **AuthenticationModule** to enable the dependency injection (suggested to use [Google Guice](https://github.com/google/guice))
 
 ```java
  class AppConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
      return Guice.createInjector(
        new AuthenticationModule()
      );
    }
  }
 ```
 
 3. Filter your restricted API through the **SessionFilter** and serve the sign-in and sign-out servlets
 
 ```java
  class AppServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
      Map<String, String> initParams = new HashMap<>();
      initParams.put(SessionFilter.LOGIN_URL_REDIRECT, "https://myapp.com/google-sign-in/");
      filter("/api/*").through(SessionFilter.class, initParams);
      serve("/google-auth").with(GoogleSigninServlet.class);
      serve("/signout").with(SignOutServlet.class);
    }
  }
 ```
 
 #How do I know who is logged-in?
 From any servlet filtered through the **SessionFilter**, you can get the current user thanks to the **SessionService**
 
 ```java
 public class MyServlet extends HttpServlet {

    private final transient SessionService sessionService;

    @Inject
    private MyServlet(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        AuthenticatedUser user = sessionService.getUserFromSession(req.getSession());
        System.out.println("The logged-in user is " + user);
    }
}
 ```
