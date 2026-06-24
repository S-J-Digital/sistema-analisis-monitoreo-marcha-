package com.example.enviodatos.service;

import com.example.enviodatos.Dto.Datos.DatoParticipanteDto;
import com.example.enviodatos.Dto.Participante.ParticipanteUpdate;
import com.example.enviodatos.Dto.Participante.Participantedto;
import com.example.enviodatos.Dto.Patologia.PatologiaDto;
import com.example.enviodatos.Dto.Patologia.RelaciónDatoPatologiaDto;
import com.example.enviodatos.Dto.RedNeuronal.ModeloRequest;
import com.example.enviodatos.Dto.RedNeuronal.Prediccion;
import com.example.enviodatos.Dto.Sennal.ListaSenalesDTO;
import com.example.enviodatos.Dto.Sennal.SennalDto;
import com.example.enviodatos.Dto.UserLogin;
import com.example.enviodatos.Dto.UserName;
import com.example.enviodatos.Dto.Userdto;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @Headers({"Content-Type: application/json" })
    @POST("/api/v1/usuario/create")
    Call<ResponseBody> enviarusuario(@Body Userdto userdto);
    @Headers("Content-Type: application/json")
    @PUT("/api/v1/usuario/update/{id}")
    Call<ResponseBody> modificarusuario(@Path("id") Long id , @Body Userdto user );

    @GET("/api/v1/usuario/obtenerUsuarioporNombre/{nombre}")
    Call<UserName> buscarIdbynombre(@Path("nombre") String nombre );

    @DELETE("/api/v1/usuario/delete/{id}")
    Call<ResponseBody> eliminarusuario(@Path("id") Long id);

    //Login
    @Headers("Content-Type: application/json")
    @POST("/api/login/")
    Call<ResponseBody>login(@Body UserLogin userLogin);
    //Participante

    @Headers("Content-Type: application/json")
    @POST("/api/v1/participante/create")
    Call<ResponseBody> enviarparticipante(@Body Participantedto participantedto);

    @GET("/api/v1/participante/getIdbyParticipantebyCi/{ci}")
    Call<Integer> buscarIdbyCi(@Path("ci") String ci );

    @Headers("Content-Type: application/json")
    @PUT("/api/v1/participante/update/{id}")
    Call<ResponseBody> modificarParticipante(@Path("id") Long id , @Body ParticipanteUpdate participantedto );

    @DELETE("/api/v1/participante/delete/{id}")
    Call<ResponseBody> eliminarparticipante(@Path("id") Long id);

    //Patología
    @Headers("Content-Type: application/json")
    @POST("/api/v1/patologia/create")
    Call<ResponseBody> enviarpatologia(@Body PatologiaDto patologiaDto);
    @Headers("Content-Type: application/json")
    @POST("/api/v1/patologia/createrelacion")
    Call<ResponseBody> crearrelacionpatologiadato(@Body RelaciónDatoPatologiaDto relaciónDatoPatologiaDto);
    @DELETE("/api/v1/patologia/eliminarrelacion/iddato={iddato}&fecha={fecha}")
    Call<ResponseBody> eliminarRelacion(@Path("iddato") Long iddato, @Path("fecha") String fecha);
    @GET("/api/v1/patologia/obtenerIdbyNombre/{nombre}")
    Call<Integer> obtenerIdbynombre(@Path("nombre") String nombre);

    //Dato Participante
    @Headers("Content-Type: application/json")
    @POST("/api/v1/datoparticipante/create")
    Call<ResponseBody> enviardatoparticipante(@Body DatoParticipanteDto datoParticipanteDto);
    @GET("/api/v1/datoparticipante/obtenerIdbyparticipanteAndFecha/idParticipante={idParticipante}&fecha={fecha}")
    Call<Integer> obtenerIdbyparticipanteAndFecha(@Path("idParticipante") Long idParticipante,@Path("fecha") String fecha);
    @Headers("Content-Type: application/json")
    @PUT("/api/v1/datoparticipante/update/{id}")
    Call<ResponseBody> modificarDatoParticipante(@Path("id") Long id , @Body DatoParticipanteDto participantedto );

    @DELETE("/api/v1/datoparticipante/delete/{id}")
    Call<ResponseBody> eliminarDatoParticipante(@Path("id") Long id);

    //Señales
    @Headers("Content-Type: application/json")
    @POST("/api/v1/sennal/create")
    Call<ResponseBody> enviarSennales(@Body ListaSenalesDTO listaSenales);

    //Obtener PDF
    @GET("/api/v1/generarpdf/generar-pdf/{nombre}")
    Call<Map<String, String>> descargarPdf(@Path("nombre") String nombre);

    //Red Neuronal
    @Headers("Content-Type: application/json")
    @POST("/api/v1/modelo/predecir")
    Call<List<String>> predecir(@Body ModeloRequest request);

}
