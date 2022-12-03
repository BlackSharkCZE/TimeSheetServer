package cz.blackshark.modules.main.http.controller

import com.fasterxml.jackson.databind.ObjectMapper
import cz.blackshark.modules.commons.model.RestResponse
import cz.blackshark.modules.main.beans.RateBean
import cz.blackshark.modules.main.dto.RateVo
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.SecurityAttribute
import io.quarkus.test.security.TestSecurity
import io.quarkus.test.security.jwt.Claim
import io.quarkus.test.security.jwt.JwtSecurity
import io.restassured.http.Header
import io.restassured.mapper.ObjectMapperType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

//@QuarkusTest
class RateControllerTest {

    @Inject
    private lateinit var objectMapper: ObjectMapper

    @InjectMock
    private lateinit var rateBean: RateBean

//    @Test
    @TestSecurity(user = "testUser", roles = ["role1"])
    @JwtSecurity(
        claims = [
            Claim(key = "user", value = "testUserFromClaim"),
            Claim(key = "validated", value = "true"),
            Claim(key = "sub", value = "sub1234"),
            Claim(key = "subject", value = "subject1234")
        ]
    )
    fun `create rate without any data in database`() {
        val rateVo = RateVo(null, BigDecimal(7000), ZonedDateTime.now(), 1)
        Given {
            relaxedHTTPSValidation()
            header(Header("Content-Type", MediaType.APPLICATION_JSON))
            body(rateVo, ObjectMapperType.JACKSON_2)
        }  When {
            post("/rate/create")
        } Then {
            log().body()
            statusCode(Status.INTERNAL_SERVER_ERROR.statusCode)
            body("message", CoreMatchers.notNullValue())
            body("message", CoreMatchers.equalTo("Subject not found!"))
        } Extract {
            `as`(RestResponse::class.java)
        }
    }

}
