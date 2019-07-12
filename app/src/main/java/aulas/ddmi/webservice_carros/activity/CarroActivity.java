package aulas.ddmi.webservice_carros.activity;

import android.os.Bundle;
import android.util.Log;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.fragment.CarroDetalheFragment;
import aulas.ddmi.webservice_carros.fragment.CarroNovoFragment;
import aulas.ddmi.webservice_carros.model.Carro;

/**
 * Esta classe é um container para os fragmentos CarroNovoFragment, CarroDetalheFragment
 * e CarroEdicaoFragment.
 * Created by vagner on 11/08/16.
 */
public class CarroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //associa um layout a esta Activity
        setContentView(R.layout.activity_carro);

        //obtém do extras da intent recebida o fragmento que ela deve abrir
        String msg = (String) getIntent().getCharSequenceExtra("qualFragmentAbrir");
        Log.d(TAG, "Qual fragment abrir? " + msg);
        if(msg.equals("CarroNovoFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarroNovoFragment()).commit();
        }else if(msg.equals("DetalheCarroFragment")){
            Log.d(TAG, "Entrou no else");
            //constrói uma instância do Fragment CarroDetalheFragment
            CarroDetalheFragment carroDetalheFragment = new CarroDetalheFragment();
            //obtém o carro que foi repassado pela CarrosActivity ao chamar esta Activity
            Carro carro = (Carro) getIntent().getSerializableExtra("carro");
            Log.d(TAG, "Objeto carro recebido em CarroActivity: " + carro.toString()); //um log para o LogCat
            //repassa o objeto carro para o fragmento
            carroDetalheFragment.setCarro(carro);
            //insere o fragmento como conteúdo de content_main.xml
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, carroDetalheFragment).commit();
        }
    }
}
