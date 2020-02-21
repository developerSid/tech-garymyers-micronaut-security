package tech.garymyers.micronaut.security.authentication.infrastructure

import io.micronaut.core.async.publisher.Publishers.just
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import org.reactivestreams.Publisher
import javax.inject.Singleton

@Singleton
class UserAuthenticationProvider : AuthenticationProvider {
   override fun authenticate(authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse> {
      val identity = authenticationRequest?.identity as String
      val secret = authenticationRequest.secret as String

      return if (identity == "user1" && secret == "password") {
         just(UserDetails(identity, emptyList()))
      } else {
         just(AuthenticationFailed(CREDENTIALS_DO_NOT_MATCH))
      }
   }
}