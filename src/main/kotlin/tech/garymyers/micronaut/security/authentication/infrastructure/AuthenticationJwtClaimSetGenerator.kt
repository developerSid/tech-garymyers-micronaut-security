package tech.garymyers.micronaut.security.authentication.infrastructure

import com.nimbusds.jwt.JWTClaimsSet
import io.micronaut.context.annotation.Replaces
import io.micronaut.runtime.ApplicationConfiguration
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.token.config.TokenConfiguration
import io.micronaut.security.token.jwt.generator.claims.ClaimsAudienceProvider
import io.micronaut.security.token.jwt.generator.claims.JWTClaimsSetGenerator
import io.micronaut.security.token.jwt.generator.claims.JwtIdGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Replaces(bean = JWTClaimsSetGenerator::class)
class AuthenticationJwtClaimSetGenerator @Inject constructor(
   tokenConfiguration: TokenConfiguration,
   jwtIdGenerator: JwtIdGenerator?,
   claimsAudienceProvider: ClaimsAudienceProvider?,
   applicationConfiguration: ApplicationConfiguration?
) : JWTClaimsSetGenerator(tokenConfiguration, jwtIdGenerator, claimsAudienceProvider, applicationConfiguration) {

   override fun populateWithUserDetails(builder: JWTClaimsSet.Builder?, userDetails: UserDetails?) {
      super.populateWithUserDetails(builder, userDetails)

      builder?.claim("user", userDetails?.username)
   }
}