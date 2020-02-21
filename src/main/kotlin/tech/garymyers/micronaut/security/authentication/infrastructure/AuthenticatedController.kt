package tech.garymyers.micronaut.security.authentication.infrastructure

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.UNAUTHORIZED
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.micronaut.security.utils.SecurityService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tech.garymyers.micronaut.security.authentication.AuthenticatedUserInformation
import javax.inject.Inject

@Secured(IS_AUTHENTICATED)
@Controller("/api/authenticated")
class AuthenticatedController @Inject constructor(
   private val securityService: SecurityService
) {
   private val logger: Logger = LoggerFactory.getLogger(AuthenticatedController::class.java)

   @Secured(IS_ANONYMOUS)
   @Get(produces = [APPLICATION_JSON])
   fun authenticated(authentication: Authentication?, httpRequest: HttpRequest<*>): HttpResponse<AuthenticatedUserInformation> {
      logger.info("authentication {}", authentication)
      logger.info("securityService.authentication {}", securityService.authentication.orElse(null))

      return if (authentication != null) {
         HttpResponse.ok(AuthenticatedUserInformation(authentication.name))
      } else {
         HttpResponse.status(UNAUTHORIZED)
      }
   }
}