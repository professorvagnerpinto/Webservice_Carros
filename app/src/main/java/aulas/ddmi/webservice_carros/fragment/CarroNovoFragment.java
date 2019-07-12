package aulas.ddmi.webservice_carros.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import aulas.ddmi.webservice_carros.R;
import aulas.ddmi.webservice_carros.activity.CarroActivity;
import aulas.ddmi.webservice_carros.model.Carro;
import aulas.ddmi.webservice_carros.service.RetrofitSetup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vagner on 25/05/16.
 */
public class CarroNovoFragment extends BaseFragment {

    private Carro carro; //uma instância da classe Carro com escopo global para utilização em membros da classe
    private ProgressBar progressBarImg;  //uma progressbar para informar o processamento REST
    //componentes <-> objeto carro
    private RadioButton rbClassicos, rbEsportivos, rbLuxo; //campos referente ao tipo do objeto carro
    private EditText editTextNome; //campo referente ao atributo nome do objeto carro
    private EditText editTextDescricao; //campo referente ao atributo descrição do objeto carro
    private EditText editTextLatitude;  //campo referente ao atributo latitude do objeto carro
    private EditText editTextLongitude; //campo referente ao atributo longitude do objeto carro
    private ImageView imageViewFoto;  //campo referente ao atributo url_foto do objeto carro
    private EditText editTextUrlVideo; //campo referente ao atributo url_video do objeto carro

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); //informa ao sistema que o fragment irá adicionar botões na ActionBar

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novocarro, container, false); //infla o xml da UI e associa ao Fragment

        ((CarroActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_novocarro);  //um título para a janela

        //Cria uma instancia da classe de modelo
        carro = new Carro();

        //mapeia e inicializa os componentes da UI
        //Card0
        imageViewFoto = (ImageView) view.findViewById(R.id.imv_card0_frnovocarro);
        imageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cria uma Intent
                //primeiro argumento: ação ACTION_PICK "escolha um item a partir dos dados e retorne o seu URI"
                //segundo argumento: refina a ação para arquivos de imagem, retornando um URI
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //inicializa uma Activity. Neste caso, uma que forneca acesso a galeria de imagens do dispositivo.
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 0);
            }
        });
        //Card1
        rbClassicos = (RadioButton) view.findViewById(R.id.rb_classicos_card1_frnovocarro);
        rbClassicos.setChecked(true);
        rbEsportivos = (RadioButton) view.findViewById(R.id.rb_esportivo_card1_frnovocarro);
        rbLuxo = (RadioButton) view.findViewById(R.id.rb_luxo_card1_frnovocarro);
        //Card2
        editTextNome = (EditText) view.findViewById(R.id.etNome_card2_frnovocarro);
        editTextDescricao = (EditText) view.findViewById(R.id.etDescricao_card2_frnovocarro);
        //Card3
        editTextLatitude = (EditText) view.findViewById(R.id.etlatitude_card3_frnovocarro);
        editTextLongitude = (EditText) view.findViewById(R.id.etlongitude_card3_frnovocarro);
        //Card4
        editTextUrlVideo = (EditText) view.findViewById(R.id.etURLVideo__card4_frnovocarro);
        editTextUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cria uma Intent
                //primeiro argumento: ação ACTION_PICK "escolha um item a partir dos dados e retorne o seu URI"
                //segundo argumento: URI
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                //inicializa uma Activity. Neste caso, uma que forneca acesso a galeria de imagens do dispositivo.
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 0);
            }
        });

        return view;
    }

    /*
        Infla o menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_novocarro, menu);
    }

    /*
        Trata eventos dos itens de menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_salvar:{
                //carrega os dados do formulário no objeto
                if(!editTextNome.getText().toString().isEmpty()) {
                    carro.setNome(editTextNome.getText().toString());
                    carro.setDesc(editTextDescricao.getText().toString());
                    carro.setLatitude(editTextLatitude.getText().toString());
                    carro.setLongitude(editTextLongitude.getText().toString());
                    if(rbClassicos.isChecked()){
                        carro.setTipo(getContext().getResources().getString(R.string.tipo_classicos));
                    }else if(rbEsportivos.isChecked()){
                        carro.setTipo(getContext().getResources().getString(R.string.tipo_esportivos));
                    }else {
                        carro.setTipo(getContext().getResources().getString(R.string.tipo_luxo));
                    }
                    //emite uma caixa de diálogo de processando
                    showWait(getContext(), R.string.app_name, R.string.progressdialog_wait);
                    //Faz uma call com a operação POST para o servidor
                    Call call = new RetrofitSetup().getCarroService().inserir(carro);
                    call.enqueue(new Callback() { //executa em uma Thread separada da main
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.i("tag", response.message());
                            //retira a caixa de diálogo de processando
                            dismissWait();
                            //faz aparecer uma caixa de diálogo confirmando a operação
                            alertOk(R.string.title_confirmacao, R.string.msg_realizadocomsucesso);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.i("tag", "Falha ao realizar a requisição POST.");
                        }
                    });
                }else{
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.val_dadosinputs), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case android.R.id.home:
                getActivity().finish();
                break;
        }

        return true;
    }

    /**
     * Método que recebe o retorno da Activity de galeria de imagens.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            Log.d(TAG, data.toString());
            Uri arquivoUri = data.getData(); //obtém o URI da imagem
            Log.d(TAG, "URI do arquivo: " + arquivoUri);
            if(arquivoUri.toString().contains("images")) {
                imageViewFoto.setImageURI(arquivoUri); //coloca a imagem no ImageView
                carro.setUrlFoto(arquivoUri.toString()); //armazena o Uri para salvar a imagem no objeto imagem
            }else if(arquivoUri.toString().contains("video")) {
                editTextUrlVideo.setText(arquivoUri.toString()); //coloca a URL do vídeo no EditText
                carro.setUrlVideo(arquivoUri.toString()); //armazena o URL do vídeo no objeto do modelo
            }
        }
    }

}//fim classe
