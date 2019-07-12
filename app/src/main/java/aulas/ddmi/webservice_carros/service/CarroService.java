package aulas.ddmi.webservice_carros.service;

import aulas.ddmi.webservice_carros.dto.CarroSync;
import aulas.ddmi.webservice_carros.model.Carro;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by vagner on 22/02/18.
 */

public interface CarroService {
    //Call faz a chamada para o servidor.
    //Ela realiza a requisição em URL_BASE/*

    @GET("carros")
    Call<CarroSync> getCarros();

    @GET("carros/tipo/{tipo}")
    Call<CarroSync> getCarrosByTipo(@Path("tipo") String tipo);

    @POST("carros")
    Call<Void> inserir(@Body Carro carro);

    @PUT ("carros")
    Call<Void> alterar(@Body Carro carro);

    @DELETE("carros/{id}")
    Call<Void> excluir(@Path("id") String id);
}
