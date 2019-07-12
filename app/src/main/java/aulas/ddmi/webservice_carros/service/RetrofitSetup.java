package aulas.ddmi.webservice_carros.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by vagner on 22/02/18.
 */

public class RetrofitSetup {

    private final Retrofit retrofit;

    //configura o retrofit
    public RetrofitSetup() {
        //para gerar um log mais elaborado
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder(); //cria um cliente para o interceptor
        client.addInterceptor(interceptor); //adiciona o interceptor no cliente

        //cria o objeto retrofit, com ele você fará suas calls para o servidor na URL_BASE que você definiu aqui
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.252.166:8080/api/rest/")
                .addConverterFactory(GsonConverterFactory.create()) //JacksonConverterFactory.create()
                .client(client.build()) //diz quem será o cliente no retrofit
                .build();
    }

    //cria o serviço
    public CarroService getCarroService(){
        return retrofit.create(CarroService.class);
    }
}
