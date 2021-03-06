package ar.gob.coronavirus.data.remoto

import ar.gob.coronavirus.data.remoto.modelo.*
import ar.gob.coronavirus.data.remoto.modelo_autodiagnostico.RemoteSelfEvaluation
import ar.gob.coronavirus.utils.Constantes
import ar.gob.coronavirus.utils.Constantes.NOTIFICATIONS
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

/**
 * La autenticación por DNI y nro de trámite es imperfecta pero es la más viable de las
 * opciones disponibles al momento. La situación de emergencia sanitaria impide la concreción de un
 * trámite presencial para el alta.
 *
 * Los mecanismos biométricos suben la barrera de entrada tecnológica además de presentar otros desafíos.
 * Los mecanismos como el de AFIP, si bien más robustos, dejan afuera a un porcentaje alto de la población.
 * Algo similar sucede con los proveedores de identidad extranjeros, sumado al problema que representan
 * para la soberanía digital.
 *
 * En el mediano plazo se requiere de un mecanismo masivo que permita al grueso de la población poder
 * darse de alta en poco tiempo, sin hacer trámites presenciales.
 *
 * A su vez, la identificación de "sexo", que suena tan antagónica hoy en día, es un requisito
 * necesario para poder validar DNIs, que se utiliza en parte para mitigar el problema de los números de
 * DNIs repetidos (herencia de la era pre digital en la asignación de número de DNIs).
 */
interface CovidApiService {
    @POST(Constantes.AUTHORIZATION_ENDPOINT)
    fun authorize(@Body body: UserAuthorization): Single<Token>

    @POST(Constantes.REFRESH_ENDPOINT)
    fun refresh(@Body body: TokenRefreshBody): Single<Token>

    @GET("usuarios/{dni}")
    fun getUser(@Path("dni") dni: String, @Query("sexo") gender: String): Single<RemoteUser>

    @PATCH("usuarios/{dni}")
    fun updateUser(
            @Path("dni") dni: String,
            @Query("sexo") gender: String,
            @Body userInformationUpdate: UserInformationUpdate): Single<RemoteUser>

    @POST("usuarios/{dni}/${NOTIFICATIONS}/registrar")
    fun registerToken(@Path("dni") dni: Long, @Query("sexo") gender: String, @Body pushBody: PushBody): Completable

    @POST("usuarios/{dni}/${NOTIFICATIONS}/desregistrar")
    fun unregisterToken(@Path("dni") dni: String, @Query("sexo") gender: String, @Body pushBody: PushBody): Completable

    @POST("/usuarios/{dni}/autoevaluaciones")
    fun confirmSelfEvaluation(
            @Path("dni") dni: String,
            @Query("sexo") gender: String,
            @Body remoteSelfEvaluation: RemoteSelfEvaluation
    ): Single<SelfEvaluationResponse>
}