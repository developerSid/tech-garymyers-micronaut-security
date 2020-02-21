package tech.garymyers.micronaut.security.authentication.infrastructure

import groovy.json.JsonSlurper
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.annotation.MicronautTest
import javax.inject.Inject
import spock.lang.Specification


import static io.micronaut.http.HttpRequest.GET
import static io.micronaut.http.HttpRequest.POST
import static io.micronaut.http.HttpStatus.OK
import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest
class AuthenticatedControllerSpecification extends Specification {
   @Client("/api") @Inject RxHttpClient httpClient

   def jsonSlurper = new JsonSlurper()

   void "login and check auth success" () {
      setup:
      final token = httpClient.toBlocking().exchange(POST("/login", new UsernamePasswordCredentials("user1", "password")), BearerAccessRefreshToken).body().accessToken

      when:
      def response = httpClient.toBlocking().exchange(GET("/authenticated").header("Authorization", "Bearer $token"), Argument.of(String), Argument.of(String))

      then:
      notThrown(Exception)
      response.status() == OK
      def json = response.body().with { jsonSlurper.parseText(it) }
      json.username == "user1"
   }

   void "login and check auth failure due to bad username" () {
      when:
      httpClient.toBlocking().exchange(POST("/login", new UsernamePasswordCredentials("user2", "password")), BearerAccessRefreshToken).body().accessToken

      then:
      final e = thrown(HttpClientResponseException)
      e.response.status == UNAUTHORIZED
   }
}
